package org.opensaga.plugin.compiler;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.opensaga.runtime.model.precompile.FileSystemBasedServletContextMock;
import org.opensaga.startup.BootStrapListener;

/**
 * The OpenSAGA dependent executor wraps the execution into a separate class.
 * This is done due to class loading issues. The classes are loaded on runtime
 * after enhancing the plugin classpath. Therefore the implementation moved into
 * a separated class.
 * 
 * @author cklewes
 */
public class OpenSAGADependentExecutor
{
    /**
     * The extension identifier of the application builder extension
     */
    private static final String APPLICATION_BUILDER_EXTENSION = "os-pre-compiler";

    /**
     * The base folder of the opensaga portal.
     */
    private final String baseFolder;

    /**
     * Extends the default behaviour
     */
    private final String extendExtension;


    /**
     * Creates an OpenSAGA dependent executor which is based on the given base
     * folder. The OpenSAGA implementation will be started with this folder.
     * 
     * @param baseFolder The folder of the opensaga portal.
     * @param extendExtension Extends the default behaviour
     */
    public OpenSAGADependentExecutor(String baseFolder, String extendExtension)
    {
        this.baseFolder = baseFolder;
        this.extendExtension = extendExtension;
    }


    public void execute() throws MojoExecutionException
    {
        ServletContext context = new FileSystemBasedServletContextMock(baseFolder);
        ServletContextEvent event = new ServletContextEvent(context);

        // Appends resource to the opensaga system so the application builder
        // extension will be loaded and executed.
        
        String extensions = StringUtils.join(new Object[] { APPLICATION_BUILDER_EXTENSION, extendExtension }, ",");
        System.setProperty("OPENSAGA_RESOURCE_EXTENSIONS", extensions);
        System.setProperty("OPENSAGA_APPEND_RESOURCE_EXTENSIONS", "true");

        try
        {
            BootStrapListener bootstrapping = new BootStrapListener();
            bootstrapping.contextInitialized(event);
        }
        catch (Exception e)
        {
            throw new MojoExecutionException("Precompiling of the OpenSAGA project failed.", e);
        }
    }
}
