package org.opensaga.plugin.builder.meta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.opensaga.plugin.util.MavenLogAppender;
import org.opensaga.plugin.builder.meta.parser.HandlerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates java source files which contains meta informations about the domain
 * types defined in the project. The generated source files enables the use of
 * rapid error feedback at compile time, while programming in Java with
 * OpenSAGA.
 * <p>
 * This Mojo builds meta models from a given model base directory. It's possible
 * to configure the target directory where the generated files should be stored.
 * The {@code MetaModelBuilderMojo} picks up all extensions, if they're not
 * constrained by {@link #includedExtensions} or {@link #excludedExtensions}.
 * <p>
 * All generated domain type meta models are also generated in a global domain
 * class, which enables access to all meta informations about the domain types.
 * To change the domain name please see {@link #domainPrefix}.
 * 
 * @author cklewes
 * @goal meta
 * @requiresDependencyResolution compile+runtime
 * @requiresProject true
 * @phase validate
 */
public class MetaModelClassBuilderMojo
    extends AbstractMojo
{

    private static final Logger log = LoggerFactory.getLogger(MetaModelClassBuilderMojo.class);
    
    /**
     * Defines the target directory for the generated classes, the classes are
     * stored into this directory with the additional package name.
     * 
     * @see #packageName
     * @parameter default-value="${project.build.sourceDirectory}"
     */
    private String targetDirectory;
    

    /**
     * Defines the target directory for the generated classes, the classes are
     * stored into this directory with the additional package name.
     * 
     * @see #packageName
     * @parameter default-value="${basedir}/src/test/java"
     */
    private String targetTestDirectory;

    /**
     * Provides the model base directory where the domain-type models can be
     * found. If you want to use the {@coed MetaModelBuilderMojo} in combination
     * with the extension exclude/include, please make sure you provide the
     * extension parent folder.
     * 
     * @parameter default-value="${basedir}/src/main/webapp"
     */
    private String modelBaseDirectory;

    /**
     * A list of all included extensions. If set only extensions which are
     * defined will be searched, nothing else.
     * 
     * @parameter
     */
    private final String[] includedExtensions = new String[0];

    /**
     * A list of all excluded extensions. If set only extensions which are not
     * excluded will be searched, nothing else.
     * 
     * @parameter
     */
    private final String[] excludedExtensions = new String[0];

    /**
     * A list of all excluded domain types. By default all domain types are
     * included, respectively to {@link #excludedExtensions} and
     * {@link #includedExtensions}
     * 
     * @parameter
     */
    private final String[] excludedModels = new String[0];

    /**
     * The package name for the generated classes. This should be adjusted for
     * the specific project to your default package.
     * 
     * @parameter default-value="org.opensaga.runtime.model.domain.types.meta"
     */
    private String packageName;

    /**
     * The domain prefix for the domain class which provides access to all
     * domains.
     * 
     * @parameter default-value="Project"
     */
    private String domainPrefix;
    
    /**
     * Generate the integration test support to the {@code src/test/java} folder.
     * 
     *  @parameter default-value=false
     */
    private boolean integrationTestSupport = false;
    
    /**
     * Generated view classes should extend the given qualified class 
     * 
     *  @parameter 
     */
    private String extendViewClass;
    
    /**
     * The Maven project.
     * 
     * @parameter default-value="${project}"
     * @required
     * @readonly
     */
    MavenProject project;
   
    
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        MavenLogAppender.startPluginLog(this);

        JavaBasedMetaModelClassBuilder builder = new JavaBasedMetaModelClassBuilder(targetDirectory, targetTestDirectory, modelBaseDirectory, domainPrefix);
        
        builder.setExcludedDomainTypes(excludedModels);
        builder.setExcludedExtensions(excludedExtensions);
        builder.setIncludedExtensions(includedExtensions);
        builder.setPackageName(packageName);
        builder.setIntegrationTestSupport(integrationTestSupport);
        
        configureMetaModelBuilder(builder);
        
        log.info("Hint: Enable integration test support in the POM configuration to generate test classes");
        
        try
        {
            assertDependencyIsDeclaredIfRequired("org.opensaga", "opensaga-selenium", integrationTestSupport);
            builder.generateMetaModels();
        }
        catch (Exception e)
        {
            throw new MojoFailureException("The generation of the meta models failed due to an internal error.", e);
        }
    }

    protected void configureMetaModelBuilder(JavaBasedMetaModelClassBuilder builder)
    {
        if(StringUtils.isNotEmpty(extendViewClass))
        {
            final HashMap<String, Object> viewModelHandlerConfiguration = new HashMap<String, Object>();
            viewModelHandlerConfiguration.put(HandlerConstants.CONFIG_KEY_EXTENDS_CLASS, extendViewClass);

            final HashMap<String, Map<String, Object>> configuration = new HashMap<String, Map<String,Object>>();
            configuration.put(HandlerConstants.VIEW_MODEL_HANDLER, viewModelHandlerConfiguration);
            
            builder.setModelParserDependentConfiguration(configuration);
        }
    }
    
    protected void assertDependencyIsDeclaredIfRequired(String groupId, String artifactId, boolean required) throws MojoExecutionException
    {
        if(!required)
        {
            return;
        }
        
        boolean dependencyWasFound = false;

        final List<Dependency> dependencies = project.getDependencies();
        for (Dependency dependency : dependencies)
        {
            final String dependencyGroupId = dependency.getGroupId();
            final String dependencyArtifactId = dependency.getArtifactId();

            if (dependencyGroupId.equals(groupId) && dependencyArtifactId.equals(artifactId))
            {
                log.info("The required dependency '{}:{}:{}', was found.", new Object[] { groupId, artifactId, dependency.getVersion() });
                dependencyWasFound = true;
            }
        }
        
        if(!dependencyWasFound)
        {
            throw new MissingDependencyException(groupId, artifactId);
        }
    }
    
}
