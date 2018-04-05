package org.opensaga.plugin.builder.meta.parser.types.view;

import org.opensaga.selenium.query.ModelElementQuery;

public class ErrorMessageListQuery
    extends AbstractQuery
{

    @Override
    public String getTag()
    {
        return "error-message-list";
    }


    @Override
    protected void initQuery()
    {
        addConvenienceMethod(QueryConvenientMethodTemplates.GETTER);
        addModelElementQuery(ComponentIdentifier.NAME, ModelElementQuery.ERROR_LIST);
    }
    
}
