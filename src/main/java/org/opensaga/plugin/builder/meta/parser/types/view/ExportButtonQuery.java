package org.opensaga.plugin.builder.meta.parser.types.view;

import org.opensaga.selenium.query.ModelElementQuery;

public class ExportButtonQuery
    extends AbstractQuery
{

    @Override
    public String getTag()
    {
        return "export-button";
    }


    @Override
    protected void initQuery()
    {
        addConvenienceMethod(QueryConvenientMethodTemplates.GETTER);
        addConvenienceMethod(QueryConvenientMethodTemplates.CLICKABLE);
        addConvenienceMethod(QueryConvenientMethodTemplates.VISIBLE);
        addModelElementQuery(ComponentIdentifier.ID, ModelElementQuery.BUTTON_BY_ID);
    }
    
}
