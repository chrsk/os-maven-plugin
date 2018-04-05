package org.opensaga.plugin.builder.meta;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.opensaga.plugin.builder.meta.ModelCandidateResolver.ModelFileSet;
import org.opensaga.plugin.builder.meta.generator.JavaSourceGenerator;
import org.opensaga.plugin.builder.meta.generator.MetaGenerationFailedException;
import org.opensaga.plugin.builder.meta.generator.VelocityBasedJavaSourceGenerator;
import org.opensaga.plugin.builder.meta.generator.model.DomainTypeMetaModel;
import org.opensaga.plugin.builder.meta.generator.model.MetaModel;
import org.opensaga.plugin.builder.meta.generator.model.PropertyMetaModel;
import org.opensaga.plugin.builder.meta.parser.HandlerContext;
import org.opensaga.plugin.builder.meta.parser.ModelContextDependentPostProcessor;
import org.opensaga.plugin.builder.meta.parser.ModelHandler;
import org.opensaga.plugin.builder.meta.parser.PropertyType;
import org.opensaga.plugin.builder.meta.parser.TypeBasedModelParser;
import org.opensaga.plugin.builder.meta.parser.XmlParserFailureException;
import org.opensaga.plugin.builder.meta.parser.types.DomainMetaModelPostProcessor;
import org.opensaga.plugin.builder.meta.parser.types.DomainTypeModelHandler;
import org.opensaga.plugin.builder.meta.parser.types.ExternalDomainTypeModelHandler;
import org.opensaga.plugin.builder.meta.parser.types.ProcessMetaModelPostProcessor;
import org.opensaga.plugin.builder.meta.parser.types.ProcessModelHandler;
import org.opensaga.plugin.builder.meta.parser.types.RelationModelHandler;
import org.opensaga.plugin.builder.meta.parser.types.TestDomainMetaModelPostProcessor;
import org.opensaga.plugin.builder.meta.parser.types.TestDomainTypeModelHandler;
import org.opensaga.plugin.builder.meta.parser.types.TestExternalDomainTypeModelHandler;
import org.opensaga.plugin.builder.meta.parser.types.ViewModelHandler;
import org.opensaga.plugin.util.DocumentRootQNameResolver;
import org.opensaga.plugin.util.DocumentRootQNameResolver.DocumentRootQNameNotFoundException;
import org.opensaga.plugin.util.NameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;

/**
 * Creates java source files which contains meta informations about the domain
 * types defined in the project. The generated source files enables the use of
 * rapid error feedback at compile time, while programming in Java with
 * OpenSAGA.
 * <p>
 * This Mojo builds meta models from a given model base directory. It's possible
 * to configure the target directory where the generated files should be stored.
 * The {@code JavaBasedMetaModelClassBuilder} picks up all extensions, if
 * they're not constrained by {@link #includedExtensions} or
 * {@link #excludedExtensions}.
 * <p>
 * All generated domain type meta models are also generated in a global domain
 * class, which enables access to all meta informations about the domain types.
 * To change the domain name please see {@link #domainPrefix}.
 * 
 * @author cklewes
 */
public class JavaBasedMetaModelClassBuilder
{

    private static final Logger log = LoggerFactory.getLogger(JavaBasedMetaModelClassBuilder.class);

    /**
     * Defines the target directory for the generated classes, the classes are
     * stored into this directory with the additional package name.
     * 
     * @see #packageName
     */
    private final String targetDirectory;

    /**
     * Defines the target directory for the generated classes, the classes are
     * stored into this directory with the additional package name.
     * 
     * @see #packageName
     */
    private final String targetTestDirectory;

    /**
     * Provides the model base directory where the domain-type models can be
     * found. If you want to use the {@code MetaModelBuilderMojo} in combination
     * with the extension exclude/include, please make sure you provide the
     * extension parent folder.
     */
    private final String modelBaseDirectory;

    /**
     * A list of all included extensions. If set only extensions which are
     * defined will be searched, nothing else.
     */
    private String[] includedExtensions = new String[0];

    /**
     * A list of all excluded extensions. If set only extensions which are not
     * excluded will be searched, nothing else.
     */
    private String[] excludedExtensions = new String[0];

    /**
     * A list of all excluded domain types. By default all domain types are
     * included, respectively to {@link #excludedExtensions} and
     * {@link #includedExtensions}
     * 
     * @parameter
     */
    private String[] excludedModels = new String[0];

    /**
     * The package name for the generated classes. This should be adjusted for
     * the specific project to your default package.
     */
    private String packageName = "org.opensaga.runtime.model.domain.meta";

    /**
     * The domain prefix for the domain class which provides access to all
     * domains.
     */
    private final String domainPrefix;

    /**
     * Generate the integration test support to the {@code src/test/java} folder. 
     */
    private boolean integrationTestSupport = true;
    
    /**
     * The model parser dependent configurations
     */
    private Map<String, Map<String, Object>> modelParserDependentConfiguration = new HashMap<String, Map<String, Object>>();


    public JavaBasedMetaModelClassBuilder(String targetDirectory, String targetTestDirectory, String modelBaseDirectory, String domainPrefix)
    {
        this.targetDirectory = targetDirectory;
        this.targetTestDirectory = targetTestDirectory;
        this.modelBaseDirectory = modelBaseDirectory;
        this.domainPrefix = NameUtils.toJavaClassName(domainPrefix);
    }


    @SuppressWarnings("unchecked")
    public void generateMetaModels() throws MetaGenerationFailedException
    {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        
        // Delete all old generated artifacts
        cleanupTargetDirectory(targetDirectory);
        cleanupTargetDirectory(targetTestDirectory);

        final Map<TypeBasedModelParser, List<MetaModel>> modelParserList = getAvailableTypeBasedModelParser();
        Collection<ModelCandidate> candidates = findModelCandidates(modelParserList.keySet());
        
        
        final Map<HandlerType, JavaSourceGenerator> generators = new HashMap<HandlerType, JavaSourceGenerator>();
        generators.put(HandlerType.SOURCE_DOMAIN, new VelocityBasedJavaSourceGenerator(targetDirectory, packageName));
        generators.put(HandlerType.TEST_DOMAIN, new VelocityBasedJavaSourceGenerator(targetTestDirectory, packageName));

        final ConcurrentModelBuilder concurrentModelBuilder = new ConcurrentModelBuilder(generators);
        final HandlerContext handlerContext = new HandlerContext();

        if(integrationTestSupport)
        {
            log.info("The integration test support is active.");
        }
        
        log.info("Found '{}' model candidates, not all may be handled.", candidates.size());
        int generatedFiles = 0;

        log.info("Phase 1: Parse all meta model candidates.");
        List<Callable<List<MetaModel>>> callables = new ArrayList<Callable<List<MetaModel>>>();
               
        for (final ModelCandidate candidate : candidates)
        {
            callables.add(new Callable<List<MetaModel>>()
            {
                @Override
                public List<MetaModel> call() throws Exception
                {
                    TypeBasedModelParser typeBasedModelHandler = candidate.getHandler();
                    configureTypeBasedModelHandler(typeBasedModelHandler);

                    try
                    {
                        return typeBasedModelHandler.parseModel(candidate.getFile());
                    }
                    catch (XmlParserFailureException e)
                    {
                        throw new MetaGenerationFailedException("A domain type model couldn't be parsed by " +
                            "XML parser. The file name was: '" + candidate.getFile().getAbsolutePath(), e);
                    }
                }
            });
        }
        
        concurrentModelBuilder.handleModelCandidatesConcurrently(modelParserList, callables);
 

        log.info("Phase 2: Merging meta model properties.");
        mergeMultipleMetaModels(modelParserList);

        log.info("Phase 3: Dereferencing domaintype properties.");

        for (List<MetaModel> metaModels : modelParserList.values())
        {
            for (MetaModel model : metaModels)
            {
                if (model instanceof DomainTypeMetaModel)
                {
                    for (PropertyMetaModel propertyModel : ((DomainTypeMetaModel) model).getMetaPropertyModels())
                    {
                        if (propertyModel.getPropertyRef() != null && propertyModel.getType() == null)
                        {
                            log.debug("Resolving model '{}' with reference '{}'.", propertyModel.getId(),
                                propertyModel.getPropertyRef());
                            PropertyType type = findReferencedPropertyType(modelParserList,
                                propertyModel.getPropertyRef(), propertyModel.getId());
                            propertyModel.setType(type);
                        }
                    }
                }
            }
        }
        
        log.info("Phase 4: Propagating the handler contexts");
        for (Entry<TypeBasedModelParser, List<MetaModel>> entry : modelParserList.entrySet())
        {
            TypeBasedModelParser typeBasedModelHandler = entry.getKey();

            if (typeBasedModelHandler instanceof TypeBasedModelParser)
            {
                handlerContext.addContextForHandlerWithId(typeBasedModelHandler.getId(), entry.getValue());
            }
        }
        
        log.info("Phase 5: Writing Java classes");
        for (Entry<TypeBasedModelParser, List<MetaModel>> entry : modelParserList.entrySet())
        {
            TypeBasedModelParser typeBasedModelHandler = entry.getKey();

            if (typeBasedModelHandler instanceof ModelHandler)
            {
                final ModelHandler<MetaModel> modelHandler = (ModelHandler<MetaModel>) typeBasedModelHandler;

                // Sorting the domain type info into a consistent stable order.
                // This is necessary for source controlled projects to ensure the file remains
                // unchanged if no changes in the domain types are performed.
                
                final List<MetaModel> metaModelList = entry.getValue();
                Collections.sort(metaModelList, new IdBasedLexicographicalMetaModelComparator());
                
                final int generatedFilesByBuilder = concurrentModelBuilder.executeClassGenerationConcurrently(handlerContext, modelHandler, metaModelList);
                log.debug("The builder '{}' generated '{}' files", typeBasedModelHandler.getId(), generatedFilesByBuilder);
                
                generatedFiles += generatedFilesByBuilder;
            }
        }
        
        log.info("Phase 4: Postprocessing generated Java classes (creating domain classes)");

        for (ModelContextDependentPostProcessor postProcessor : getModelPostProcessors())
        {
            final JavaSourceGenerator generator = generators.get(postProcessor.getType());
            generatedFiles += postProcessor.handleModel(handlerContext, generator);
        }

        log.info("Generated '{}' Java classes.", (generatedFiles));
        log.info("The complete analyzing and generation completed in '{}'.", stopWatch.toString());
    }

    /**
     * Configures the given model handler with the found configuration
     *  
     * @param typeBasedModelHandler The model handler which should be configured.
     */
    protected void configureTypeBasedModelHandler(TypeBasedModelParser typeBasedModelHandler)
    {
        for (Map.Entry<String, Map<String, Object>> configuration : modelParserDependentConfiguration.entrySet())
        {
            if(configuration.getKey().equals(typeBasedModelHandler.getId()))
            {
                typeBasedModelHandler.setConfiguration(configuration.getValue());
            }
        }
    }


    protected void mergeMultipleMetaModels(
        Map<TypeBasedModelParser, List<MetaModel>> modelParserList)
    {
        Set<Entry<TypeBasedModelParser, List<MetaModel>>> entrySet = modelParserList.entrySet();
        for (Entry<TypeBasedModelParser, List<MetaModel>> groupedMetaModels : entrySet)
        {
            Collection<MetaModel> metaModelList = groupedMetaModels.getValue();
            Collection<MetaModel> obsoleteMetaModels = new ArrayList<MetaModel>();

            for (final MetaModel metaModel : metaModelList)
            {
                if (!obsoleteMetaModels.contains(metaModel))
                {
                    Collection<MetaModel> matchingMetaModels = findAllMatching(metaModelList, new Predicate<MetaModel>()
                    {
                        @Override
                        public boolean apply(MetaModel input)
                        {
                            boolean notNull = input != null && metaModel != null;
                            boolean notTheSameLocation = notNull && !input.getLocation().equals(metaModel.getLocation());
                            boolean hasEqualIdentifier = notNull && input.getId().equals(metaModel.getId());

                            return hasEqualIdentifier && notTheSameLocation;
                        }
                    });

                    if (!matchingMetaModels.isEmpty())
                    {
                        for (MetaModel matchingMetaModel : matchingMetaModels)
                        {
                            String metaModelLocation = "... " +
                                StringUtils.difference(metaModel.getLocation(), matchingMetaModel.getLocation());
                            String matchingMetaModelLocation = "... " +
                                StringUtils.difference(matchingMetaModel.getLocation(), metaModel.getLocation());

                            log.info(
                                "Found meta model with id '{}' and location '{}' merged with further meta model located at '{}'. ",
                                new Object[] { metaModel.getId(), metaModelLocation, matchingMetaModelLocation });

                            groupedMetaModels.getKey().mergeModel(metaModel, matchingMetaModel);
                        }
                    }

                    obsoleteMetaModels.addAll(matchingMetaModels);
                }
            }

            groupedMetaModels.getValue().removeAll(obsoleteMetaModels);
        }
    }


    private <T> Collection<T> findAllMatching(Collection<T> collection, Predicate<T> predicate)
    {
        LinkedList<T> matches = new LinkedList<T>();

        for (T item : collection)
        {
            if (predicate.apply(item))
            {
                matches.add(item);
            }
        }

        return matches;
    }


    private PropertyType findReferencedPropertyType(Map<TypeBasedModelParser, List<MetaModel>> modelParserList,
        String propertyRef, String modelId) throws MetaGenerationFailedException
    {
        PropertyMetaModel sourcePropertyTypeModel = null;

        search: for (List<MetaModel> metaModels : modelParserList.values())
        {
            for (MetaModel model : metaModels)
            {
                if (model instanceof DomainTypeMetaModel)
                {
                    sourcePropertyTypeModel = ((DomainTypeMetaModel) model).findPropertyModel(propertyRef);
                    if (sourcePropertyTypeModel != null)
                    {
                        break search;
                    }
                }
            }
            
        }

        if (sourcePropertyTypeModel == null)
        {
            log.warn("The model '" + propertyRef + "' referenced by model '" +
                modelId + "' cannot be found.");
            
            // Take the default property type then.
            return new PropertyType("String");
        }
        else
        {
            // We need to check recursively if we encounter another property reference.
            if (!StringUtils.isEmpty(sourcePropertyTypeModel.getPropertyRef()))
            {
                return findReferencedPropertyType(modelParserList, sourcePropertyTypeModel.getPropertyRef()
                    , modelId);
            }
            else
            {
                PropertyType modelType = sourcePropertyTypeModel.getType();
                if (modelType == null)
                {
                    throw new MetaGenerationFailedException("model '" + modelId + "' references the model '" +
                        sourcePropertyTypeModel.getId() + "' which has not type information set.");
                }
                else
                {
                    return modelType;
                }
            }
        }
    }


    private Collection<ModelCandidate> findModelCandidates(Set<TypeBasedModelParser> modelHandlers)
        throws MetaGenerationFailedException
    {
        List<String> modelDirectories = createDefaultModelDirectories();

        ModelFileSet fileSet = new ModelFileSet(modelBaseDirectory);
        fileSet.setIncludes(includedExtensions);
        fileSet.setExcludes(excludedExtensions);
        fileSet.setModelDirectories(modelDirectories);

        Collection<File> candidates = ModelCandidateResolver.findCandidates(fileSet);
        Collection<ModelCandidate> modelCandidates = new ArrayList<ModelCandidate>();

        for (File candidate : candidates)
        {
            try
            {
                String rootQName = DocumentRootQNameResolver.resolveRootElementName(candidate);

                log.debug("Found candidate for model with the root element name '{}' in '{}'.", rootQName,
                    candidate.getAbsolutePath());

                for (TypeBasedModelParser modelHandler : modelHandlers)
                {
                    if (modelHandler.supportsType(rootQName))
                    {
                        modelCandidates.add(new ModelCandidate(modelHandler, candidate));
                    }
                }
            }
            catch (DocumentRootQNameNotFoundException e)
            {
                throw new MetaGenerationFailedException("A possible candidate seems to be a not well-formed "
                    + "XML document. The root qualified name couldn't be resolved", e);
            }
        }

        return modelCandidates;
    }


    public void cleanupTargetDirectory(String targetDirectory)
    {
        String concreteDirectory = FilenameUtils.concat(targetDirectory, packageName.replace('.', File.separatorChar));
        File baseFolder = new File(concreteDirectory);
        
        if(baseFolder.exists())
        {
            log.info("Cleaning directory '{}' with recursive wild card mather *Domain.java, *MetaModel.java", baseFolder);
            
            IOFileFilter fileFilter = new WildcardFileFilter(Arrays.asList("*Domain.java", "*MetaModel.java"));
            Iterator<File> foundFiles = FileUtils.iterateFiles(baseFolder, fileFilter, TrueFileFilter.INSTANCE);
            
            while (foundFiles.hasNext())
            {
                File file = foundFiles.next();
                log.debug("Deleting the old meta model file with name '{}'.", file.getName());
                FileUtils.deleteQuietly(file);
            }
        }
    }


    protected List<String> createDefaultModelDirectories()
    {
        return Arrays.asList("models/domain", "models/domain/types", "models/domain/external", "models/domain/joined",
            "models/domain/constant-types", "models/processes", "models/subprocesses");
    }


    protected Map<TypeBasedModelParser, List<MetaModel>> getAvailableTypeBasedModelParser()
    {
        Map<TypeBasedModelParser, List<MetaModel>> modelParsers = new HashMap<TypeBasedModelParser, List<MetaModel>>();

        modelParsers.put(new DomainTypeModelHandler(Arrays.asList(excludedModels)), createNewSynchronizedList());
        modelParsers.put(new ExternalDomainTypeModelHandler(Arrays.asList(excludedModels)), createNewSynchronizedList());
        modelParsers.put(new RelationModelHandler(Arrays.asList(excludedModels)), createNewSynchronizedList());

        // Handler for the Test environment
        if(integrationTestSupport)
        {
            modelParsers.put(new TestDomainTypeModelHandler(Arrays.asList(excludedModels)), createNewSynchronizedList());
            modelParsers.put(new TestExternalDomainTypeModelHandler(Arrays.asList(excludedModels)), createNewSynchronizedList());
            modelParsers.put(new ProcessModelHandler(), new ArrayList<MetaModel>());
            modelParsers.put(new ViewModelHandler(), new ArrayList<MetaModel>());
        }

        return modelParsers;
    }


    protected List<MetaModel> createNewSynchronizedList()
    {
        return Collections.synchronizedList(new ArrayList<MetaModel>());
    }


    protected List<ModelContextDependentPostProcessor> getModelPostProcessors()
    {
        List<ModelContextDependentPostProcessor> modelDependentPostProcessors = new ArrayList<ModelContextDependentPostProcessor>();
        modelDependentPostProcessors.add(new DomainMetaModelPostProcessor(domainPrefix));
        
        if(integrationTestSupport)
        {
            modelDependentPostProcessors.add(new TestDomainMetaModelPostProcessor(domainPrefix));
            modelDependentPostProcessors.add(new ProcessMetaModelPostProcessor(domainPrefix));
        }
        
        return modelDependentPostProcessors;
    }


    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }


    public void setExcludedExtensions(String[] excludedExtensions)
    {
        this.excludedExtensions = excludedExtensions;
    }


    public void setExcludedDomainTypes(String[] excludedDomainTypes)
    {
        this.excludedModels = excludedDomainTypes;
    }


    public void setIncludedExtensions(String[] includedExtensions)
    {
        this.includedExtensions = includedExtensions;
    }
    
    public void setIntegrationTestSupport(boolean integrationTestSupport)
    {
        this.integrationTestSupport = integrationTestSupport;
    }
    
    public void setModelParserDependentConfiguration(Map<String, Map<String, Object>> modelParserDependentConfiguration)
    {
        this.modelParserDependentConfiguration = modelParserDependentConfiguration;
    }

    private static class IdBasedLexicographicalMetaModelComparator
        implements Comparator<MetaModel>
    {

        @Override
        public int compare(MetaModel info1, MetaModel info2)
        {
            return info1.getId().compareTo(info2.getId());
        }

    }
}
