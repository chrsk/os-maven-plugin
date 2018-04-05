package org.opensaga.plugin.builder.meta.parser.types.view;

import org.opensaga.selenium.query.ModelElementQuery;

public class SelectFieldQuery
    extends AbstractQuery
{

    @Override
    public String getTag()
    {
        return "select-field";
    }


    @Override
    protected void initQuery()
    {
        addConvenienceMethod(QueryConvenientMethodTemplates.SELECT_FIELD_GETTER);
        addModelElementQuery(ComponentIdentifier.ID, ModelElementQuery.SELECT_FIELD_BY_ID);
    }
}
