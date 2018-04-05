package org.opensaga.plugin.release.facettes;

import java.util.Map;

public interface Facette
{

    void addAspect(String key, Object value);


    void setAspects(Map<String, Object> aspects);


    Map<String, Object> getAspects();

}
