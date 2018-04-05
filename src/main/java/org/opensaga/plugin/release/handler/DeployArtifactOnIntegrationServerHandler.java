package org.opensaga.plugin.release.handler;

import java.io.IOException;
import java.util.List;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.common.SecurityUtils;
import net.schmizz.sshj.connection.ConnectionException;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.xfer.FileSystemFile;

import org.apache.commons.lang.StringUtils;
import org.opensaga.plugin.release.ArtifactRequestContext;
import org.opensaga.plugin.release.ArtifactHandler;
import org.opensaga.plugin.release.ReleaseConfiguration;
import org.opensaga.plugin.release.facettes.DeployFacette;
import org.opensaga.plugin.release.facettes.Facette;
import org.opensaga.plugin.release.facettes.FacettedArtifact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeployArtifactOnIntegrationServerHandler
    implements ArtifactHandler
{

    private static final Logger log = LoggerFactory.getLogger(DeployArtifactOnIntegrationServerHandler.class);

    private IntegrationServerConfigurationStrategy serverConfigurationStrategy = new DefaultIntegrationServerConfigurationStrategy();


    @Override
    public void processArtifact(ArtifactRequestContext artifactRequestContext, ReleaseConfiguration releaseConfiguration,
        List<FacettedArtifact> facettedArtifacts)
    {
        if (!releaseConfiguration.isIntegrationSupport())
        {
            log.info("Integration server support is disabled, skipping publishing.");
            return;
        }

        log.info("Starting publishing artifact to integration server");
        log.info("Target Integration Server: {}@{}", new Object[] { releaseConfiguration.getIntegrationServerLogin(),
            releaseConfiguration.getIntegrationServer() });

        setupIntegrationServer(artifactRequestContext, releaseConfiguration, facettedArtifacts);

        log.info("Published artifact {} to integration server.", StringUtils.join(facettedArtifacts, ", "));
    }


    public void setupIntegrationServer(ArtifactRequestContext artifactRequestContext, ReleaseConfiguration releaseConfiguration,
        List<FacettedArtifact> facettedArtifacts)
    {

        SSHClient ssh = null;

        try
        {
            SecurityUtils.setRegisterBouncyCastle(false);

            ssh = new SSHClient();
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.connect(releaseConfiguration.getIntegrationServer());
            ssh.authPassword(releaseConfiguration.getIntegrationServerLogin(), releaseConfiguration.getIntegrationServerPassword().toCharArray());

            logServerStatusCommand(releaseConfiguration, ssh);
            stopServer(releaseConfiguration, ssh);
            deleteExistentArtifacts(releaseConfiguration, facettedArtifacts, ssh);
            uploadArtifact(releaseConfiguration, facettedArtifacts, ssh);
            startServer(releaseConfiguration, ssh);
        }
        catch (Exception e)
        {
            log.warn("Failed to setup the integration server.", e);
        }
        finally
        {
            IOUtils.closeQuietly(ssh);
        }
    }


    protected void startServer(ReleaseConfiguration releaseConfiguration, SSHClient ssh) throws ConnectionException,
        TransportException, IOException
    {
        final Command startCommand = ssh.startSession().exec(
            serverConfigurationStrategy.getServerStartCommand(releaseConfiguration));
        log.debug(IOUtils.readFully(startCommand.getInputStream()).toString());
    }


    protected void uploadArtifact(ReleaseConfiguration releaseConfiguration, List<FacettedArtifact> facettedArtifacts, SSHClient ssh)
        throws IOException
    {
        for (FacettedArtifact facettedArtifact : facettedArtifacts)
        {
            final FileSystemFile sourceFile = new FileSystemFile(facettedArtifact.getArtifactFile());
            
            if (!sourceFile.getFile().exists())
            {
                throw new IOException("The source file '" + facettedArtifact.getArtifactFile().getAbsolutePath() +
                    "' was not accessible or doesn't exists.");
            }
            
            ssh.newSCPFileTransfer().upload(sourceFile, serverConfigurationStrategy.getServerDeployPath(releaseConfiguration));
        }
    }


    protected void deleteExistentArtifacts(ReleaseConfiguration releaseConfiguration, List<FacettedArtifact> typedArtifacts, SSHClient ssh)
        throws ConnectionException, TransportException, IOException
    {
        for (FacettedArtifact facettedArtifact : typedArtifacts)
        {
            final String artifactName = facettedArtifact.getArtifactFile().getName();
            final String deleteCommand = serverConfigurationStrategy.getServerDeleteArtifactCommand(releaseConfiguration, artifactName);
            final Command deleteArtifactCommand = ssh.startSession().exec(deleteCommand);
            log.debug(IOUtils.readFully(deleteArtifactCommand.getInputStream()).toString());
        }
    }


    protected void stopServer(ReleaseConfiguration releaseConfiguration, SSHClient ssh) throws ConnectionException,
        TransportException, IOException
    {
        final Command stopCommand = ssh.startSession().exec(
            serverConfigurationStrategy.getServerStopCommand(releaseConfiguration));
        log.debug(IOUtils.readFully(stopCommand.getInputStream()).toString());
    }


    protected void logServerStatusCommand(ReleaseConfiguration releaseConfiguration, SSHClient ssh) throws ConnectionException,
        TransportException, IOException
    {
        final Command statusCommand = ssh.startSession().exec(
            serverConfigurationStrategy.getServerStatusCommand(releaseConfiguration));
        log.debug(IOUtils.readFully(statusCommand.getInputStream()).toString());
    }

    public void setServerConfigurationStrategy(IntegrationServerConfigurationStrategy serverConfigurationStrategy)
    {
        this.serverConfigurationStrategy = serverConfigurationStrategy;
    }


    @Override
    public Class<? extends Facette> getSupportedArtifactsByFacet()
    {
        return DeployFacette.class;
    }

}
