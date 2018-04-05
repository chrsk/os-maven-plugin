package org.opensaga.plugin.builder.meta.parser.types.view;

import org.opensaga.selenium.query.ModelElementQuery;

public class LinkQuery
    extends AbstractQuery
{

    @Override
    public String getTag()
    {
        return "button";
    }


    @Override
    protected void initQuery()
    {
        addConvenienceMethod(QueryConvenientMethodTemplates.GETTER);
        addConvenienceMethod(QueryConvenientMethodTemplates.CLICKABLE);
        addModelElementQuery(ComponentIdentifier.ID, ModelElementQuery.BUTTON_BY_ID);
        addModelElementQuery(ComponentIdentifier.NAME, ModelElementQuery.BUTTON_BY_LABEL);
    }
    
}
