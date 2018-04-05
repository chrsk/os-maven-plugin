package org.opensaga.plugin.builder.meta.parser.types.view;

import org.opensaga.selenium.query.ModelElementQuery;

public class ButtonQuery
    extends AbstractQuery
{

    @Override
    public String getTag()
    {
        return "link";
    }


    @Override
    protected void initQuery()
    {
        addConvenienceMethod(QueryConvenientMethodTemplates.GETTER);
        addConvenienceMethod(QueryConvenientMethodTemplates.CLICKABLE);
        addModelElementQuery(ComponentIdentifier.NAME, ModelElementQuery.LINK_BY_LABEL);
    }
    
}
