package org.opensaga.plugin.builder.meta.generator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes the OpenSAGA view model. Provides access to all view components
 * contained in this view.
 * 
 * @author cklewes
 */
public class ViewMetaModel
    extends AbstractMetaModel
{

    private List<ViewComponentMetaModel> componentMetaModels = new ArrayList<ViewComponentMetaModel>();


    /**
     * Retrieves a list of all contained view components.
     * 
     * @return A list of view components
     */
    public List<ViewComponentMetaModel> getComponentMetaModels()
    {
        return componentMetaModels;
    }


    /**
     * Defines a list of view components which are contained in this view.
     * 
     * @param componentMetaModels A list of component meta models, must not be {@code null}.
     */
    public void setComponentMetaModels(List<ViewComponentMetaModel> componentMetaModels)
    {
        this.componentMetaModels = componentMetaModels;
    }

}
