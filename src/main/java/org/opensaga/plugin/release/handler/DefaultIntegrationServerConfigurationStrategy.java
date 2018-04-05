package org.opensaga.plugin.release.handler;

import java.text.MessageFormat;

import org.opensaga.plugin.release.ReleaseConfiguration;

public class DefaultIntegrationServerConfigurationStrategy
    implements IntegrationServerConfigurationStrategy
{

    @Override
    public String getServerStatusCommand(ReleaseConfiguration releaseConfiguration)
    {
        final String superUserPassword = releaseConfiguration.getIntegrationServerSuperUserPassword();
        final String tomcatName = releaseConfiguration.getIntegrationServerTomcatName();
        final String command = MessageFormat.format("echo {0} | sudo -S /etc/init.d/{1} status", superUserPassword, tomcatName);
        
        return command;
    }

    @Override
    public String getServerStopCommand(ReleaseConfiguration releaseConfiguration)
    {
        final String superUserPassword = releaseConfiguration.getIntegrationServerSuperUserPassword();
        final String tomcatName = releaseConfiguration.getIntegrationServerTomcatName();
        final String command = MessageFormat.format("echo {0} | sudo -S /etc/init.d/{1} stop", superUserPassword, tomcatName);
        
        return command;
    }

    @Override
    public String getServerDeleteArtifactCommand(ReleaseConfiguration releaseConfiguration, String artifactName)
    {
        final String superUserPassword = releaseConfiguration.getIntegrationServerSuperUserPassword();
        final String tomcatName = releaseConfiguration.getIntegrationServerTomcatName();
        final String tomcatPath = releaseConfiguration.getIntegrationServerTomcatPath();

        final String artifactNameFolder = artifactName.substring(0, artifactName.indexOf(".war"));
        final String deleteFolderCommand = MessageFormat.format("rm -rf /{0}/{1}/webapps/{2}", tomcatPath, tomcatName, artifactNameFolder);
        final String deleteFileCommand = MessageFormat.format("rm -rf /{0}/{1}/webapps/{2}", tomcatPath, tomcatName, artifactName);

        final String command = MessageFormat.format("echo {0} | tee >(sudo -S {1}) >(sudo -S {2})", superUserPassword, deleteFolderCommand, deleteFileCommand);
        return command;
    }

    @Override
    public String getServerDeployPath(ReleaseConfiguration releaseConfiguration)
    {
        return MessageFormat.format("/{0}/{1}/webapps", releaseConfiguration.getIntegrationServerTomcatPath(), releaseConfiguration.getIntegrationServerTomcatName());
    }

    @Override
    public String getServerStartCommand(ReleaseConfiguration releaseConfiguration)
    {
        final String superUserPassword = releaseConfiguration.getIntegrationServerSuperUserPassword();
        final String tomcatName = releaseConfiguration.getIntegrationServerTomcatName();
        final String command = MessageFormat.format("echo {0} | sudo -S /etc/init.d/{1} start", superUserPassword, tomcatName);

        return command;
    }

}
