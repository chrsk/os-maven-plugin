package org.opensaga.plugin.builder.meta.parser.types.view;

import org.opensaga.selenium.query.ModelElementQuery;

public class HelpButtonQuery
    extends AbstractQuery
{

    @Override
    public String getTag()
    {
        return "help-button";
    }


    @Override
    protected void initQuery()
    {
        addConvenienceMethod(QueryConvenientMethodTemplates.GETTER);
        addModelElementQuery(ComponentIdentifier.ID, ModelElementQuery.HELP_BUTTON_BY_ID);
    }
    
}
