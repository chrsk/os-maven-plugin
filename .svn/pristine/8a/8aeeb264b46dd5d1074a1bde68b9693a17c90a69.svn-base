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
public interface MetaModel
{

    /**
     * Retrieves the ID of the meta model. The ID is not coercively unique. The
     * ID is not mandatory for a meta model.
     * 
     * @return The ID of this meta model, may {@code null}.
     */
    String getId();


    /**
     * The name of the meta model if there's one.
     * 
     * @return The name of this meta model, may {@code null}.
     */
    String getName();


    /**
     * The location of this meta model. The location is the XML file location
     * which this meta model represents. When the meta model is not a top level
     * XML model itself the direct parent location is returned.
     * 
     * @return  The location of this meta model.
     */
    String getLocation();
    
    /**
     * Retrieves the parser which parsed the model candidate into this meta model.
     * 
     * @return The parser of this meta model.
     */
    TypeBasedModelParser getParser();

}
