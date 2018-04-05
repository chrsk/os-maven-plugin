package org.opensaga.plugin.builder.meta.parser.types.view;

import org.opensaga.selenium.components.radio.RadioButtons;
import org.opensaga.selenium.query.ModelElementQuery;

public class RadioButtonsQuery
    extends AbstractQuery
{

    @Override
    public String getTag()
    {
        return "radio-buttons";
    }

    @Override
    public Class<?> getComponentType()
    {
        return RadioButtons.class;
    }


    @Override
    protected void initQuery()
    {
        addConvenienceMethod(QueryConvenientMethodTemplates.GETTER);
        addModelElementQuery(ComponentIdentifier.ID, ModelElementQuery.RADIO_BUTTONS_WIDGET_BY_ID);
    }
}
