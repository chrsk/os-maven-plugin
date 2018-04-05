package org.opensaga.plugin.release.handler;

import java.io.FileInputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.opensaga.plugin.release.ArtifactHandler;
import org.opensaga.plugin.release.ArtifactRequestContext;
import org.opensaga.plugin.release.ReleaseConfiguration;
import org.opensaga.plugin.release.facettes.Facette;
import org.opensaga.plugin.release.facettes.FacettedArtifact;
import org.opensaga.plugin.release.facettes.UploadFacette;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UploadArtifactToFtpHandler
    implements ArtifactHandler
{

    private static final Logger log = LoggerFactory.getLogger(UploadArtifactToFtpHandler.class);

    
    @Override
    public void processArtifact(ArtifactRequestContext artifactRequestContext,
        ReleaseConfiguration releaseConfiguration, List<FacettedArtifact> facettedArtifacts)
    {
        if(!releaseConfiguration.isFtpSupport())
        {
            log.info("FTP support is disabled, skipping upload");
            return;
        }
        
        log.info("Starting publishing artifact to FTP server");
        log.info("Target FTP Server: {}@{}:{}", 
            new Object[] { releaseConfiguration.getFtpServerLogin(),
                           releaseConfiguration.getFtpServer(), 
                           releaseConfiguration.getFtpServerPort() });
        
        publishArtifactToFtpServer(artifactRequestContext, releaseConfiguration, facettedArtifacts);
        log.info("Published artifact {} to FTP server.", StringUtils.join(facettedArtifacts, ", "));
    }
    
    protected void publishArtifactToFtpServer(ArtifactRequestContext artifactRequestContext,
        ReleaseConfiguration releaseConfiguration, List<FacettedArtifact> facettedArtifacts)
    {
        FileInputStream fileInputStream = null;
        FTPClient client = null;
        
        try
        {
            client = new FTPClient();
            client.connect(releaseConfiguration.getFtpServer());
            client.login(releaseConfiguration.getFtpServerLogin(), releaseConfiguration.getFtpServerPassword());
            
            client.changeWorkingDirectory(releaseConfiguration.getFtpServerPath());
            client.makeDirectory(releaseConfiguration.getVersion());
            client.changeWorkingDirectory(releaseConfiguration.getVersion());
            
            for (FacettedArtifact facettedArtifact : facettedArtifacts)
            {
                fileInputStream = new FileInputStream(facettedArtifact.getArtifactFile());
                
                client.setFileType(FTPClient.BINARY_FILE_TYPE);
                client.storeFile(facettedArtifact.getArtifactFile().getName(), fileInputStream);
            }
        }
        catch (Exception e)
        {
            log.warn("Uploading of the artifact failed.", e);
        }
        finally
        {
            closeQuietly(client);
            IOUtils.closeQuietly(fileInputStream);
        }
    }

    protected static void closeQuietly(FTPClient client)
    {
        try
        {
            client.logout();
            client.disconnect();
        }
        catch (Exception e)
        {
            log.warn("Uploading of the artifact failed.", e);
        }
    }

    @Override
    public Class<? extends Facette> getSupportedArtifactsByFacet()
    {
        return UploadFacette.class;
    }


}
