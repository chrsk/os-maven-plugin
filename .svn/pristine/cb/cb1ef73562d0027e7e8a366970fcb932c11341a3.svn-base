package org.opensaga.plugin.release;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

@Component(role = ReleaseConfigurationQueryer.class)
public class DefaultReleaseConfigurationQueryer
    implements ReleaseConfigurationQueryer
{

    @Requirement(hint = "release")
    private Prompter prompter;


    @Override
    public boolean confirmConfiguration(ReleaseConfiguration configuration)
        throws PrompterException
    {
        StringBuilder query = new StringBuilder();

        query.append("\n\nPlease confirm the following configurations\n");
        query.append(configuration.prettyPrint());
        query.append("\n\n Are all values correct?");

        String answer = prompter.prompt(query.toString(), "Y");
        return "Y".equalsIgnoreCase(answer);
    }


    @Override
    public String getIntegrationServer(String integrationServerUrl) throws PrompterException
    {
        return prompter.prompt("What is the URL of the integration server?", integrationServerUrl);
    }


    @Override
    public String getIntegrationServerLogin(String integrationServerLogin) throws PrompterException
    {
        return prompter.prompt("What is the login to the integration server?",
            integrationServerLogin);
    }


    @Override
    public String getIntegrationServerPassword(String integrationServerPassword)
        throws PrompterException
    {
        return prompter.promptForPassword("Please specify the password of the integration server");
    }


    @Override
    public String getIntegrationServerSuperUserPassword(String integrationServerPassword)
        throws PrompterException
    {
        return prompter
            .promptForPassword("Please specify the super user password of the integration server");
    }


    @Override
    public String getVersion(String version) throws PrompterException
    {
        return prompter.prompt("What is the version you want to deploy?", version);
    }


    @Override
    public String getProperty(String property, String defaultValue) throws PrompterException
    {
        return prompter.prompt("Please specifiy the value for the property '" + property + "'",
            defaultValue);
    }


    @Override
    public String getFtpServer(String ftpServer) throws PrompterException
    {
        return prompter.prompt("What is the URL of the FTP server?", ftpServer);
    }


    @Override
    public String getFtpServerLogin(String ftpServerLogin) throws PrompterException
    {
        return prompter.prompt("What is the login to the FTP server?", ftpServerLogin);
    }


    @Override
    public String getFtpServerPassword(String ftpServerPassword) throws PrompterException
    {
        return prompter.promptForPassword("Please specify the password of the FTP server");
    }


    @Override
    public String getFtpServerPort(String ftpServerPort) throws PrompterException
    {
        return prompter.prompt("What is the port for the FTP server?", ftpServerPort);
    }


    @Override
    public String getFtpServerPath(String ftpServerPath) throws PrompterException
    {
        return prompter.prompt("In which directory on the FTP shall the artifacts be copied?",
            ftpServerPath);
    }

}
