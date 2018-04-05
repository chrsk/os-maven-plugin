package org.opensaga.plugin.compiler.cleanup;

import java.io.File;
import java.io.InputStream;

import org.opensaga.plugin.util.DocumentRootQNameResolver.DocumentRootQNameNotFoundException;



/**
 * Strategy that decides, if a file can be deleted. The method returns a
 * {@link Advise} that indicates, that the file can be safely deleted, must be
 * keeped or maybe an advise to delegate the decision, because there are just
 * indicators, that this files <strong>may be</strong> deleted.
 * 
 * @author cschneider
 */
public interface DeleteCandidateStrategy
{

    public enum Advise
    {
        KEEP,
        UNSURE,
        DELETE
    }


    /**
     * Decide, if a file may be deleted and give an advise. The file path must
     * be as complete as possible, the method <strong>must</strong> handle
     * relative paths and full qualified paths. The file name must be the
     * filename plus extension. The contents must be an {@link InputStream} that
     * <strong>may</strong> be consumed by this method
     * <p>
     * It is to the caller to reset the stream or generate a new one after
     * calling this method. The method returns {@link Advise.KEEP}, if the
     * <strong>must not</strong> be deleted, it returns {@link Advise.DELETE},
     * if it is <strong>safe to delete</strong> and {@link Advise.UNSURE}, if
     * there are indicators, that this file <strong>could be</strong> deleted,
     * e.g. because of matching a naming rule, but maybe it is unsafe to do so,
     * so the decision is delegated to the caller.
     * </p>
     * 
     * @param filename
     * @param contents
     * @return
     * @throws DocumentRootQNameNotFoundException
     */
    Advise isFileDeleteCandidate(File file) throws DocumentRootQNameNotFoundException;


    /**
     * Determines if a directory is a delete candidate and returns the adequate
     * advise for the caller.
     * 
     * @param directory The directory to check
     * @return
     */
    Advise isDirectoryDeleteCandidate(File directory);

}
