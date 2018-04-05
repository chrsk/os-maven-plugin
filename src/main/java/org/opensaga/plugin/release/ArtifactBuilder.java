package org.opensaga.plugin.release;

import java.util.List;

import org.opensaga.plugin.release.facettes.FacettedArtifact;
import org.opensaga.plugin.release.handler.MavenBasedArtifactBuilder;

/**
 * The artifact builder is responsible to create artifacts. These artifacts can
 * be faceted for further procession. The faceted artifact will be handled by
 * their matching {@link ArtifactHandler}. To prepare a release please have a
 * look into {@link ReleasePreparer}.
 * <p>
 * The artifact builder builds at least one faceted artifact. All collected
 * informations are passed in the {@link ArtifactRequestContext}.
 * 
 * @see FacettedArtifact
 * @see MavenBasedArtifactBuilder
 * @author cklewes
 */
public interface ArtifactBuilder
{

    /**
     * Synthesizes a list of artifacts. At least one artifact. The artifacts can
     * be faceted for further post procession. The built artifact must have a
     * valid file handle.
     * 
     * @param artifactRequest All collected informations of the artifact request
     *            lifecycle.
     * @param releaseConfiguration The release configuration represents the user
     *            input and customization of the release process.
     * @return A list of synthesized artifacts, never an empty list!
     * @throws ArtifactCreationException Whenever the creation of the artifact
     *             fails abnormally. This is handled and presented to the user
     *             as an fatal error.
     */
    List<FacettedArtifact> synthesizeArtifacts(ArtifactRequestContext artifactRequest, ReleaseConfiguration releaseConfiguration)
        throws ArtifactCreationException;

}
