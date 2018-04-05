package org.opensaga.plugin.builder.meta.generator.model;

import org.opensaga.plugin.builder.meta.parser.TypeBasedModelParser;

/**
 * The default abstract implementation of a meta model.
 * 
 * @author cklewes
 *
 */
public abstract class AbstractMetaModel implements EditableMetaModel
{
    
    private String id;

    private String name;
    
    private String location;

    private TypeBasedModelParser parser;
    
    @Override
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    @Override
    public String getName()
    {
        return name;
    }


    public void setName(String name)
    {
        this.name = name;
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractMetaModel other = (AbstractMetaModel) obj;
        if (id == null)
        {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        if (location == null)
        {
            if (other.location != null)
                return false;
        }
        else if (!location.equals(other.location))
            return false;
        if (name == null)
        {
            if (other.name != null)
                return false;
        }
        else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String getLocation()
    {
        return location;
    }
    
    public void setLocation(String location)
    {
        this.location = location;
    }
    
    @Override
    public TypeBasedModelParser getParser()
    {
        return parser;
    }
    
    @Override
    public void setParser(TypeBasedModelParser parser)
    {
        this.parser = parser;
    }
    
}
