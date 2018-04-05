package org.opensaga.plugin.builder.meta.generator;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.opensaga.plugin.builder.meta.generator.model.ViewComponentMetaModel;
import org.opensaga.plugin.builder.meta.parser.ModelNamingStrategy;
import org.opensaga.plugin.builder.meta.parser.types.view.QueryConvenientMethodTemplates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides an implementation with the template engine Velocity. This class
 * generates a method in the context of the Java meta models. The generated
 * method will be returned and is capable of an easy integration in other
 * template files. This class is mainly used to call from another templating
 * function.
 * 
 * @see JavaSourceGenerator
 * @author cklewes
 */
public class VelocityBasedMethodGenerator
{

    private static final Logger log = LoggerFactory.getLogger(VelocityBasedMethodGenerator.class);

    /**
     * The location of the velocity properties
     */
    private static final String VELOCITY_PROPERTIES = "velocity.properties";

    private static final String BASE_METHOD_FOLDER = "templates/java-snippets/";

    static
    {
        // Startup velocity engine.
        InputStream configuration = VelocityBasedMethodGenerator.class.getClassLoader().getResourceAsStream(VELOCITY_PROPERTIES);
        Velocity.init(getProperties(configuration));
    }


    /**
     * {@inheritDoc}
     */
    public String generateMethod(QueryConvenientMethodTemplates method,
        ModelNamingStrategy namingStrategy, ViewComponentMetaModel viewComponentMetaModel)
        throws MetaGenerationFailedException
    {
        StringWriter writer = null;

        try
        {
            VelocityContext velocityContext = new VelocityContext();
            velocityContext.put("model", viewComponentMetaModel);
            velocityContext.put("namingStrategy", namingStrategy);
            velocityContext.put("stringUtils", new StringUtils());

            Template template = Velocity.getTemplate(BASE_METHOD_FOLDER + method.getTemplate());
            writer = new StringWriter();

            template.merge(velocityContext, writer);
            return writer.toString();
        }
        catch (ParseErrorException e)
        {
            throw new MetaGenerationFailedException("The template couldn't be parsed.", e);
        }
        catch (ResourceNotFoundException e)
        {
            throw new MetaGenerationFailedException("The template couldn't be found.", e);
        }
        finally
        {
            IOUtils.closeQuietly(writer);
        }
    }


    /**
     * Load properties from input stream
     * 
     * @param configuration The input stream which is used to load the
     *            properties.
     * @return The properties, either filled by the input stream or a empty
     *         property object.
     */
    private static Properties getProperties(InputStream configuration)
    {
        Properties properties = new Properties();

        try
        {
            properties.load(configuration);
        }
        catch (IOException e)
        {
            log.error("Error while loading properties from logging configuration '" +
                configuration + "'.", e);
        }

        return properties;
    }
}
