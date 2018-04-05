package org.opensaga.plugin.builder.meta.parser.types.view;

import org.opensaga.selenium.query.ModelElementQuery;

public class TextFieldQuery
    extends AbstractQuery
{

    @Override
    public String getTag()
    {
        return "text-field";
    }


    @Override
    protected void initQuery()
    {
        addConvenienceMethod(QueryConvenientMethodTemplates.GETTER);
        addConvenienceMethod(QueryConvenientMethodTemplates.VISIBLE);
        addModelElementQuery(ComponentIdentifier.ID, ModelElementQuery.TEXT_FIELD_BY_ID);
    }
    
}
