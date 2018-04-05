package org.opensaga.plugin.builder.meta.parser.types.view;

import org.opensaga.selenium.components.tabset.TabSetComponent;
import org.opensaga.selenium.query.ModelElementQuery;

public class TabSetQuery
    extends AbstractQuery
{

    @Override
    public String getTag()
    {
        return "tab-set";
    }


    @Override
    public Class<?> getComponentType()
    {
        return TabSetComponent.class;
    }


    @Override
    protected void initQuery()
    {
        addConvenienceMethod(QueryConvenientMethodTemplates.GETTER);
        addModelElementQuery(ComponentIdentifier.ID, ModelElementQuery.TABSET_BY_ID);
    }
}
