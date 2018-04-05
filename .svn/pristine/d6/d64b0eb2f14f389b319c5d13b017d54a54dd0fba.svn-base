package org.opensaga.plugin.resource.cleaner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.opensaga.plugin.compiler.cleanup.DeleteCandidateStrategy.Advise;
import org.opensaga.plugin.compiler.cleanup.OpenSAGADeleteCandidateStrategy;
import org.opensaga.plugin.util.DocumentRootQNameResolver.DocumentRootQNameNotFoundException;

public class OpenSAGADeleteCandidateStrategyTest
{

    private static final File baseDir = new File("src/test/resources/remove-strategy/");

    private OpenSAGADeleteCandidateStrategy deleteCandidateStrategy;


    @Before
    public void initializeTest()
    {
        deleteCandidateStrategy = new OpenSAGADeleteCandidateStrategy();
    }


    @Test(expected = IllegalArgumentException.class)
    public void testIfisDirectoryDeleteCandidateCanHandleNull()
    {
        deleteCandidateStrategy.isDirectoryDeleteCandidate(null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testIfisDirectoryDeleteCandidateCanHandleFile()
    {
        deleteCandidateStrategy.isDirectoryDeleteCandidate(new File(baseDir, "emptyFile.txt"));
    }


    @Test
    public void testIfisDirectoryDeleteCandidateResultIsCorrect()
    {
        Advise advise = deleteCandidateStrategy
            .isDirectoryDeleteCandidate(new File(baseDir, "deleteCandidateDirectory"));
        assertThat(advise, equalTo(Advise.KEEP));
    }


    @Test(expected = IllegalArgumentException.class)
    public void fileDeleteCandidateCanHandleNullFileName() throws DocumentRootQNameNotFoundException
    {
        deleteCandidateStrategy.isFileDeleteCandidate(null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void fileDeleteCandidateCanHandleDirectory() throws DocumentRootQNameNotFoundException
    {
        deleteCandidateStrategy.isFileDeleteCandidate(new File(baseDir, "deleteCandidateDirectory"));
    }


    @Test(expected = DocumentRootQNameNotFoundException.class)
    public void fileDeleteCandidateResultCanHandleEmptyXMLFile() throws DocumentRootQNameNotFoundException
    {
        File file = new File(baseDir, "WEB-INF/resources/empty.xml");
        Advise advise = deleteCandidateStrategy.isFileDeleteCandidate(file);
        assertThat(advise, equalTo(Advise.KEEP));
    }


    @Test
    public void fileDeleteCandidateResultCanHandleKeepXMLFile() throws DocumentRootQNameNotFoundException
    {
        File file = new File(baseDir, "WEB-INF/resources/keep.xml");
        Advise advise = deleteCandidateStrategy.isFileDeleteCandidate(file);
        assertThat(advise, equalTo(Advise.KEEP));
    }


    @Test
    public void fileDeleteCandidateResultCanHandleRemoveXMLFile() throws DocumentRootQNameNotFoundException
    {
        File file = new File(baseDir, "WEB-INF/resources/remove.xml");
        Advise advise = deleteCandidateStrategy.isFileDeleteCandidate(file);
        assertThat(advise, equalTo(Advise.DELETE));
    }


    @Test
    public void fileDeleteCandidateResultCanHandleKeepXMLOutsideWEBINFStructureFile()
        throws DocumentRootQNameNotFoundException
    {
        File file = new File(baseDir, "keep.xml");
        Advise advise = deleteCandidateStrategy.isFileDeleteCandidate(file);
        assertThat(advise, equalTo(Advise.KEEP));
    }


    @Test
    public void fileDeleteCandidateResultCanHandleRemoveXMLOutsideWEBINFStructureFile()
        throws DocumentRootQNameNotFoundException
    {
        File file = new File(baseDir, "remove.xml");
        Advise advise = deleteCandidateStrategy.isFileDeleteCandidate(file);
        assertThat(advise, equalTo(Advise.KEEP));
    }


    @Test
    public void fileDeleteCandidateResultCanHandleKeepXSDFile() throws DocumentRootQNameNotFoundException
    {
        File file = new File(baseDir, "WEB-INF/xsd/remove.xsd");
        Advise advise = deleteCandidateStrategy.isFileDeleteCandidate(file);
        assertThat(advise, equalTo(Advise.DELETE));
    }


    @Test
    public void fileDeleteCandidateResultCanHandleRemoveXSDOutsideWEBINFStructureFile()
        throws DocumentRootQNameNotFoundException
    {
        File file = new File(baseDir, "remove.xsd");
        Advise advise = deleteCandidateStrategy.isFileDeleteCandidate(file);
        assertThat(advise, equalTo(Advise.KEEP));
    }

}
