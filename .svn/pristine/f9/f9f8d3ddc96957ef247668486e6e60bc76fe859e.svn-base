package org.opensaga.plugin.compiler.cleanup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.opensaga.plugin.compiler.cleanup.DeleteCandidateStrategy.Advise;
import org.opensaga.plugin.util.DocumentRootQNameResolver.DocumentRootQNameNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Removes files from the exploded war file structure that became obsolete after
 * precompiling the application. Following realms are cleaned up.
 * <ul>
 * <li>JAR-files that are named {@code opensaga-*.jar}</li>
 * <li>Files with the {@code XML} extension</li>
 * </ul>
 * <p>
 * It removes all content inside the {@code WEB-INF} and {@code META-INF} folder
 * and the {@code index.jsp} from <strong>inside</strong> JAR files, but leaves
 * all other content untouched. From the exploded file structure all valid
 * XML-files that start with a defined root element and that are in the folder
 * {@code WEB-INF/resources} or a subfolder will be deleted.
 * </p>
 * 
 * @author cschneider
 */
public class OpenSAGAResourceCleaner
{
    private static final Logger log = LoggerFactory.getLogger(OpenSAGAResourceCleaner.class);

    private final String OPENSAGA_EXTENSION_PREFIX = "opensaga-";

    private final DeleteCandidateStrategy deleteCandidateStrategy;

    private final String baseDirectory;


    public OpenSAGAResourceCleaner(DeleteCandidateStrategy deleteCandidateStrategy, String baseDirectory)
    {
        this.deleteCandidateStrategy = deleteCandidateStrategy;
        this.baseDirectory = baseDirectory;
    }


    public void cleanUp() throws MojoExecutionException, MojoFailureException
    {
        long jarBytesGained = 0;
        long fileBytesGained = 0;

        Collection<File> candidates = findRemoveCandidates();
        for (File candidate : candidates)
        {
            String absolutePath = candidate.getAbsolutePath();
            log.debug("Processing: " + absolutePath);

            if (FilenameUtils.isExtension(absolutePath, "jar"))
            {
                jarBytesGained = jarBytesGained + handleJarFile(candidate);
            }
            else
            {
                fileBytesGained = fileBytesGained + handleFile(candidate);
            }
        }

        double jarMBGained = ByteUnitConverter.convert(jarBytesGained, ByteUnit.MEGA);
        double fileMBGained = ByteUnitConverter.convert(fileBytesGained, ByteUnit.MEGA);
        double allMBGained = jarMBGained + fileMBGained;

        String formatResult = "%23s %6.2f %s";
        String formatLine = "%33s";

        log.info("");
        log.info("Cleaning Results:");
        log.info("");
        log.info(String.format(formatResult, "from jars:", jarMBGained, "MB"));
        log.info(String.format(formatResult, "from files:", fileMBGained, "MB"));
        log.info(String.format(formatLine, "---------"));
        log.info(String.format(formatResult, "Resources cleaned up:", allMBGained, "MB"));
    }

    private static enum ByteUnit
    {

        KILO(1),
        MEGA(2),
        GIGA(3);

        public final int EXPONENT;


        private ByteUnit(int exponent)
        {
            this.EXPONENT = exponent;
        }
    }

    private static class ByteUnitConverter
    {

        private static final int CONVERSION_VALUE = 1024;


        public static double convert(long bytes, ByteUnit unit)
        {
            return ((double) Math.round((bytes / Math.pow(CONVERSION_VALUE, unit.EXPONENT) * 100))) / 100;
        }
    }


    /**
     * Handle all files and delete them, if appropriate. This method must not
     * provide any special treatments of files, like jar-files, just delete
     * files or not.
     * 
     * @param candidate
     * @return bytes gained
     */
    private long handleFile(File candidate)
    {

        long bytesGained = 0;
        FileInputStream fileInputStream = null;
        try
        {
            if (deleteCandidateStrategy.isFileDeleteCandidate(candidate) == Advise.DELETE)
            {
                bytesGained = candidate.length();
                boolean fileDeleted = candidate.delete();

                if (fileDeleted)
                {
                    log.debug(
                        "Gaining " + bytesGained + " by removing file " + candidate.getAbsolutePath());
                }
                else
                {
                    bytesGained = 0;
                    log.error("Cannot remove file " + candidate.getAbsolutePath());
                }
            }
        }
        catch (DocumentRootQNameNotFoundException e)
        {
            log.error(
                "Ignoring file. Error occured while examining the document root name: " + candidate.getAbsolutePath(),
                e);
        }
        catch (Exception e)
        {
            log.error("Ignoring file. Error while parsing: " + candidate.getAbsolutePath(), e);
        }
        finally
        {
            IOUtils.closeQuietly(fileInputStream);
        }
        return bytesGained;
    }


    /**
     * This method handles jar-files only. It creates a temporary jar file,
     * removes WEB-INF, META-INF and index.jsp and replaces the original jar
     * file. If there are no other resources than the deleted ones, the original
     * war file is deleted.
     * 
     * @param candidate
     * @return bytes gained
     */
    private long handleJarFile(File candidate)
    {

        ZipEntry entry;

        long originalFilesize = candidate.length();
        long newFilesize = 0;
        int filesPreserved = 0;
        File jarTemp = null;

        log.debug("Phase 1: Find longest removable paths");
        List<String> shortestPathsToRemove = findRemovablePathsInJar(candidate);

        log.debug("Phase 2: Processing jar file");
        try
        {
            jarTemp = File.createTempFile("opensaga", "jar");

            ZipInputStream inJar = null;
            ZipOutputStream outJar = null;

            boolean noUnknownContentFound = true;

            try
            {
                inJar = new ZipInputStream(new FileInputStream(candidate));
                outJar = new ZipOutputStream(new FileOutputStream(jarTemp));

                while ((entry = inJar.getNextEntry()) != null)
                {
                    String entryName = entry.getName();

                    boolean preserveFile = true;
                    // Check, if this entry starts with a path that is marked to
                    // be removed from the jar file
                    for (String path : shortestPathsToRemove)
                    {
                        if (entryName.startsWith(path))
                        {
                            preserveFile = false;
                            break;
                        }
                    }

                    if (preserveFile)
                    {
                        // At this point there are no directories that are
                        // marked to be removed, but there may be files, so
                        // handle them here.
                        if ("index.jsp".equals(entryName))
                        {
                            preserveFile = false;
                        }
                        else
                        {
                            // flag, that at least one file is _not_ removed.
                            noUnknownContentFound = false;

                            log.debug("Preserving: " + entryName);
                            filesPreserved++;
                            outJar.putNextEntry(entry);
                            IOUtils.copy(inJar, outJar);
                            outJar.closeEntry();
                        }
                    }
                    else if (log.isDebugEnabled())
                    {
                        log.debug("Removing: " + entryName);
                    }
                }

                // A zip archive must have at least one entry. Since the zip
                // archive is empty, when noUnknownConentFound is set, we set
                // one fake entry, because the temporary zip file will not be
                // copied.
                if (noUnknownContentFound)
                {
                    entry = new ZipEntry("fake");
                    outJar.putNextEntry(entry);
                    outJar.write(0);
                    outJar.closeEntry();
                }

            }
            catch (IOException e)
            {
                log.error("Error while writing temporary zipfile");
            }
            finally
            {
                IOUtils.closeQuietly(inJar);
                IOUtils.closeQuietly(outJar);
            }

            if (noUnknownContentFound)
            {
                newFilesize = removeJarFile(candidate);
            }
            else
            {
                newFilesize = replaceJarFile(candidate, jarTemp, filesPreserved);
            }
        }
        catch (IOException e)
        {
            newFilesize = originalFilesize;
            log.error(
                "Error while creating temporary file to shrink candidate jar file '" + candidate.getAbsolutePath() +
                    "'. Leaving it untouched.", e);
        }
        finally
        {
            if (jarTemp != null)
            {
                jarTemp.delete();
            }
        }

        return originalFilesize - newFilesize;
    }


    /**
     * Finds the shortest paths in a OpenSAGA jar file that can be removed.
     * 
     * @param candidate
     * @return
     */
    private List<String> findRemovablePathsInJar(File candidate)
    {
        ArrayList<String> resultList = new ArrayList<String>();
// Removed the following statements because they removed the META-INF folder which is now used for the build number
        
//        Set<String> metaInfSet = new HashSet<String>();
//
//        ZipInputStream inJar = null;
//        ZipEntry entry;
//        boolean removeMetaInf = true;
//
//        try
//        {
//            inJar = new ZipInputStream(new FileInputStream(candidate));
//
//            while ((entry = inJar.getNextEntry()) != null)
//            {
//                String entryName = entry.getName();
//
//                // Search in META-INF and add candidates to the HashSet
//                // (probably more than one time, but the HashSet will handle
//                // that), because we only know if we can remove META-INF when
//                // the whole jar file is processed.
//                if (entryName.startsWith("META-INF"))
//                {
//                    if (entryName.startsWith("META-INF/maven"))
//                    {
//                        metaInfSet.add("META-INF/maven");
//                    }
//                    else if (entryName.startsWith("META-INF/MANIFEST.MF"))
//                    {
//                        metaInfSet.add("META-INF/MANIFEST.MF");
//                    }
//                    else if (!entryName.equals("META-INF"))
//                    {
//                        removeMetaInf = false;
//                    }
//                }
//
//                // Added by default, see below
//                // else if (entryName.startsWith("WEB-INF")) { }
//            }
//
//        }
//        catch (IOException e)
//        {
//            log.error("Error while writing temporary zipfile");
//        }
//        finally
//        {
//            IOUtils.closeQuietly(inJar);
//        }
//
//        // If only removable content is found in META-INF, add the directory to
//        // the resultList, if not, the set generated above is added to handle a
//        // more precise remove.
//        if (removeMetaInf)
//        {
//            resultList.add("META-INF");
//        }
//        else
//        {
//            resultList.addAll(metaInfSet);
//        }
        resultList.add("WEB-INF");
        return resultList;
    }


    /**
     * Removes a candidate jar file or leaves it, when it cannot be deleted and
     * returns the correct amount of bytes that were removed.
     * 
     * @param candidate
     * @return
     */
    private long removeJarFile(File candidate)
    {
        long newFilesize = 0;
        long originalFilesize = candidate.length();
        boolean fileDeleted = candidate.delete();
        if (fileDeleted)
        {
            newFilesize = 0;
            log.info(
                "Removing '" + candidate.getName() + "' because no non-model-content was found. " + (originalFilesize) +
                    " bytes gained.");
        }
        else
        {
            newFilesize = originalFilesize;
            log.warn(
                "Cannot remove file: " + candidate.getAbsolutePath() + ". Leaving old file untouched.");
        }
        return newFilesize;
    }


    /**
     * Replaces a candidate jar file with the processed jar file. Leaves the
     * original jar file if the move operation does not work. Returns the
     * correct amount of bytes gained.
     * 
     * @param candidate
     * @param jarTemp
     * @param filesPreserved
     * @return
     */
    private long replaceJarFile(File candidate, File jarTemp, int filesPreserved)
    {
        long originalFilesize = candidate.length();
        long newFilesize = 0;

        boolean fileDeleted = candidate.delete();
        if (fileDeleted)
        {
            newFilesize = jarTemp.length();
            try
            {
                FileUtils.moveFile(jarTemp, candidate);
            }
            catch (IOException e)
            {
                log.error(
                    "Error while moving: " + jarTemp.getAbsolutePath() + " to " + candidate.getAbsolutePath() +
                        ". Leaving old file untouched.");
            }
            log.info(
                "Processed '" + candidate.getName() + "'. " + filesPreserved + " files preserved. " +
                    (originalFilesize - newFilesize) + " bytes gained.");
        }
        else
        {
            newFilesize = originalFilesize;
            log.warn(
                "Cannot remove file: " + candidate.getAbsolutePath() + ". Leaving old file untouched.");
        }
        return newFilesize;
    }


    /**
     * Finds candidate files to remove or process in the outputDirectory.
     * 
     * @return
     */
    private Collection<File> findRemoveCandidates()
    {
        return FileUtils.listFiles(new File(baseDirectory), new CandidateFilter(), TrueFileFilter.INSTANCE);
    }

    /**
     * CandidateFilter that filters candidate files that may be processed by
     * cleanup. It ignores directories and collects xml, xsd, css and // *
     * opensaga-jar files.
     */
    private class CandidateFilter
        implements IOFileFilter
    {

        @Override
        public boolean accept(File pathname)
        {
            String absolutePath = pathname.getAbsolutePath();
            if (pathname.isFile())
            {
                return accept(new File(FilenameUtils.getFullPath(absolutePath)), FilenameUtils.getName(absolutePath));
            }
            else
            {
                // ignore directories.
            }
            return false;
        }


        @Override
        public boolean accept(File dir, String name)
        {
            boolean isXmlFile = FilenameUtils.isExtension(name, "xml");
            boolean isXsdFile = FilenameUtils.isExtension(name, "xsd");
            boolean isCssFile = FilenameUtils.isExtension(name, "css");
            boolean isOpenSagaExtensionJar = FilenameUtils.getBaseName(name).startsWith(OPENSAGA_EXTENSION_PREFIX) &&
                FilenameUtils.isExtension(name, "jar");
            return isXmlFile || isXsdFile || isCssFile || isOpenSagaExtensionJar;
        }

    }
}
