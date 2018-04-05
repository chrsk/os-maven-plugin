package org.opensaga.plugin.builder.meta.generator.model;

import org.opensaga.plugin.builder.meta.parser.PropertyType;

/**
 * Describes a property representation of properties which are defined in
 * {@link DomainTypeMetaModel}s. A {@code PropertyMetaModel} provides access to
 * their ID, name and type.
 * 
 * @author cklewes
 */
public class PropertyMetaModel
    extends AbstractMetaModel
{

    private PropertyType type;

    private String propertyRef;


    /**
     * Retrieves the property type of this property model.
     * 
     * @return The property type.
     */
    public PropertyType getType()
    {
        return type;
    }


    /**
     * Defines the property type of this property model.
     * 
     * @param type The property type.
     */
    public void setType(PropertyType type)
    {
        this.type = type;
    }


    /**
     * A property meta model may refers to another property model. This is the
     * case for e.g. enum properties. Enum properties refers to real properties.
     * This can be used to retrieve their property type.
     * 
     * @return  The reference to another property.
     */
    public String getPropertyRef()
    {
        return propertyRef;
    }


    /**
     * Defines the property reference for this property meta model.
     * 
     * @see #getPropertyRef()
     * @param propertyRef The property reference
     */
    public void setPropertyRef(String propertyRef)
    {
        this.propertyRef = propertyRef;
    }


    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("PropertyMetaModel [");
        if (getId() != null)
            builder.append("id=").append(getId()).append(", ");
        if (getName() != null)
            builder.append("name=").append(getName()).append(", ");
        if (getType() != null)
            builder.append("type=").append(getType()).append(", ");
        if (propertyRef != null)
            builder.append("propertyRef=").append(propertyRef);
        builder.append("]");
        return builder.toString();
    }

}
