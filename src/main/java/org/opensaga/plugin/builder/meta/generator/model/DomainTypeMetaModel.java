package org.opensaga.plugin.builder.meta.generator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.util.Assert;

/**
 * The meta model for the OpenSAGA domain type model. A domain type meta model
 * provides the property models of this domain type.
 * 
 * @author cklewes
 */
public class DomainTypeMetaModel
    extends AbstractMetaModel
{

    private final HashMap<String, PropertyMetaModel> metaPropertyModels = new HashMap<String, PropertyMetaModel>();


    /**
     * Retrieves a list of meta property models for this domain type.
     * 
     * @return
     */
    public List<PropertyMetaModel> getMetaPropertyModels()
    {
        ArrayList<PropertyMetaModel> list = new ArrayList<PropertyMetaModel>(
            metaPropertyModels.values());
        return list;
    }


    /**
     * Searches for a property model with the given ID.
     * 
     * @param id The ID to search for.
     * @return Either the found {@link PropertyMetaModel} or {@code null} if no
     *         matching property model was found.
     */
    public PropertyMetaModel findPropertyModel(String id)
    {
        return metaPropertyModels.get(id);
    }


    /**
     * Adds a meta property model to this domain type model.
     * 
     * @param metaPropertyModel The meta property model to add to this domain
     *            type meta model. Must not be {@code null}.
     */
    public void addMetaPropertyModels(PropertyMetaModel metaPropertyModel)
    {
        Assert.notNull(metaPropertyModel, "The given meta property model must not be null");
        this.metaPropertyModels.put(metaPropertyModel.getId(), metaPropertyModel);
    }


    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("DomainTypeMetaModel [");
        builder.append("id=").append(getId()).append(", name=").append(getName())
            .append(", location=").append(getLocation());
        builder.append("metaPropertyModels=(");
        for (Entry<String, PropertyMetaModel> entry : metaPropertyModels.entrySet())
        {
            builder.append(entry.getKey()).append("=").append(entry.getValue());
        }

        builder.append(")]");

        return builder.toString();
    }

}
