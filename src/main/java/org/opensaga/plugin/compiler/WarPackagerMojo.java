package org.opensaga.plugin.compiler;

import java.io.File;

import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.war.WarMojo;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.war.WarArchiver;
import org.opensaga.plugin.util.MavenLogAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Packages a folder as a WAR file. This is an workaround for a Maven
 * misconception. There exists an improvement requirement, as soon as this is
 * fulfilled the class is obsolete.
 * <p>
 * Currently it's not possible to use the default {@link WarMojo}. The
 * {@code WarMojo} rebuilds the exploded war and may destroy the generated
 * artifacts.
 * <p>
 * For further informations and current process please see <a
 * href="http://jira.codehaus.org/browse/MWAR-86">MWAR-86</a> in Maven's JIRA.
 * 
 * @goal war
 * @phase package
 * @threadSafe
 * @requiresDependencyResolution runtime
 */
public class WarPackagerMojo
    extends AbstractMojo
{

    private static final Logger log = LoggerFactory.getLogger(WarPackagerMojo.class);

    /**
     * The directory for the generated WAR.
     * 
     * @parameter default-value="${project.build.directory}"
     * @required
     */
    private String outputDirectory;

    /**
     * The Maven project.
     * 
     * @parameter default-value="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * The directory where the webapp is built.
     * 
     * @parameter 
     *            default-value="${project.build.directory}/${project.build.finalName}"
     * @required
     */
    private File webappDirectory;

    /**
     * The name of the generated WAR.
     * 
     * @parameter default-value="${project.build.finalName}"
     * @required
     */
    private String warName;

    /**
     * Classifier to add to the generated WAR. If given, the artifact will be an
     * attachment instead. The classifier will not be applied to the JAR file of
     * the project - only to the WAR file.
     * 
     * @parameter
     */
    private String classifier;

    /**
     * The WAR archiver.
     * 
     * @component role="org.codehaus.plexus.archiver.Archiver" roleHint="war"
     */
    private WarArchiver warArchiver;

    /**
     * Archive configuration of the maven archiver
     */
    private MavenArchiveConfiguration archive = new MavenArchiveConfiguration();


    /**
     * Packages the exploded war into a WAR file.
     * 
     * @throws MojoExecutionException
     */
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        MavenLogAppender.startPluginLog(this);

        try
        {
            final File webXmlFile = new File(webappDirectory, "WEB-INF/web.xml");
            warArchiver.addDirectory(webappDirectory, new String[] { "**" }, new String[0]);
            warArchiver.setWebxml(webXmlFile);

            /* add manifest, needed for OpenSAGA-Schema-Version */
            File manifestFile = new File(webappDirectory, "META-INF/MANIFEST.MF");
            if (manifestFile.exists())
            {
                warArchiver.setManifest(manifestFile);
            }

            MavenArchiver archiver = new MavenArchiver();
            archiver.setArchiver(warArchiver);
            archiver.setOutputFile(getTargetWarFile());

            log.info("Start packaging webapp");
            archiver.createArchive(project, archive);
            log.info("Finished packaging webapp");
        }
        catch (Exception e)
        {
            throw new MojoExecutionException("Error assembling WAR: " + e.getMessage(), e);
        }
    }


    /**
     * Creates a target file name from the given parameters.
     * 
     * @param basedir The basedir of the project
     * @param finalName The final name of the project
     * @param classifier The classifier of the project
     * @param type The project type
     * @return The generated filename as a file.
     */
    protected static File getTargetFile(File basedir, String finalName, String classifier, String type)
    {
        if (classifier == null)
        {
            classifier = "";
        }
        else if (classifier.trim().length() > 0 && !classifier.startsWith("-"))
        {
            classifier = "-" + classifier;
        }

        return new File(basedir, finalName + classifier + "." + type);
    }


    /**
     * Creates the target war file
     * 
     * @return A new target war file
     */
    protected File getTargetWarFile()
    {
        return getTargetFile(new File(outputDirectory), warName, classifier, "war");
    }
}
