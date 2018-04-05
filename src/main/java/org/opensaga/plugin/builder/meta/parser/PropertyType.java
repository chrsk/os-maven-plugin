package org.opensaga.plugin.builder.meta.parser;

public class PropertyType
{

    private String typeClass;


    public PropertyType(String typeClass)
    {
        super();
        this.typeClass = typeClass;
    }


    public String getTypeClass()
    {
        return typeClass;
    }


    public void setTypeClass(String typeClass)
    {
        this.typeClass = typeClass;
    }


    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("PropertyType [typeClass=").append(typeClass).append("]");
        return builder.toString();
    }

}
