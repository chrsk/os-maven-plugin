package org.opensaga.plugin.builder.meta.parser.types.view;

import org.opensaga.selenium.components.datagrid.DataGrid;
import org.opensaga.selenium.query.ModelElementQuery;

public class DataGridQuery
    extends AbstractQuery
{

    @Override
    public String getTag()
    {
        return "datagrid";
    }


    @Override
    public Class<?> getComponentType()
    {
        return DataGrid.class;
    }


    @Override
    protected void initQuery()
    {
        addConvenienceMethod(QueryConvenientMethodTemplates.GETTER);
        addModelElementQuery(ComponentIdentifier.NAME, ModelElementQuery.DATAGRID_BY_HEADING);
    }
}
