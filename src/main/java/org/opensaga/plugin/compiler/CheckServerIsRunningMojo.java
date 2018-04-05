package org.opensaga.plugin.compiler;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * Verifies that the tomcat is accessible and running otherwise produces an critical error.
 * 
 * @author cklewes
 * @goal server-startup-check
 * @phase pre-integration-test
 * @requiresDependencyResolution runtime
 */
public class CheckServerIsRunningMojo
    extends AbstractMojo
{
    
    private static final Logger log = LoggerFactory.getLogger(CheckServerIsRunningMojo.class);

    /**
     * @parameter expression="${opensaga.port}"  default-value="8080"
     */
    private String port;

    /**
     * @parameter expression="${opensaga.host}"  default-value="localhost"
     */
    private String server;

    /**
     * @parameter expression="${opensaga.contextPath}" default-value="${project.build.finalName}"
     */
    private String contextPath;
    
    /**
     * @parameter expression="${opensaga.disableServerCheck}" default-value="false"
     */
    private boolean disableServerCheck;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        final String uri = "http://{0}:{1}/{2}";
        final String qualifiedUrl = MessageFormat.format(uri, server, port, contextPath);
        
        try
        {
            URL url = new URL(qualifiedUrl);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setDoOutput(true);
            huc.setRequestMethod("GET");
            huc.connect();
            int code = huc.getResponseCode();

            if (200 == code)
            {
                log.info("The server respond for the given URL '{}' was HTTP 200 OK.", code);
                return;
            }

            propagateError(qualifiedUrl, code, null);
        }
        catch (Exception e)
        {
            propagateError(qualifiedUrl, null, e);
        }

    }


    protected void propagateError(final String qualifiedUrl, Integer code, Exception e) throws MojoExecutionException
    {
        String errorCodeHint = "' The response code was '" + code + "'.";
        String errorMessage = "The server didn't respond for given URL '" + qualifiedUrl + ".";

        if(code != null)
        {
            errorMessage += errorCodeHint;
        }
        
        if(disableServerCheck)
        {
            log.warn(errorMessage);
        }
        else
        {
            throw new MojoExecutionException(errorMessage);
        }
    }

}
