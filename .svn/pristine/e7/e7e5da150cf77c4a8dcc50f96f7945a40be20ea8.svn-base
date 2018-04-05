package org.opensaga.plugin.builder.meta.generator.model;

import org.opensaga.selenium.query.ModelElementQuery;
import org.opensaga.plugin.builder.meta.parser.types.view.ComponentIdentifier;
import org.opensaga.plugin.builder.meta.parser.types.view.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Describes an OpenSAGA view component. A view component is always contained in
 * a {@link ViewMetaModel}. The view component meta model contains several
 * informations about their way to identify. We use this mainly to generate view
 * java files which provide convenient access to selectors used by web
 * integration tests.
 * 
 * @author cklewes
 */
public class ViewComponentMetaModel
    extends AbstractMetaModel
{

    private static final Logger log = LoggerFactory.getLogger(ViewComponentMetaModel.class);

    private Query query;

    private String tagName;

    private ComponentIdentifier componentIdentifier;

    private String identifier;


    /**
     * Retrieves if this view component model is identifiable.
     * 
     * @see ComponentIdentifier
     * @return Returns {@code true} when the component is identifiable
     *         {@code false} otherwise.
     */
    public boolean isIdentifiable()
    {
        if (componentIdentifier != null)
        {
            return true;
        }

        // We don't know anything about this component, therefore
        // we log a warning to alert the developer.
        log.info("Unidentifiable component of type '{}' in file '{}'", getTagName(), getLocation());
        return false;
    }


    /**
     * Defines the tag name for this view component meta model. The tag name is
     * the name of the XML node which represents this view component meta model.
     * 
     * @param nodeName The node name of the XML node.
     */
    public void setTagName(String nodeName)
    {
        this.tagName = nodeName;
    }


    /**
     * Retrieves the tag name of this view component.
     * 
     * @see #setTagName(String)
     * @return
     */
    public String getTagName()
    {
        return tagName;
    }


    /**
     * Retrieves the query for this view component model. A query is the
     * selector of this view component. A query selector describes the way how
     * this view component can be identified in a unique way in their view.
     * 
     * @return Either the query of this view component or {@code null}.
     */
    public Query getQuery()
    {
        return query;
    }


    /**
     * Defines the query for this view component model
     * 
     * @see #getQuery()
     * @param query
     */
    public void setQuery(Query query)
    {
        this.query = query;
    }


    /**
     * Retrieves the component identifier. A component identifier describes the
     * way this view component meta model is identified.
     * 
     * @return  Either the defined {@link ComponentIdentifier} or {@code null}.
     */
    public ComponentIdentifier getComponentIdentifier()
    {
        return componentIdentifier;
    }


    /**
     * Retrieves the model element query from the given query. The model element
     * query is the real implementation of the selector for this view component.
     * 
     * @see #getQuery()
     * @see #setTagName(String)
     * @return
     */
    public ModelElementQuery getModelElementQuery()
    {
        return getQuery().getModelElementQuery(getComponentIdentifier());
    }


    /**
     * Retrieves the real identifier of this view component.
     * 
     * @see #identifier
     * @return
     */
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Defines the component identifier and the real identifier, which can
     * represent every attribute of the XML model which has an identifying
     * character.
     * 
     * @see #getComponentIdentifier()
     * @param componentIdentifier The component identifier, describes which type
     *            of identifier was found.
     * @param identifier The real identifier value, most likely a attribute of a
     *            value.
     */
    public void setIdentifier(ComponentIdentifier componentIdentifier, String identifier)
    {
        this.componentIdentifier = componentIdentifier;
        this.identifier = identifier;
    }


    @Override
    public String toString()
    {
        return String.format(
            "View component [query=%s, tagName=%s, componentIdentifier=%s, identifier=%s]", query,
            tagName, componentIdentifier, identifier);
    }


    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
        result = prime * result + ((tagName == null) ? 0 : tagName.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ViewComponentMetaModel other = (ViewComponentMetaModel) obj;
        if (identifier == null)
        {
            if (other.identifier != null)
                return false;
        }
        else if (!identifier.equals(other.identifier))
            return false;
        if (tagName == null)
        {
            if (other.tagName != null)
                return false;
        }
        else if (!tagName.equals(other.tagName))
            return false;
        return true;
    }

}
