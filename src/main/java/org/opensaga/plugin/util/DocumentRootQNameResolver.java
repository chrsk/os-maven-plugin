package org.opensaga.plugin.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Resolves the root element name of a XML file. The file will be read and
 * queried for it's root element name.
 * 
 * @author cklewes
 */
public class DocumentRootQNameResolver
{

    /**
     * Resolves the root element name of the given XML file. The XML file must
     * be well formed. The qualified name will be returned.
     * 
     * @param file The file to resolve the root element from. It must not be
     *            {@code null}.
     * @return The root element of the given XML file.
     * @throws DocumentRootQNameNotFoundException If an error occured while
     *             resolving the root element.
     */
    public static String resolveRootElementName(File file) throws DocumentRootQNameNotFoundException
    {
        try
        {
            FileInputStream fileInputStream = new FileInputStream(file);
            String rootElement = resolveRootElement(fileInputStream);
            fileInputStream.close();

            return rootElement;
        }
        catch (IOException e)
        {
            throw new DocumentRootQNameNotFoundException();
        }
    }


    /**
     * Resolves the root element name of the given XML input stream. The content
     * must be well formed. The qualified name will be returned.
     * 
     * @see #resolveRootElementName(File)
     * @param fileInputStream The input stream of a well-formed XML file.
     * @return The root element of the given XML file.
     * @throws DocumentRootQNameNotFoundException If an error occured while
     *             resolving the root element.
     */
    public static String resolveRootElement(InputStream fileInputStream) throws DocumentRootQNameNotFoundException
    {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try
        {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            saxParser.parse(fileInputStream, new SaxRootElementHandler());
        }
        catch (RootElementAbortException e)
        {
            return e.getRootName();
        }
        catch (Exception e)
        {
            throw new DocumentRootQNameNotFoundException(e);
        }

        return null;
    }

    private static class SaxRootElementHandler
        extends DefaultHandler
    {
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
        {
            // Throw exception immediately to abort the handler
            throw new RootElementAbortException(qName);
        }
    }

    public static class DocumentRootQNameNotFoundException
        extends Exception
    {

        private static final long serialVersionUID = -5926798400925277932L;


        public DocumentRootQNameNotFoundException()
        {
            super();
        }


        public DocumentRootQNameNotFoundException(String message, Throwable cause)
        {
            super(message, cause);
        }


        public DocumentRootQNameNotFoundException(String message)
        {
            super(message);
        }


        public DocumentRootQNameNotFoundException(Throwable cause)
        {
            super(cause);
        }

    }

    /**
     * A holder exception for the qualified root element name. The exception is
     * thrown by the {@link SaxRootElementHandler} after reading the first start
     * element which obviously is the root element.
     * 
     * @author cklewes
     */
    private static class RootElementAbortException
        extends SAXException
    {

        private static final long serialVersionUID = 1640032933505957182L;

        private final String rootName;


        public RootElementAbortException(String rootName)
        {
            this.rootName = rootName;

        }


        public String getRootName()
        {
            return rootName;
        }
    }

}
