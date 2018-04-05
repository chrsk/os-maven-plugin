package org.opensaga.plugin.builder.meta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.opensaga.plugin.builder.meta.generator.JavaSourceGenerator;
import org.opensaga.plugin.builder.meta.generator.model.MetaModel;
import org.opensaga.plugin.builder.meta.parser.HandlerContext;
import org.opensaga.plugin.builder.meta.parser.ModelHandler;
import org.opensaga.plugin.builder.meta.parser.TypeBasedModelParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains the parts which are executed concurrently.
 * 
 * @author cklewes
 *
 */
public class ConcurrentModelBuilder
{
    private static final Logger log = LoggerFactory.getLogger(ConcurrentModelBuilder.class);

    private final Map<HandlerType, JavaSourceGenerator> generators;


    public ConcurrentModelBuilder(Map<HandlerType, JavaSourceGenerator> generators)
    {
        this.generators = generators;
    }


    /**
     * Executes the class generation concurrently to improve performance.
     * 
     * @param handlerContext The handler context of the parsers.
     * @param modelHandler The model handler whose meta models should be generated.
     * @param metaModelList The list of meta models which should be generated into java files.
     * 
     * @return The amount of generated java files.
     */
    public int executeClassGenerationConcurrently(final HandlerContext handlerContext,
        final ModelHandler<MetaModel> modelHandler, final List<MetaModel> metaModelList)
    {
        List<Callable<Integer>> callables = new ArrayList<Callable<Integer>>();
        Integer generatedFiles = 0;

        for (final MetaModel metaModel : metaModelList)
        {
            callables.add(new Callable<Integer>()
            {
                @Override
                public Integer call() throws Exception
                {
                    final JavaSourceGenerator generator = generators.get(modelHandler.getType());
                    return modelHandler.handleModel(handlerContext, metaModel, generator);
                }
            });
        }

        try
        {
            ExecutorService taskExecutor = Executors.newFixedThreadPool(4);
            List<Future<Integer>> futures = taskExecutor.invokeAll(callables);

            for (Future<Integer> future : futures)
            {
                final Integer generatedFilesInFuture = future.get();
                generatedFiles += generatedFilesInFuture;
            }
        }
        catch (IllegalStateException e)
        {
            log.warn("The concurrent exexcution exception failed", e);
        }
        catch (InterruptedException e)
        {
            log.warn("The concurrent exexcution exception failed", e);
        }
        catch (ExecutionException e)
        {
            log.warn("The execution of the future task failed.", e);
        }

        return generatedFiles;
    }


    public void handleModelCandidatesConcurrently(Map<TypeBasedModelParser, List<MetaModel>> modelParserList, List<Callable<List<MetaModel>>> callables)
    {
        try
        {
            ExecutorService taskExecutor = Executors.newFixedThreadPool(4);
            List<Future<List<MetaModel>>> futures = taskExecutor.invokeAll(callables);

            for (Future<List<MetaModel>> future : futures)
            {
                final List<MetaModel> parsedModelList = future.get();
                
                for (MetaModel metaModel : parsedModelList)
                {
                    modelParserList.get(metaModel.getParser()).add(metaModel);
                }
            }
        }
        catch (IllegalStateException e)
        {
            log.warn("The concurrent exexcution exception failed", e);
        }
        catch (InterruptedException e)
        {
            log.warn("The concurrent exexcution exception failed", e);
        }

        catch (ExecutionException e)
        {
            log.warn("The execution of the future task failed.", e);
        }
    }
}
