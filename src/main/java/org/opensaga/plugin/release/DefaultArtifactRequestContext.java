package org.opensaga.plugin.release;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.shared.invoker.Invoker;

/**
 * The default implementation of {@link ArtifactRequestContext}.
 * 
 * @author cklewes
 */
public class DefaultArtifactRequestContext
    implements ArtifactRequestContext
{

    private ReleaseRequest releaseRequest;

    private Invoker invoker;

    private MavenSession mavenSession;


    public DefaultArtifactRequestContext setMavenSession(MavenSession mavenSession)
    {
        this.mavenSession = mavenSession;
        return this;
    }


    @Override
    public MavenSession getMavenSession()
    {
        return mavenSession;
    }


    @Override
    public Invoker getInvoker()
    {
        return invoker;
    }


    public DefaultArtifactRequestContext setInvoker(Invoker invoker)
    {
        this.invoker = invoker;
        return this;
    }


    @Override
    public ReleaseRequest getReleaseRequest()
    {
        return releaseRequest;
    }


    public DefaultArtifactRequestContext setReleaseRequest(ReleaseRequest releaseRequest)
    {
        this.releaseRequest = releaseRequest;
        return this;
    }

}
