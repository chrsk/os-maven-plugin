package org.opensaga.plugin.builder.meta.generator.model;

import org.opensaga.plugin.builder.meta.parser.TypeBasedModelParser;


/**
 * Describes a meta model which is a representation of an XML OpenSAGA model.
 * This meta model is used to generate so called meta classes. Which are
 * dynamically generated java files to improve the programming capabilities of
 * OpenSAGA.
 * <p>
 * A meta model must implement at least the ID, name and location. All further
 * properties are stored in the subclasses of this interface.
 * 
 * @author cklewes
 */
public interface EditableMetaModel extends MetaModel
{

   
    /**
     * Defines the parser which parsed the model candidate into this meta model.
     * 
     * @return The parser of this meta model.
     */
    void setParser(TypeBasedModelParser parser);

}
