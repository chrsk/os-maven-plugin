package org.opensaga.plugin.builder.meta.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.opensaga.plugin.builder.meta.generator.model.MetaModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a implementation with the template engine Velocity.
 * 
 * @see JavaSourceGenerator
 * @author cklewes
 */
public class VelocityBasedJavaSourceGenerator
    implements JavaSourceGenerator
{

    private static final Logger log = LoggerFactory.getLogger(VelocityBasedJavaSourceGenerator.class);

    /**
     * The location of the velocity properties
     */
    private static final String VELOCITY_PROPERTIES = "velocity.properties";

    /**
     * The base directory where to search for candidates
     */
    private String baseDirectory;

    /**
     * The packagename for the generated java files
     */
    private final String packageName;


    /**
     * Creates a {@code JavaMetaModelGenerator} for the given parameters.
     * 
     * @param javaClassTemplateLocation The template location of the java files
     * @param baseDirectory The base directory where to perform the model search.
     * @param packageName The package name for the generated Java files
     */
    public VelocityBasedJavaSourceGenerator(String baseDirectory, String packageName)
    {
        this.baseDirectory = baseDirectory;
        this.packageName = packageName;
        
        InputStream configuration = this.getClass().getClassLoader().getResourceAsStream(VELOCITY_PROPERTIES);
        Velocity.init(getProperties(configuration));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void generateJavaSourceFile(String javaClassTemplateLocation, String fileName, String subPackage,
        Map<String, Object> specificContext, MetaModel... metaModel) throws MetaGenerationFailedException
    {

        String directory = FilenameUtils.concat(baseDirectory, packageName.replace('.', '/'));
        String concreteDirectory = FilenameUtils.concat(directory, subPackage.replace('.', '/'));

        // Startup velocity engine.

        File javaClassFile = new File(concreteDirectory, fileName);
        Writer writer = null;

        try
        {
            createNewFile(javaClassFile);

            VelocityContext velocityContext = createDefaultContext(subPackage, metaModel);
            mergeWithSpecificContext(specificContext, velocityContext);
            
            Template template = Velocity.getTemplate(javaClassTemplateLocation);

            writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(javaClassFile), "UTF-8"));
            template.merge(velocityContext, writer);
        }
        catch (ParseErrorException e)
        {
            throw new MetaGenerationFailedException("The template couldn't be parsed.", e);
        }
        catch (ResourceNotFoundException e)
        {
            throw new MetaGenerationFailedException("The template couldn't be found.", e);
        }
        catch (IOException e)
        {
            throw new MetaGenerationFailedException("The java class file for model '" + fileName +
                "' was not writeable.", e);
        }
        finally
        {
            IOUtils.closeQuietly(writer);
        }
    }


    private void mergeWithSpecificContext(Map<String, Object> specificContext, VelocityContext velocityContext)
    {
        if(specificContext != null)
        {
            for (Entry<String, Object> context : specificContext.entrySet())
            {
                velocityContext.put(context.getKey(), context.getValue());
            }
        }
    }


    /**
     * Create default velocity context
     * @param subPackage 
     * 
     * @param generatorInfo The generator info
     * @return The default velocity context
     */
    protected VelocityContext createDefaultContext(String subPackage, MetaModel ... metaModel)
    {
        VelocityContext context = new VelocityContext();
        
        final SimpleDateFormat ISO_8601_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String currentDate = ISO_8601_FORMATTER.format(new Date());
        
        context.put("date", currentDate);
        context.put("website", "http://www.opensaga.org");
        context.put("comment", "This class is generated by OpenSAGA.");
        context.put("model", metaModel);
        context.put("packageName", packageName + "." + subPackage);
        context.put("basePackageName", packageName);
        return context;
    }


    /**
     * Try to create a new file and return the result. Either {@code true} if
     * the creation was successful or {@code false} otherwise.
     * 
     * @param javaClassFile
     * @return Either {@code true} if the creation was successful or
     *         {@code false} otherwise.
     * @throws IOException
     */
    private boolean createNewFile(File javaClassFile) throws IOException
    {
        FileUtils.forceMkdir(new File(javaClassFile.getParent()));
        boolean createdNewFile = javaClassFile.createNewFile();

        if (createdNewFile)
        {
            log.debug("The java class '" + javaClassFile.getAbsolutePath() + "' is created.");
        }
        else
        {
            log.debug(
                "The java class '" + javaClassFile.getAbsolutePath() + "' already exists and will be overwritten");
        }
        return createdNewFile;
    }


    /**
     * Load properties from input stream
     * 
     * @param configuration The input stream which is used to load the
     *            properties.
     * @return The properties, either filled by the input stream or a empty
     *         property object.
     */
    private Properties getProperties(InputStream configuration)
    {
        Properties properties = new Properties();

        try
        {
            properties.load(configuration);
        }
        catch (IOException e)
        {
            log.error(
                "Error while loading properties from logging configuration '" + configuration + "'.", e);
        }

        return properties;
    }
}
