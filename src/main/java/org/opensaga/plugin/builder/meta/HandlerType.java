package org.opensaga.plugin.builder.meta;

public enum HandlerType
{

    SOURCE_DOMAIN(),

    TEST_DOMAIN("Test");

    private final String name;


    private HandlerType()
    {
        this("");
    }


    private HandlerType(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return name;
    }

}
