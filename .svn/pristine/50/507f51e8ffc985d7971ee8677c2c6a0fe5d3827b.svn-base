package org.opensaga.plugin.builder.meta.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opensaga.plugin.builder.meta.generator.model.MetaModel;

/**
 * The handler context is a global context which collects retrieved meta models
 * from all handlers. The context contains a list of meta models which are
 * produced by the handler.
 * 
 * @see HandlerConstants
 * @author cklewes
 */
public class HandlerContext
{

    private Map<String, List<MetaModel>> context = new HashMap<String, List<MetaModel>>();

    /**
     * Retrieves the list of meta models for the given handler ID.
     * 
     * @param id The ID of the handler to retrieve the context from.
     * 
     * @see HandlerConstants
     * @return
     */
    public List<MetaModel> getContextForHandlerWithId(String id)
    {
        return context.get(id);
    }


    /**
     * Adds a list of meta models to this context for the specific handler ID.
     * 
     * @param id The ID of the handler.
     * @param metaModels The list of meta models procuded by the handler.
     */
    public void addContextForHandlerWithId(String id, List<MetaModel> metaModels)
    {
        if (context.containsKey(id))
        {
            context.get(id).addAll(metaModels);
        }
        else
        {
            context.put(id, metaModels);
        }
    }

}
