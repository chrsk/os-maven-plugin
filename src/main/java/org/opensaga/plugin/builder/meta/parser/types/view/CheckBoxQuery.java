package org.opensaga.plugin.builder.meta.parser.types.view;

import org.opensaga.selenium.query.ModelElementQuery;

public class CheckBoxQuery
    extends AbstractQuery
{

    @Override
    public String getTag()
    {
        return "checkbox";
    }


    @Override
    protected void initQuery()
    {
        addConvenienceMethod(QueryConvenientMethodTemplates.GETTER);
        addConvenienceMethod(QueryConvenientMethodTemplates.CLICKABLE);
        addModelElementQuery(ComponentIdentifier.ID, ModelElementQuery.CHECK_BOX_BY_ID);
    }
    
}
