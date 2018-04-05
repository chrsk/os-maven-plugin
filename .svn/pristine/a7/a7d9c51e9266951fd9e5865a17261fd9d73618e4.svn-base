package org.opensaga.plugin.release.facettes;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractFacette
    implements Facette
{

    private Map<String, Object> aspects = new HashMap<String, Object>();
    
    @Override
    public Map<String, Object> getAspects()
    {
        return aspects;
    }
    
    public void setAspects(Map<String, Object> aspects)
    {
        this.aspects = aspects;
    }
    
    public void addAspect(String key, Object value)
    {
        this.aspects.put(key, value);
    }

}
