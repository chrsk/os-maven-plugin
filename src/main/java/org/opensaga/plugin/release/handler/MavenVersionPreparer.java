package org.opensaga.plugin.release.handler;

import java.io.File;
import java.io.FileWriter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.opensaga.plugin.release.ReleasePreparer;
import org.opensaga.plugin.release.ArtifactRequestContext;
import org.opensaga.plugin.release.ReleaseConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycila.xmltool.XMLDoc;
import com.mycila.xmltool.XMLTag;

public class MavenVersionPreparer
    implements ReleasePreparer
{

    private static final Logger log = LoggerFactory.getLogger(MavenVersionPreparer.class);


    @Override
    public void prepareArtifact(ArtifactRequestContext artifactRequestContext,
        ReleaseConfiguration releaseConfiguration)
    {
        FileWriter fileWriter = null;
        try
        {
            final File pom = artifactRequestContext.getMavenSession().getCurrentProject().getFile();
            final XMLTag versionTag = XMLDoc.from(pom, true).gotoFirstChild("version");
            final String previousVersion = versionTag.getText();
            final String futureVersion = releaseConfiguration.getVersion();
            
            if(!StringUtils.equals(previousVersion, futureVersion))
            {
                versionTag.setText(futureVersion);
                
                fileWriter = new FileWriter(pom);
                versionTag.gotoRoot().toStream(fileWriter);
                
                log.info("The previous POM version was '{}' changed version to '{}'", previousVersion, futureVersion);
            }
            else
            {
                log.info("The previous POM version was equal to the new version '{}'.", futureVersion);                
            }
            
        }
        catch (Exception e) {
            log.warn("Failed to change version.", e);
        }
        finally
        {
            IOUtils.closeQuietly(fileWriter);
        }
    }
}
