package org.opensaga.plugin.builder.meta.parser.types.view;

import org.opensaga.selenium.components.multiconnect.MultiConnect;
import org.opensaga.selenium.query.ModelElementQuery;

public class MultiConnectQuery
    extends AbstractQuery
{

    @Override
    public String getTag()
    {
        return "multi-connect";
    }

    @Override
    public Class<?> getComponentType()
    {
        return MultiConnect.class;
    }

    @Override
    protected void initQuery()
    {
        addConvenienceMethod(QueryConvenientMethodTemplates.GETTER);
        addModelElementQuery(ComponentIdentifier.ID, ModelElementQuery.MULTI_CONNECT_BY_ID);
    }
    
}
