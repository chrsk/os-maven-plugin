package org.opensaga.plugin.release;

import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

public class ReleaseConfiguration
    extends ReleaseRequest
{
    public ReleaseConfiguration(ReleaseRequest releaseRequest)
    {
        setIntegrationSupport(releaseRequest.isIntegrationSupport());
        setIntegrationServer(releaseRequest.getIntegrationServer());
        setIntegrationServerLogin(releaseRequest.getIntegrationServerLogin());
        setIntegrationServerPassword(releaseRequest.getIntegrationServerPassword());
        setIntegrationServerSuperUserPassword(releaseRequest.getIntegrationServerSuperUserPassword());
        setIntegrationServerTomcatName(releaseRequest.getIntegrationServerTomcatName());
        setIntegrationServerTomcatPath(releaseRequest.getIntegrationServerTomcatPath());
        
        setFtpSupport(releaseRequest.isFtpSupport());
        setFtpServer(releaseRequest.getFtpServer());
        setFtpServerLogin(releaseRequest.getFtpServerLogin());
        setFtpServerPassword(releaseRequest.getFtpServerPassword());
        setFtpServerPort(releaseRequest.getFtpServerPort());
        setFtpServerPath(releaseRequest.getFtpServerPath());
        
        setVersion(releaseRequest.getVersion());
        
        setProperties(releaseRequest.getProperties());
        setPromptableProperties(releaseRequest.getPromptableProperties());
    }


    public boolean isConfigured()
    {
        boolean isConfigured = true;

        if(isFtpSupport())
        {
            isConfigured &= StringUtils.isNotEmpty(getFtpServer());
            isConfigured &= StringUtils.isNotEmpty(getFtpServerLogin());
            isConfigured &= StringUtils.isNotEmpty(getFtpServerPort());
            isConfigured &= StringUtils.isNotEmpty(getFtpServerPath());
        }
        
        if(isIntegrationSupport())
        {
            isConfigured &= StringUtils.isNotEmpty(getIntegrationServer());
            isConfigured &= StringUtils.isNotEmpty(getIntegrationServerLogin());
            isConfigured &= StringUtils.isNotEmpty(getIntegrationServerTomcatName());
        }
        
        isConfigured &= StringUtils.isNotEmpty(getVersion());

        return isConfigured;
    }
    
    public String prettyPrint()
    {
        final StringBuilder output = new StringBuilder();
        
        if(isIntegrationSupport())
        {
            output.append("Integration server: ");
            output.append(ObjectUtils.defaultIfNull(getIntegrationServer(), ""));
            output.append("\n");
            
            output.append("Integration server login: ");
            output.append(ObjectUtils.defaultIfNull(getIntegrationServerLogin(), ""));
            output.append("\n");
            
            output.append("Integration server password: ");
            output.append(ObjectUtils.defaultIfNull(StringUtils.repeat("*", getIntegrationServerSuperUserPassword().length()), ""));
            output.append("\n");
            
            output.append("Integration server super user password: ");
            output.append(ObjectUtils.defaultIfNull(StringUtils.repeat("*", getIntegrationServerPassword().length()), ""));
            output.append("\n\n");
        }

        if(isFtpSupport())
        {
            output.append("FTP server URL: ");
            output.append(ObjectUtils.defaultIfNull(getFtpServer(), ""));
            output.append("\n");
    
            output.append("FTP server path: ");
            output.append(ObjectUtils.defaultIfNull(getFtpServerPath(), ""));
            output.append("\n");
            
            output.append("FTP server port: ");
            output.append(ObjectUtils.defaultIfNull(getFtpServerPort(), ""));
            output.append("\n");
    
            output.append("FTP server login: ");
            output.append(ObjectUtils.defaultIfNull(getFtpServerLogin(), ""));
            output.append("\n");
    
            output.append("FTP server password: ");
            output.append(ObjectUtils.defaultIfNull(StringUtils.repeat("*", getFtpServerPassword().length()), ""));
            output.append("\n\n");
        }

        output.append("Release version: ");
        output.append(ObjectUtils.defaultIfNull(getVersion(), ""));
        output.append("\n");
        
        if(!getPromptableProperties().isEmpty())
        {
            output.append("\nCustom promptable properties:\n");
            for (Map.Entry<String, String> promptableProperty : getPromptableProperties().entrySet())
            {
                output.append("Property '");
                output.append(promptableProperty.getKey());
                output.append("': ");
                output.append(promptableProperty.getValue());
            }
        }
        
        return output.toString();
    }
}
