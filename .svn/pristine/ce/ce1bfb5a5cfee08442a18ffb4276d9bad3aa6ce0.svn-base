package org.opensaga.plugin.release.handler;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.opensaga.plugin.release.ArtifactBuilder;
import org.opensaga.plugin.release.ArtifactCreationException;
import org.opensaga.plugin.release.ArtifactRequestContext;
import org.opensaga.plugin.release.ReleaseConfiguration;
import org.opensaga.plugin.release.facettes.DeployFacette;
import org.opensaga.plugin.release.facettes.FacettedArtifact;
import org.opensaga.plugin.release.facettes.HistorizeFacette;
import org.opensaga.plugin.release.facettes.UploadFacette;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MavenBasedArtifactBuilder
    implements ArtifactBuilder
{

    private static final Logger log = LoggerFactory.getLogger(MavenBasedArtifactBuilder.class);
    
    @Override
    public List<FacettedArtifact> synthesizeArtifacts(ArtifactRequestContext artifactRequest,
        ReleaseConfiguration releaseConfiguration) throws ArtifactCreationException
    {
        final String goals = artifactRequest.getReleaseRequest().getGoals();
        final MavenProject currentProject = artifactRequest.getMavenSession().getCurrentProject();
        
        if (StringUtils.isNotEmpty(goals))
        {
            log.info("Invoking Maven goals to produce artifact");
            InvocationRequest request = new DefaultInvocationRequest()
                .setBaseDirectory(currentProject.getBasedir())
                .setRecursive(false)
                .setNonPluginUpdates(false)
                .setShellEnvironmentInherited(false)
                .setGoals(Arrays.asList(StringUtils.split(goals, ",")));
            
            try
            {
                final InvocationResult result = artifactRequest.getInvoker().execute(request);
                
                if(result.getExitCode() != 0)
                {
                    throw new ArtifactCreationException("Subsequent maven goal failed.", result.getExecutionException());
                }
            }
            catch (MavenInvocationException e)
            {
                throw new ArtifactCreationException("Cannot run additions goals.", e);
            }

            final String buildDirectory = currentProject.getBuild().getDirectory();
            final String finalName = currentProject.getBuild().getFinalName();
            final String packaging = currentProject.getPackaging();
            final File artifact = new File(FilenameUtils.concat(buildDirectory, finalName + "." + packaging));
            
            return Arrays.asList(new FacettedArtifact(artifact, new DeployFacette(), new UploadFacette(), new HistorizeFacette()));
        }
        
        return Collections.emptyList();
    }

}
