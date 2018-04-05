package org.opensaga.plugin.builder.meta.parser.types.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebElement;
import org.opensaga.selenium.query.ModelElementQuery;

public abstract class AbstractQuery
    implements Query
{

    private Map<ComponentIdentifier, ModelElementQuery> modelElementQueries = new HashMap<ComponentIdentifier, ModelElementQuery>();

    private final List<QueryConvenientMethodTemplates> methodList = new ArrayList<QueryConvenientMethodTemplates>();

    public AbstractQuery()
    {
        initQuery();
    }

    @Override
    public Class<?> getComponentType()
    {
        return WebElement.class;
    }


    protected final void addModelElementQuery(ComponentIdentifier componentIdentifier,
        ModelElementQuery modelElementQuery)
    {
        modelElementQueries.put(componentIdentifier, modelElementQuery);
    }


    protected final void addConvenienceMethod(QueryConvenientMethodTemplates method)
    {
        methodList.add(method);
    }


    protected abstract void initQuery();


    @Override
    public ModelElementQuery getModelElementQuery(ComponentIdentifier componentIdentifier)
    {
        return modelElementQueries.get(componentIdentifier);
    }


    @Override
    public List<QueryConvenientMethodTemplates> getConvenientMethods()
    {
        return methodList;
    }


    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Query [");
        
        builder.append("tag = [");
        builder.append(getTag());
        builder.append("], ");
        
        if (modelElementQueries != null)
        {
            builder.append("queries = [");
            
            for (Map.Entry<ComponentIdentifier, ModelElementQuery> entry : modelElementQueries.entrySet())
            {
                
                builder.append("{");
                builder.append(entry.getKey());
                builder.append(":");
                builder.append(entry.getValue());
                builder.append("},");
            }
            
            builder.append("], ");
        }
        if (methodList != null)
        {
            builder.append("methods = [");
            
            for (QueryConvenientMethodTemplates method : methodList)
            {
                builder.append("{");
                builder.append(method);
                builder.append("},");
            }
            builder.append("], ");
        }
        if (getComponentType() != null)
        {
            builder.append("type = ");
            builder.append(getComponentType());
            builder.append(", ");
        }
      
        builder.append("]");
        return builder.toString();
    }

}
