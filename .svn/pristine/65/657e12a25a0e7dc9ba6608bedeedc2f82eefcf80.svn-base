package org.opensaga.plugin.builder.meta.parser.types.view;

public enum ComponentIdentifier
{

    ID(false, "id"),

    NAME(true, "label", "text", "heading", "title");

    private final String[] attributes;
    
    private final boolean translatedValue;


    private ComponentIdentifier(boolean translatedValue, String... attributes)
    {
        this.translatedValue = translatedValue;
        this.attributes = attributes;
    }


    public static ComponentIdentifier getIdentifierForAttribute(String attribute)
    {
        for (ComponentIdentifier componentIdentifier : ComponentIdentifier.values())
        {
            String[] attributes = componentIdentifier.attributes;

            for (String currentAttribute : attributes)
            {
                if (currentAttribute.equals(attribute))
                {
                    return componentIdentifier;
                }
            }
        }

        return null;
    }


    public boolean isTranslatedValue()
    {
        return translatedValue;
    }
}
