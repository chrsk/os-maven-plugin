package org.opensaga.plugin.builder.meta.generator.model;

/**
 * Describes an OpenSAGA view state model contained in a process.
 * 
 * @author cklewes
 */
public class ViewStateMetaModel
    extends AbstractMetaModel
{

    private String viewReference;


    /**
     * Retrieves the view reference of this view state model.
     * 
     * @return The view reference.
     */
    public String getViewReference()
    {
        return viewReference;
    }


    /**
     * Defines the view reference.
     * 
     * @param viewReference The view reference
     */
    public void setViewReference(String viewReference)
    {
        this.viewReference = viewReference;
    }

}
