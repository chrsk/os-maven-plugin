package org.opensaga.plugin.builder.meta.parser.types.view;

import org.opensaga.selenium.query.ModelElementQuery;

public class RefreshButtonQuery
    extends AbstractQuery
{

    @Override
    public String getTag()
    {
        return "refresh-button";
    }


    @Override
    protected void initQuery()
    {
        addConvenienceMethod(QueryConvenientMethodTemplates.GETTER);
        addConvenienceMethod(QueryConvenientMethodTemplates.CLICKABLE);
        addConvenienceMethod(QueryConvenientMethodTemplates.VISIBLE);
        addModelElementQuery(ComponentIdentifier.NAME, ModelElementQuery.BUTTON_BY_LABEL);
    }
    
}
