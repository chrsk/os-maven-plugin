package org.opensaga.plugin.compiler.cleanup;

import java.io.File;
import java.util.HashSet;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.Validate;
import org.opensaga.plugin.util.DocumentRootQNameResolver;
import org.opensaga.plugin.util.DocumentRootQNameResolver.DocumentRootQNameNotFoundException;

/**
 * The implementation of the specific opensaga strategy. The supported files are
 * checked for the extension. The models are typical in a XML file. The root
 * element is checked for a opensaga known model and then marked as deletable.
 * <p>
 * 
 * @author cklewes
 */
public class OpenSAGADeleteCandidateStrategy
    implements DeleteCandidateStrategy
{

    private HashSet<String> supportedModels = new HashSet<String>()
    {
        private static final long serialVersionUID = 1L;
        {
            add("portal");
            add("stages");
            add("agents");
            add("time-based-agent");
            add("relation-set");
            add("constant-domain-type");
            add("property-type");
            add("domain-type");
            add("virtual-domain-type");
            add("layout");
            add("navigation");
            add("process");
            add("view");
            add("translation");
        }
    };


    /**
     * {@inheritDoc}
     */
    @Override
    public Advise isFileDeleteCandidate(File file) throws DocumentRootQNameNotFoundException
    {
        Validate.notNull(file, "The given candidate must not be null!");
        Validate.isTrue(file.isFile(), "The given candidate must be a file not a directory!");

        String extension = FilenameUtils.getExtension(file.getName());
        String filePath = FilenameUtils.normalizeNoEndSeparator(FilenameUtils.getPathNoEndSeparator(file.getAbsolutePath()));
        FileExtension fileExtension = FileExtension.forValue(extension);

        switch (fileExtension)
        {
            case XML:
                return handleXmlFile(file, filePath);
            case XSD:
                return handleXsdFile(filePath);
            default:
                return Advise.KEEP;
        }
    }


    /**
     * Decides if the given XSD file can be deleted.
     * 
     * @param filePath The file path to the xsd file
     */
    private Advise handleXsdFile(String filePath)
    {
        String deletableXsdPath = FilenameUtils.separatorsToSystem("WEB-INF/xsd");

        if (filePath.contains(deletableXsdPath))
        {
            return Advise.DELETE;
        }

        return Advise.KEEP;
    }


    /**
     * Decides if the given XML file can be deleted.
     * 
     * @param filePath The file path to the XML file
     */
    private Advise handleXmlFile(File file, String filePath) throws DocumentRootQNameNotFoundException
    {
        String resourceFolder = FilenameUtils.separatorsToSystem("WEB-INF/resources");

        if (filePath.contains(resourceFolder))
        {
            String rootElement = DocumentRootQNameResolver.resolveRootElementName(file);
            if (supportedModels.contains(rootElement))
            {
                return Advise.DELETE;
            }
        }

        return Advise.KEEP;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Advise isDirectoryDeleteCandidate(File directory)
    {
        Validate.notNull(directory, "The given candidate directory must not be null!");
        Validate.isTrue(directory.isDirectory(), "The given candidate must be a directory");

        // Be conservative and keep everything that we do not identify to handle
        // in a different way
        return Advise.KEEP;
    }

    /**
     * Provides information about the file extension handler. This can be used
     * to delegate the file extension to the specific handler.
     * 
     * @author cklewes
     */
    private enum FileExtension
    {
        /**
         * XML file extension
         */
        XML("xml"),

        /**
         * XSD file extension
         */
        XSD("xsd"),

        /**
         * Unsupported file extension
         */
        UNSUPPORTED("unsupported");

        private final String text;


        private FileExtension(String text)
        {
            this.text = text;
        }

        private String getFileExtension()
        {
            return text;
        }


        public static FileExtension forValue(String fileExtension) throws IllegalArgumentException
        {
            for (FileExtension extension : FileExtension.values())
            {
                if (extension.getFileExtension().equals(fileExtension))
                {
                    return extension;
                }
            }

            return UNSUPPORTED;
        }

    }

}
