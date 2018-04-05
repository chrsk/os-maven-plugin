package org.opensaga.plugin.builder.meta.parser.types.view;

import org.opensaga.selenium.components.listiterator.ListIterator;
import org.opensaga.selenium.query.ModelElementQuery;

public class ListIteratorQuery
    extends AbstractQuery
{

    @Override
    public String getTag()
    {
        return "list-iterator";
    }


    @Override
    public Class<?> getComponentType()
    {
        return ListIterator.class;
    }


    @Override
    protected void initQuery()
    {
        addConvenienceMethod(QueryConvenientMethodTemplates.GETTER);
        addModelElementQuery(ComponentIdentifier.ID, ModelElementQuery.LIST_ITERATOR);
    }
}
