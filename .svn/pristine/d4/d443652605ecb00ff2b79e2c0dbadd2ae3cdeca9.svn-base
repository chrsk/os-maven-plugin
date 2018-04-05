package org.opensaga.plugin.release;

import java.util.Map;
import java.util.Properties;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.components.interactivity.PrompterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(role = ReleaseConfigurator.class)
public class DefaultReleaseConfigurator
    implements ReleaseConfigurator
{
    private static final Logger log = LoggerFactory.getLogger(DefaultReleaseConfigurator.class);
    
    @Requirement
    private ReleaseConfigurationQueryer configurationQueryer;


    @Override
    public ReleaseConfiguration configureRelease(ReleaseRequest releaseRequest, Boolean interactiveMode, Properties userProperties)
        throws PrompterException
    {
        final ReleaseConfiguration releaseConfiguration = new ReleaseConfiguration(releaseRequest);

        if (interactiveMode)
        {
            log.info("The interactive mode is active.");
            retrieveConfigurationInteractive(releaseConfiguration);
        }
        else
        {
            log.info("Interactive mode is disabled, trying execution in batch mode");
        }

        return verifyReleaseConfigurationIsSufficient(releaseConfiguration);
    }


    protected void retrieveConfigurationInteractive(final ReleaseConfiguration releaseConfiguration)
        throws PrompterException
    {
        
        if(releaseConfiguration.isIntegrationSupport())
        {
            releaseConfiguration.setIntegrationServer(configurationQueryer.getIntegrationServer(releaseConfiguration.getIntegrationServer()));
            releaseConfiguration.setIntegrationServerLogin(configurationQueryer.getIntegrationServerLogin(releaseConfiguration.getIntegrationServerLogin()));
            releaseConfiguration.setIntegrationServerPassword(configurationQueryer.getIntegrationServerPassword(releaseConfiguration.getIntegrationServerPassword()));
            releaseConfiguration.setIntegrationServerSuperUserPassword(configurationQueryer.getIntegrationServerSuperUserPassword(releaseConfiguration.getIntegrationServerSuperUserPassword()));
        }

        if(releaseConfiguration.isFtpSupport())
        {
            releaseConfiguration.setFtpServer(configurationQueryer.getFtpServer(releaseConfiguration.getFtpServer()));
            releaseConfiguration.setFtpServerLogin(configurationQueryer.getFtpServerLogin(releaseConfiguration.getFtpServerLogin()));
            releaseConfiguration.setFtpServerPassword(configurationQueryer.getFtpServerPassword(releaseConfiguration.getFtpServerPassword()));
            releaseConfiguration.setFtpServerPort(configurationQueryer.getFtpServerPort(releaseConfiguration.getFtpServerPort()));
            releaseConfiguration.setFtpServerPath(configurationQueryer.getFtpServerPath(releaseConfiguration.getFtpServerPath()));
        }

        releaseConfiguration.setVersion(configurationQueryer.getVersion(releaseConfiguration.getVersion()));

        Map<String, String> promptableProperties = releaseConfiguration.getPromptableProperties();
        
        for (Map.Entry<String, String> promptableProperty : promptableProperties.entrySet())
        {
            String userInputProperty = configurationQueryer.getProperty(promptableProperty.getKey(), promptableProperty.getValue());
            promptableProperty.setValue(userInputProperty);
        }
        
        if(!configurationQueryer.confirmConfiguration(releaseConfiguration))
        {
            // The user said that the configuration is not correct, so we ask them all again
            retrieveConfigurationInteractive(releaseConfiguration);
        }
    }


    protected ReleaseConfiguration verifyReleaseConfigurationIsSufficient(final ReleaseConfiguration releaseConfiguration)
    {
        if (releaseConfiguration.isConfigured())
        {
            return releaseConfiguration;
        }

        throw new ReleaseConfigurationException("The release configuration was not sufficient configured.");
    }

}
