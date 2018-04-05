package org.opensaga.plugin;

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractOpenSAGAAwareMojo
    extends AbstractMojo
{

    private static final Logger log = LoggerFactory.getLogger(AbstractOpenSAGAAwareMojo.class);
    
    /**
     * Add the given classpath elements to the matching class realm.
     * 
     * @param elements The class path element to add to the plugin class realm.
     * @throws MojoExecutionException
     */
    protected void extendPluginClasspath(List<String> elements) throws MojoExecutionException
    {
        try
        {
            PluginDescriptor descriptor = (PluginDescriptor) getPluginContext().get("pluginDescriptor");
            ClassRealm realm = descriptor.getClassRealm();
    
            for (String element : elements)
            {
                File elementFile = new File(element);
                log.debug("Adding element to plugin classpath: " + elementFile.getPath());
                realm.addURL(elementFile.toURI().toURL());
            }
        }
        catch (Exception ex)
        {
            throw new MojoExecutionException("Failed to extend classpath", ex);
        }
    }

}
