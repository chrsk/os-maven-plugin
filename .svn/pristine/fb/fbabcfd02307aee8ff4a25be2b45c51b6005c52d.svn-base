package org.opensaga.plugin.builder.meta.generator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides access to the process and the contained states of this process. The
 * process meta model provides so far {@link ViewStateMetaModel}s and
 * {@link StartStateMetaModel}s.
 * 
 * @author cklewes
 */
public class ProcessMetaModel
    extends AbstractMetaModel
{

    private List<StartStateMetaModel> startStateMetaModels = new ArrayList<StartStateMetaModel>();

    private List<ViewStateMetaModel> viewStateMetaModels = new ArrayList<ViewStateMetaModel>();


    /**
     * Defines a list of start state meta models which occurs in this process.
     * 
     * @param startStateMetaModels A list of start state models
     */
    public void setStartStateMetaModels(List<StartStateMetaModel> startStateMetaModels)
    {
        this.startStateMetaModels = startStateMetaModels;
    }


    /**
     * Retrieves a list of start state meta models which occurs in this process.
     * 
     * @return A list of start state models
     */
    public List<StartStateMetaModel> getStartStateMetaModels()
    {
        return startStateMetaModels;
    }


    /**
     * Retrieves a list of view state meta models which occurs in this process.
     * 
     * @return A list of view state models
     */
    public List<ViewStateMetaModel> getViewStateMetaModels()
    {
        return viewStateMetaModels;
    }


    /**
     * Defines a list of view state meta models which occurs in this process.
     * 
     * @param viewStateMetaModels A list of view state models
     */
    public void setViewStateMetaModels(List<ViewStateMetaModel> viewStateMetaModels)
    {
        this.viewStateMetaModels = viewStateMetaModels;
    }


    /**
     * Verifies if this process a view state which references the given view
     * reference.
     * 
     * @param viewReference The view reference to check for.
     * @return Either {@code true} if the view reference is contained in on of
     *         the view state models or {@code false} if not.
     */
    public boolean containsViewStateModelWithViewReference(String viewReference)
    {
        for (ViewStateMetaModel viewStateMetaModel : getViewStateMetaModels())
        {
            if (viewStateMetaModel.getViewReference().equals(viewReference))
            {
                return true;
            }
        }

        return false;
    }

    
    /**
     * Returns <code>true</code> if the given start state ID is already contained
     * within the start state list.
     * 
     * @param startStateId            the start state ID
     * @return
     */

    public boolean containsStartState(String startStateId)
    {
        for (StartStateMetaModel startStateMetaModel : getStartStateMetaModels())
        {
            if (startStateId.equals(startStateMetaModel.getId()))
            {
                return true;
            }
        }
        return false;
    }


    /**
     * Returns <code>true</code> if the given view state ID is already contained
     * within the start state list.
     * 
     * @param viewStateId            the view state ID
     * @return
     */

    public boolean containsViewState(String viewStateId)
    {
        for (ViewStateMetaModel viewStateMetaModel : getViewStateMetaModels())
        {
            if (viewStateId.equals(viewStateMetaModel.getId()))
            {
                return true;
            }
        }
        return false;
    }
}
