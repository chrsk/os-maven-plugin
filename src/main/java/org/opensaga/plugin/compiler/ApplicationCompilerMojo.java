package org.opensaga.plugin.compiler;


import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.opensaga.plugin.compiler.cleanup.DeleteCandidateStrategy;
import org.opensaga.plugin.compiler.cleanup.OpenSAGADeleteCandidateStrategy;
import org.opensaga.plugin.compiler.cleanup.OpenSAGAResourceCleaner;
import org.opensaga.plugin.util.MavenLogAppender;
import org.opensaga.runtime.model.precompile.FileSystemBasedServletContextMock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Starts OpenSAGA with a mocked {@code ServletContext},
 * {@link FileSystemBasedServletContextMock}. This results into a serialized
 * model repository which is cached in a specific file.
 * <p>
 * The plugin doesn't need to be configured, all parameters are used by default.
 * 
 * @author cklewes
 * @goal precompile
 * @phase prepare-package
 * @requiresDependencyResolution runtime
 */
public class ApplicationCompilerMojo
    extends AbstractProjectClassRealmBasedMojo
{

    static final Logger log = LoggerFactory.getLogger(ApplicationCompilerMojo.class);

    /**
     * The base folder of the opensaga portal
     * 
     * @parameter 
     *            default-value="${project.build.directory}/${project.build.finalName}"
     */
    String baseDirectory;

    /**
     * The folder of the compiled classes
     * 
     * @parameter default-value="WEB-INF/classes"
     */
    String compiledClasses;

    /**
     * The extension to extend the default pre compiler behaviour
     * 
     * @parameter
     */
    String extendExtension;
    
    /**
     * Determines if the artifacts used to precompile should be cleaned up
     * afterward.
     * 
     * @parameter default-value="true"
     */
    private boolean cleanUpAfterCompile;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        MavenLogAppender.startPluginLog(this);
        initializePluginClassPath();

        log.info("Starting OpenSAGA Precompiler");

        new OpenSAGADependentExecutor(baseDirectory, extendExtension).execute();

        if (cleanUpAfterCompile)
        {
            DeleteCandidateStrategy removeStrategy = new OpenSAGADeleteCandidateStrategy();
            OpenSAGAResourceCleaner resourceCleaner = new OpenSAGAResourceCleaner(removeStrategy, baseDirectory);

            resourceCleaner.cleanUp();
        }
    }
    
    /**
     * Returns the classpath for the generated class files. This are the classes
     * which are generated by OpenSAGA.
     * 
     * @return A list of the generated classpath
     */
    @Override
    protected List<String> getCustomClasspathElements()
    {
        return Arrays.asList(new File(baseDirectory, compiledClasses).getAbsolutePath());
    }
}
