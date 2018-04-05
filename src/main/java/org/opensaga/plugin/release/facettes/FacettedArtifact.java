package org.opensaga.plugin.release.facettes;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.Assert;

/**
 * @author cklewes
 *
 */
public class FacettedArtifact
{

    private List<Facette> facetteList;

    private File artifactFile;


    public FacettedArtifact(File artifactFile, Facette... facette)
    {
        Assert.notNull(artifactFile, "The artifact file must not be null");
        
        this.artifactFile = artifactFile;
        this.facetteList = Arrays.asList(facette);
    }


    public List<Facette> getFacettes()
    {
        return facetteList;
    }


    public File getArtifactFile()
    {
        return artifactFile;
    }


    @Override
    public String toString()
    {
        return String.format("FacettedArtifact [facetteList=%s, file=%s]", facetteList, artifactFile.getAbsolutePath());
    }
    
    
}
