package org.opensaga.plugin.builder.meta;

import java.io.File;

import org.opensaga.plugin.builder.meta.parser.TypeBasedModelParser;

public class ModelCandidate
{

    private TypeBasedModelParser handler;

    private File file;


    public ModelCandidate(TypeBasedModelParser handler, File file)
    {
        super();
        this.handler = handler;
        this.file = file;
    }


    public TypeBasedModelParser getHandler()
    {
        return handler;
    }


    public File getFile()
    {
        return file;
    }

}
