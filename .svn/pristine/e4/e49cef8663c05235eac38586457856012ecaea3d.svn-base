package org.opensaga.plugin.release;

import java.util.List;

import org.opensaga.plugin.release.facettes.Facette;
import org.opensaga.plugin.release.facettes.FacettedArtifact;

/**
 * The artifact handler handles all supported artifacts determined by it's
 * facet. A artifact handler can only handle artifacts of one specific facet
 * type. All matching artifacts faceted with this facet are passed into the
 * artifact handler.
 * <p>
 * It's possible to have more than one artifact handler for the same facet. The
 * artifact handlers should be executed in the order they're declared.
 * 
 * @author cklewes
 */
public interface ArtifactHandler
{

    /**
     * Retrieves the class of the supported facet. A artifact handler can only
     * handle artifacts of one specific facet type
     * 
     * @return The class of the supported facet, never {@code null}.
     */
    Class<? extends Facette> getSupportedArtifactsByFacet();


    /**
     * Processes a list of facetted artifacts which are matching the
     * {@link #getSupportedArtifactsByFacet()} routine. The artifacts can be
     * handled but shouldn't be deleted. Otherwise further artifacts for the
     * same facet can't work properly.
     * 
     * @param artifactRequest All collected informations of the artifact request
     *            lifecycle.
     * @param releaseConfiguration The release configuration represents the user
     *            input and customization of the release process.
     * @param facetedArtifact A list of faceted artifacts which should be handled
     */
    void processArtifact(ArtifactRequestContext artifactRequestContext, ReleaseConfiguration releaseConfiguration,
        List<FacettedArtifact> facetedArtifacts);

}
