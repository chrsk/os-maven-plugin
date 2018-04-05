package org.opensaga.plugin.builder.meta.parser.types.view;

import org.opensaga.selenium.components.catselect.CategorySelectComponent;
import org.opensaga.selenium.query.ModelElementQuery;

public class CategorySelectQuery
    extends AbstractQuery
{

    @Override
    public String getTag()
    {
        return "category-select-field";
    }

    @Override
    public Class<?> getComponentType()
    {
        return CategorySelectComponent.class;
    }

    @Override
    protected void initQuery()
    {
        addConvenienceMethod(QueryConvenientMethodTemplates.GETTER);
        addModelElementQuery(ComponentIdentifier.ID, ModelElementQuery.CATEGORY_SELECT_BY_ID);
    }
    
}
