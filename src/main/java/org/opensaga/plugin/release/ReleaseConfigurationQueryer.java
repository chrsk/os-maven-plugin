package org.opensaga.plugin.release;

import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * Queries the user for further release configuration. The release configuration
 * queryer is capable of several features. Mainly it can ask the user for
 * specific input for a property. It's possible to use the either the typed
 * methods or the more generic {@link #getProperty(String, String)} for
 * promptable properties.
 * <p>
 * Further it provides a confirmation method where it's possible to let the user
 * verify the modified configuration.
 * 
 * @author cklewes
 */
public interface ReleaseConfigurationQueryer
{
    /**
     * Lets the user confirm the modified configuration.
     * 
     * @param configuration The configuration to confirm by the user
     * @return Either {@code true} when the user confirmed the configuration, or
     *         {@code false} otherwise.
     * @throws PrompterException
     */
    boolean confirmConfiguration(ReleaseConfiguration configuration) throws PrompterException;


    /**
     * Asks the user for the integration server.
     * 
     * @param defaultValue The default value which should be used when the user
     *            doesn't provide any input.
     * @return The requested value
     * @throws PrompterException
     */
    String getIntegrationServer(String defaultValue) throws PrompterException;


    /**
     * Asks the user for the integration server login.
     * 
     * @param defaultValue The default value which should be used when the user
     *            doesn't provide any input.
     * @return The requested value
     * @throws PrompterException
     */
    String getIntegrationServerLogin(String integrationServerLogin) throws PrompterException;


    /**
     * Asks the user for the integration server password.
     * 
     * @param defaultValue The default value which should be used when the user
     *            doesn't provide any input.
     * @return The requested value
     * @throws PrompterException
     */
    String getIntegrationServerPassword(String integrationServerPassword) throws PrompterException;


    /**
     * Asks the user for the integration super user password.
     * 
     * @param defaultValue The default value which should be used when the user
     *            doesn't provide any input.
     * @return The requested value
     * @throws PrompterException
     */
    String getIntegrationServerSuperUserPassword(String integrationServerSuperUserPassword)
        throws PrompterException;


    /**
     * Asks the user for the project version.
     * 
     * @param defaultValue The default value which should be used when the user
     *            doesn't provide any input.
     * @return The requested value
     * @throws PrompterException
     */
    String getVersion(String version) throws PrompterException;


    /**
     * Asks the user for the ftp server.
     * 
     * @param defaultValue The default value which should be used when the user
     *            doesn't provide any input.
     * @return The requested value
     * @throws PrompterException
     */
    String getFtpServer(String ftpServer) throws PrompterException;


    /**
     * Asks the user for the ftp server login.
     * 
     * @param defaultValue The default value which should be used when the user
     *            doesn't provide any input.
     * @return The requested value
     * @throws PrompterException
     */
    String getFtpServerLogin(String ftpServerLogin) throws PrompterException;


    /**
     * Asks the user for the ftp server password.
     * 
     * @param defaultValue The default value which should be used when the user
     *            doesn't provide any input.
     * @return The requested value
     * @throws PrompterException
     */
    String getFtpServerPassword(String ftpServerPassword) throws PrompterException;


    /**
     * Asks the user for the ftp server port.
     * 
     * @param defaultValue The default value which should be used when the user
     *            doesn't provide any input.
     * @return The requested value
     * @throws PrompterException
     */
    String getFtpServerPort(String ftpServerPort) throws PrompterException;


    /**
     * Asks the user for the ftp server path.
     * 
     * @param defaultValue The default value which should be used when the user
     *            doesn't provide any input.
     * @return The requested value
     * @throws PrompterException
     */
    String getFtpServerPath(String ftpServerPath) throws PrompterException;


    /**
     * Asks the user for the specific property value.
     * 
     * @param property The property name to ask for.
     * @param defaultValue The default value which should be used when the user
     *            doesn't provide any input.
     * @return The requested value
     * @throws PrompterException
     */
    String getProperty(String property, String defaultValue) throws PrompterException;
}
