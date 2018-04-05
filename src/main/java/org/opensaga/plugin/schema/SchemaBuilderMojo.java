package org.opensaga.plugin.schema;

import java.util.List;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.opensaga.plugin.AbstractOpenSAGAAwareMojo;
import org.opensaga.plugin.util.MavenLogAppender;
import org.opensaga.runtime.util.spring.ContextLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builds the schema from the current available model sources. This is currently
 * only supported for opensaga-core.
 * 
 * @author cklewes
 * @goal schema
 * @requiresDependencyResolution compile+runtime
 * @requiresProject true
 * @phase package
 */
public class SchemaBuilderMojo
    extends AbstractOpenSAGAAwareMojo
{
    
    static final Logger log = LoggerFactory.getLogger(SchemaBuilderMojo.class);

    /**
     * The Maven project.
     * 
     * @parameter default-value="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * The location of the schema configuration file
     * 
     * @parameter default-value="${basedir}/schema-config.xml"
     */
    private String schemaConfig;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        try
        {
            MavenLogAppender.startPluginLog(this);
            
            List<String> runtimeClasspath = project.getRuntimeClasspathElements();
            extendPluginClasspath(runtimeClasspath);
            
            ContextLauncher.main(new String[] { schemaConfig, "schemaGenerator" });
        }
        catch (DependencyResolutionRequiredException e)
        {
            log.error("Failed to retrieve the dependencies", e);
        }
        catch (Exception e) {
            log.error("Failed to execute the schema goal", e);
        }
    }

}
