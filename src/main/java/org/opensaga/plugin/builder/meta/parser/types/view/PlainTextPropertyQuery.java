package org.opensaga.plugin.builder.meta.parser.types.view;

import org.opensaga.selenium.query.ModelElementQuery;

public class PlainTextPropertyQuery
    extends AbstractQuery
{

    @Override
    public String getTag()
    {
        return "plain-text-property";
    }


    @Override
    protected void initQuery()
    {
        addConvenienceMethod(QueryConvenientMethodTemplates.GETTER);
        addModelElementQuery(ComponentIdentifier.ID, ModelElementQuery.ELEMENT_BY_ID);
    }
    
}
