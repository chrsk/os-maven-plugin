package org.opensaga.plugin.release;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.shared.invoker.Invoker;

/**
 * Defines the context which is accessible in the whole artifact/release
 * lifecycle. The lifecycle begins in the preparation of the artifact phase, and
 * ends in when all artifacts are handled. The {@code ArtifactRequestContext}
 * contains all informations about the current maven session and the release
 * request.
 * 
 * @author cklewes
 */
public interface ArtifactRequestContext
{

    /**
     * Retrieves the maven session from the context. The maven session contains
     * all informations about the current state, location and meta informations
     * of the project.
     * 
     * @return The maven session for this project.
     */
    MavenSession getMavenSession();


    /**
     * Retrieves the maven invoker. The maven invoker is useful for invoking a
     * specific maven goal on this project.
     * 
     * @return the maven invoker
     */
    Invoker getInvoker();


    /**
     * Gets the release request, the release request contains all original
     * (non-modified) informations of the release request.
     * 
     * @return
     */
    ReleaseRequest getReleaseRequest();

}
