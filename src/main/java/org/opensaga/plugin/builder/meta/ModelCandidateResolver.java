package org.opensaga.plugin.builder.meta;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Finds candidates for the model generation in a specific folder. The files are
 * reviewed and tested for their applicability.
 * 
 * @author cklewes
 */
public class ModelCandidateResolver
{
    private static final String DEFAULT_MODEL_EXTENSION = "xml";

    private static final Logger log = LoggerFactory.getLogger(ModelCandidateResolver.class);

    
    /**
     * Finds candidates by the given model file set. It returns a collection of
     * file objects which contains the candidates. The default model extension
     * is used.
     * 
     * @see #DEFAULT_MODEL_EXTENSION
     * @see #findCandidates(ModelFileSet, String)
     * @param fileSet The file set which configures the search
     * @return A collection collecting either the found candidates or nothing.
     */
    public static Collection<File> findCandidates(ModelFileSet fileSet)
    {
        return findCandidates(fileSet, DEFAULT_MODEL_EXTENSION);
    }


    /**
     * Finds candidates by the given model file set. The model extension can be
     * specified. A collection of file objects which are the possible candidates
     * are returned.
     * 
     * @param fileSet The file set which configures the search
     * @param modelExtension The extension of the models
     * @return A collection collecting either the found candidates or nothing.
     */
    public static Collection<File> findCandidates(ModelFileSet fileSet, String modelExtension)
    {
        String normalized = FilenameUtils.normalize(fileSet.getBaseDirectory());

        log.info("The meta model candidates will be searched in '" + normalized + "'.");

        if (fileSet.getIncludes().length > 0 || fileSet.getExcludes().length > 0)
        {
            log.info("Extensions included: " + Arrays.toString(fileSet.getExcludes()));
            log.info("Extensions excluded: " + Arrays.toString(fileSet.getIncludes()));
        }

        File file = new File(normalized);

        if (file.exists())
        {
            ModelCandidateFilter candidateFilter = new ModelCandidateFilter(fileSet, modelExtension);
            Collection<File> candidates = FileUtils.listFiles(file, candidateFilter, TrueFileFilter.INSTANCE);

            return candidates;
        }

        return Collections.emptyList();
    }

    /**
     * Filters a model candidate via the given model file set.
     * 
     * @see ModelCandidateResolver#findCandidates(ModelFileSet, String)
     * @author cklewes
     */
    private static class ModelCandidateFilter
        implements IOFileFilter
    {

        private final String modelExtension;

        private final ModelFileSet fileSet;


        public ModelCandidateFilter(ModelFileSet fileSet, String modelExtension)
        {
            this.fileSet = fileSet;
            this.modelExtension = modelExtension;
        }


        @Override
        public boolean accept(File location)
        {
            if (location.isFile())
            {
                if (checkForIncludes(location) && checkForExcludes(location))
                {
                    if (pathMatchesDirectories(location, fileSet.getModelDirectories()))
                    {
                        return accept(location, location.getName());
                    }
                }
            }

            return false;
        }


        /**
         * Checks if the given location is excluded from the search.
         * 
         * @param location The location to check.
         * @return Either {@code true} if the folder is excluded or {@code false}
         *         if not.
         */
        private boolean checkForExcludes(File location)
        {
            return fileSet.getExcludes().length == 0 ||
                !pathMatchesDirectories(location, Arrays.asList(fileSet.getExcludes()));
        }


        /**
         * Checks if the given location is included from the search.
         * 
         * @param location The location to check.
         * @return Either {@code true} if the folder is included or
         *         {@code false} if not.
         */
        private boolean checkForIncludes(File location)
        {
            return fileSet.getIncludes().length == 0 ||
                pathMatchesDirectories(location, Arrays.asList(fileSet.getIncludes()));
        }


        /**
         * Check if the given location contains one of the directories.
         * 
         * @param location The location to check
         * @param directories The directories which should be at least match one
         *            time.
         * @return Either {@code true} if location contains one of the passed
         *         directories, otherwise {@code false}.
         */
        private boolean pathMatchesDirectories(File location, List<String> directories)
        {
            boolean pathMatches = false;

            String normalizedLocation = FilenameUtils.separatorsToUnix(location.getAbsolutePath());

            for (String directoryPart : directories)
            {
                String normalizedDirectoryPart = FilenameUtils.separatorsToUnix(directoryPart);
                pathMatches |= normalizedLocation.contains(normalizedDirectoryPart);
            }

            return pathMatches;
        }


        @Override
        public boolean accept(File dir, String name)
        {
            boolean isMatchingExtension = FilenameUtils.isExtension(name, modelExtension);

            return isMatchingExtension;
        }

    }

    public static class ModelFileSet
    {
        private String baseDirectory;

        private String[] includes;

        private String[] excludes;

        private List<String> modelDirectories;


        public ModelFileSet(String baseDirectory)
        {
            this.baseDirectory = baseDirectory;
        }


        public void setIncludes(String[] includes)
        {
            this.includes = includes;
        }


        public String[] getIncludes()
        {
            return includes;
        }


        public void setExcludes(String[] excludes)
        {
            this.excludes = excludes;
        }


        public String[] getExcludes()
        {
            return excludes;
        }


        public void setBaseDirectory(String baseDirectory)
        {
            this.baseDirectory = baseDirectory;
        }


        public String getBaseDirectory()
        {
            return baseDirectory;
        }


        public void setModelDirectories(List<String> modelDirectories)
        {
            this.modelDirectories = modelDirectories;
        }


        public List<String> getModelDirectories()
        {
            return modelDirectories;
        }
    }
}
