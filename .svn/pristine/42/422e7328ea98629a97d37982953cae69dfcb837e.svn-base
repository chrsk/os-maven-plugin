package org.opensaga.plugin.release;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.shared.invoker.Invoker;
import org.opensaga.plugin.compiler.AbstractProjectClassRealmBasedMojo;
import org.opensaga.plugin.release.facettes.Facette;
import org.opensaga.plugin.release.facettes.FacettedArtifact;
import org.opensaga.plugin.release.handler.MavenVersionPreparer;
import org.opensaga.plugin.release.handler.DeployArtifactOnIntegrationServerHandler;
import org.opensaga.plugin.release.handler.MavenBasedArtifactBuilder;
import org.opensaga.plugin.release.handler.UploadArtifactToFtpHandler;
import org.opensaga.plugin.util.MavenLogAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cklewes
 * @goal release
 * @requiresDependencyResolution runtime
 */
public class ReleaseMojo
    extends AbstractProjectClassRealmBasedMojo
{

    private static final Logger log = LoggerFactory.getLogger(ReleaseMojo.class);

    /** @component */
    private ReleaseConfigurator releaseConfigurator;

    /** @component */
    private Invoker invoker;
    
    /**
     * The version for the release
     * 
     * @parameter default-value="${project.version}" expression="${releaseVersion}"
     */
    private String releaseVersion;

    /**
     * The integration server support status
     * 
     * @parameter  expression="${integrationServerSupport}" default-value="true"
     */
    private boolean integrationServerSupport;
    
    /**
     * The integration server URL
     * 
     * @parameter  expression="${integrationServer}"
     */
    private String integrationServer;

    /**
     * The integration server login
     * 
     * @parameter  expression="${integrationServerLogin}"
     */
    private String integrationServerLogin;
  
    /**
     * The integration server Tomcat name
     * 
     * @parameter  expression="${integrationServerTomcatName}"
     */
    private String integrationServerTomcatName;
    
    /**
     * The integration server Tomcat path
     * 
     * @parameter  expression="${integrationServerTomcatPath}" default-value="opt"
     */
    private String integrationServerTomcatPath;

    /**
     * The FTP server support status
     * 
     * @parameter expression="${ftpServerSupport}" default-value="true"
     */
    private boolean ftpServerSupport;
    
    /**
     * The FTP server URL
     * 
     * @parameter expression="${ftpServer}"
     */
    private String ftpServer;
    
    /**
     * The FTP server login
     * 
     * @parameter expression="${ftpServerLogin}"
     */
    private String ftpServerLogin;
    
    /**
     * The FTP server port
     * 
     * @parameter expression="${ftpServerPort}" default-value="21"
     */
    private String ftpServerPort;
    
    /**
     * The FTP server path
     * 
     * @parameter expression="${ftpServerPath}"
     */
    private String ftpServerPath;

    /**
     * User settings use to check the interactiveMode.
     * 
     * @parameter expression="${interactiveMode}"
     *            default-value="${settings.interactiveMode}"
     * @required
     */
    private Boolean interactiveMode;

    /**
     * @parameter expression="${session}"
     * @readonly
     */
    private MavenSession session;

    /**
     * Additional goals to immediately run on the project
     * 
     * @parameter expression="${goals}" expression="${goals}"
     */
    private String goals;
    
    /**
     * @parameter
     */
    private String[] artifactBuilders = new String[] {};

    /**
     * @parameter
     */
    private String[] artifactHandlers = new String[] {};
     
    /**
     * @parameter
     */
    private String[] releasePreparer = new String[] {};
    
    /**
     * @parameter
     */
    private Map<String, String> properties = new HashMap<String, String>();
   
    /**
     * @parameter
     */
    private Map<String, String> promptableProperties = new HashMap<String, String>();

    private Map<String, Object> keywordMapping = new HashMap<String, Object>();


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        MavenLogAppender.startPluginLog(this);
        initializePluginClassPath();

        final Properties userProperties = session.getUserProperties();
        final ReleaseRequest releaseRequest = new ReleaseRequest()
            .setIntegrationSupport(integrationServerSupport)
            .setIntegrationServer(integrationServer)
            .setIntegrationServerLogin(integrationServerLogin)
            .setIntegrationServerTomcatName(integrationServerTomcatName)
            .setIntegrationServerTomcatPath(integrationServerTomcatPath)
            .setIntegrationServerPassword("")
            .setIntegrationServerSuperUserPassword("")
            .setGoals(goals)
            .setFtpSupport(ftpServerSupport)
            .setFtpServer(ftpServer)
            .setFtpServerLogin(ftpServerLogin)
            .setFtpServerPort(ftpServerPort)
            .setFtpServerPassword("")
            .setFtpServerPath(ftpServerPath)
            .setVersion(releaseVersion)
            .setPromptableProperties(promptableProperties)
            .setProperties(properties);

        keywordMapping.put("UPLOAD_TO_FTP_ARTIFACT_HANDLER", new UploadArtifactToFtpHandler());
        keywordMapping.put("DEPLOY_TO_INTEGRATION_SERVER_ARTIFACT_HANDLER", new DeployArtifactOnIntegrationServerHandler());
        keywordMapping.put("MAVEN_VERSION_PREPARER", new MavenVersionPreparer());
        keywordMapping.put("MAVEN_BASED_ARTIFACT_BUILDER", new MavenBasedArtifactBuilder());
        
        try
        {
            final ReleaseConfiguration releaseConfiguration = releaseConfigurator
                .configureRelease(releaseRequest, interactiveMode, userProperties);

            invokeReleasePreprocessors(releaseRequest, releaseConfiguration);
            final List<FacettedArtifact> synthesizedArtifacts = invokeArtifactHandlers(releaseRequest, releaseConfiguration);
            invokeSubsequentArtifactProcessors(releaseRequest, releaseConfiguration, synthesizedArtifacts);
        }
        catch (Exception e)
        {
            log.warn("Failed to release the artifact", e);
        }

    }


    private void invokeReleasePreprocessors(ReleaseRequest releaseRequest,
        ReleaseConfiguration releaseConfiguration)
    {
        final ArtifactRequestContext artifactContext = new DefaultArtifactRequestContext()
        .setInvoker(invoker)
        .setMavenSession(session)
        .setReleaseRequest(releaseRequest);
        
        for (ReleasePreparer processor : resolveProcessorList(
            ReleasePreparer.class, releasePreparer, new MavenVersionPreparer()))
        {
            try
            {
                processor.prepareArtifact(artifactContext, releaseConfiguration);
            }
            catch (Exception e)
            {
                log.warn("Failed to execute the pre processor '" + processor.getClass().getName() +
                    "'. See error log for details. ", e);
            }
        }

    }
    
    
    private List<FacettedArtifact> invokeArtifactHandlers(ReleaseRequest releaseRequest, ReleaseConfiguration releaseConfiguration)
        throws ArtifactCreationException
    {
        final ArtifactRequestContext artifactContext = new DefaultArtifactRequestContext()
            .setInvoker(invoker)
            .setMavenSession(session)
            .setReleaseRequest(releaseRequest);

        List<FacettedArtifact> facettedArtifacts = new ArrayList<FacettedArtifact>();
        
        for(ArtifactBuilder artifactBuilder : resolveProcessorList(
            ArtifactBuilder.class, artifactBuilders,
            new MavenBasedArtifactBuilder()))
        {
            facettedArtifacts.addAll(artifactBuilder.synthesizeArtifacts(artifactContext, releaseConfiguration));
        }
        
        return facettedArtifacts;
    }


    private void invokeSubsequentArtifactProcessors(ReleaseRequest releaseRequest, ReleaseConfiguration releaseConfiguration,
        List<FacettedArtifact> artifacts)
    {
        final ArtifactRequestContext artifactContext = new DefaultArtifactRequestContext()
        .setInvoker(invoker)
        .setMavenSession(session)
        .setReleaseRequest(releaseRequest);
        
        for (ArtifactHandler artifactHandler : resolveProcessorList(
            ArtifactHandler.class, artifactHandlers,
            
            new UploadArtifactToFtpHandler(),
            new DeployArtifactOnIntegrationServerHandler()))
        {
            try
            {
                artifactHandler.processArtifact(artifactContext, releaseConfiguration, removeUnsupportedArtifacts(artifacts, artifactHandler));
            }
            catch (Exception e)
            {
                log.warn("Failed to execute the post processor '" + artifactHandler.getClass().getName() +
                    "'. See error log for details. ", e);
            }
        }
    }


    protected List<FacettedArtifact> removeUnsupportedArtifacts(List<FacettedArtifact> artifacts, ArtifactHandler artifactHandler)
    {
        final List<FacettedArtifact> supportedArtifacts = new ArrayList<FacettedArtifact>();
        
        for (FacettedArtifact facettedArtifact : artifacts)
        {
            final List<Facette> facettes = facettedArtifact.getFacettes();
            
            for (Facette facette : facettes)
            {
                if (artifactHandler.getSupportedArtifactsByFacet().equals(facette.getClass()))
                {
                    supportedArtifacts.add(facettedArtifact);
                    break;
                }
            }
        }
        return supportedArtifacts;
    }  
    

    @SuppressWarnings("unchecked")
    private <I> List<I> resolveProcessorList(Class<I> implementingInterface,
        String[] artifactProcessors, I... defaultProcessors)
    {
        List<I> defaultProcessorsList = Arrays.asList(defaultProcessors);
        List<I> processors = new ArrayList<I>();
        
        for (String className : artifactProcessors)
        {
            if(keywordMapping.containsKey(className))
            {
                processors.add((I) keywordMapping.get(className));
            }
            
            processors.add(MavenClassLoadingUtil.instanceClassImplementingInterface(className, implementingInterface));
        }
        
        if(processors.isEmpty())
        {
            // Add the default processors only when nothing else is configured.
            processors.addAll(defaultProcessorsList);
        }
        
        return processors;
    }


    @Override
    protected List<String> getCustomClasspathElements()
    {
        return Collections.emptyList();
    }

}
