package org.opensaga.plugin.builder.meta.generator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The domain meta model class provides access to all domain type meta models
 * and all relations found in this domain. The domain meta model class
 * represents a whole logic domain. In example this can be the OpenSAGA domain,
 * or a specific domain of a business project.
 * 
 * @author cklewes
 */
public class DomainMetaModel
{

    private List<DomainTypeMetaModel> domainTypeInfos = new ArrayList<DomainTypeMetaModel>();

    private List<RelationMetaModel> relationInfos = new ArrayList<RelationMetaModel>();

    private final String className;


    /**
     * Creates a domain meta model for the given domain name.
     * 
     * @param domainName The domain name for this domain meta model, may be
     *            {@code null}.
     */
    public DomainMetaModel(String domainName)
    {
        this.className = domainName;
    }


    /**
     * Defines the relation informations of this domain meta model. Defined
     * should be all relations which are attached to this domain.
     * 
     * @param relationInfos A list of all relations attached to this domain.
     */
    public void setRelationInfos(List<RelationMetaModel> relationInfos)
    {
        this.relationInfos = relationInfos;
    }


    /**
     * Retrieves the relation informations of this domain meta model. Defined
     * should be all relations which are attached to this domain.
     * 
     * @return A list of all attached relations.
     */
    public List<RelationMetaModel> getRelationInfos()
    {
        return relationInfos;
    }


    /**
     * Defines a list of domain type meta models which are attached to this
     * domain.
     * 
     * @param domainTypeInfos A list of all attached domain type meta models
     */
    public void setDomainTypeInfos(List<DomainTypeMetaModel> domainTypeInfos)
    {
        this.domainTypeInfos = domainTypeInfos;
    }


    /**
     * Retrieves the list of domain type meta models which are attached to this
     * domain. return list of all attached domain type meta models
     */
    public List<DomainTypeMetaModel> getDomainTypeInfos()
    {
        return domainTypeInfos;
    }


    /**
     * Retrieves the domain name of this domain meta model.
     * 
     * @return
     */
    public String getClassName()
    {
        return className;
    }
}
