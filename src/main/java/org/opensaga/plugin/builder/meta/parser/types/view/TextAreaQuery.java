package org.opensaga.plugin.builder.meta.parser.types.view;

import org.opensaga.selenium.query.ModelElementQuery;

public class TextAreaQuery
    extends AbstractQuery
{

    @Override
    public String getTag()
    {
        return "textarea";
    }


    @Override
    protected void initQuery()
    {
        addConvenienceMethod(QueryConvenientMethodTemplates.GETTER);
        addModelElementQuery(ComponentIdentifier.ID, ModelElementQuery.TEXT_AREA_BY_ID);
    }
    
}
