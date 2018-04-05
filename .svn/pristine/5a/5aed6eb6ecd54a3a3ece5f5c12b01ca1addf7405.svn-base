package org.opensaga.plugin.copyright;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.opensaga.plugin.util.MavenLogAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Updates the copyright information in the files in the main source directory and the model base directory.
 * 
 * @author sbereda
 * @goal update-copyright
 * @requiresDependencyResolution compile+runtime
 * @requiresProject true
 * @phase validate
 */
public class UpdateCopyrightMojo
    extends AbstractMojo
{

    private static final Logger log = LoggerFactory.getLogger(UpdateCopyrightMojo.class);

    /**
     * Provide the source directory that should be updated
     * 
     * @parameter default-value="${basedir}/src/"
     */
    private String sourceDirectory;

    /**
     * The encoding of the files.
     * 
     * @parameter default-value="UTF-8"
     */
    private String fileEncoding;

    /**
     * The Maven project.
     * 
     * @parameter default-value="${project}"
     * @required
     * @readonly
     */
    MavenProject project;

    /**
     * The regular expression pattern that matches the copyright information.
     */
    private static final Pattern COPYRIGHT_PATTERN = Pattern
        .compile("Copyright\\s?\\(C\\)\\s?(\\d{4})-(\\d{4})\\s?QuinScape\\s?GmbH");

    /**
     * The group in the {@link #COPYRIGHT_PATTERN} containing the current year.
     */
    private static final int COPYRIGHT_PATTERN_CURRENT_YEAR_GROUP = 2;

    /**
     * The current year.
     */
    private static final String CURRENT_YEAR = new SimpleDateFormat("YYYY").format(new Date());

    /**
     * The replacement for {@link #COPYRIGHT_PATTERN}.
     */
    private static final String COPYRIGHT_REPLACEMENT = "Copyright (C) $1-" + CURRENT_YEAR +
        " QuinScape GmbH";


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        MavenLogAppender.startPluginLog(this);

        log.info("Execute opensaga:update-copyright");

        int updatedFiles = 0;
        int allFiles = 0;

        // update src dir
        File sourceDir = new File(sourceDirectory);
        Collection<File> mainSourceDirFiles = FileUtils.listFiles(sourceDir, null, true);
        for (File file : mainSourceDirFiles)
        {
            if (updateFile(file))
            {
                updatedFiles++;
            }
            allFiles++;
        }

        log.info("{} of {} files updated!", updatedFiles, allFiles);
    }


    /**
     * Update the copyright information for the given file, when necessary.
     * Returns true, when the file is updated.
     * 
     * @param file
     * @return true, if file was updated, otherwise false
     */
    private boolean updateFile(File file)
    {
        try
        {
            String fileContent = FileUtils.readFileToString(file, fileEncoding);
            Matcher matcher = COPYRIGHT_PATTERN.matcher(fileContent);
            if (matcher.find())
            {
                if (isUpdatedRequired(matcher))
                {
                    log.info("Update {}", file.getAbsolutePath());

                    StringBuffer sb = new StringBuffer();
                    matcher.appendReplacement(sb, COPYRIGHT_REPLACEMENT);
                    matcher.appendTail(sb);
                    FileUtils.writeStringToFile(file, sb.toString(), fileEncoding);
                    return true;
                }
            }
        }
        catch (IOException e)
        {
            log.error("Cannot update {}", file.getAbsolutePath());
        }
        return false;
    }


    /**
     * Returns whether an update is required. It is required when the
     * {@link #COPYRIGHT_PATTERN_CURRENT_YEAR_GROUP} group of the given
     * {@link Matcher} is not equal to {@link #CURRENT_YEAR}.
     * 
     * @param matcher
     * @return
     */
    private boolean isUpdatedRequired(Matcher matcher)
    {
        return !CURRENT_YEAR.equals(matcher.group(COPYRIGHT_PATTERN_CURRENT_YEAR_GROUP));
    }
}
