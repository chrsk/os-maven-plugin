package org.opensaga.plugin.release;

import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;

public class MavenClassLoadingUtil
{
    @SuppressWarnings("unchecked")
    public static <I> I instanceClassImplementingInterface(String className, Class<I> implementingInterface)
    {
        if(StringUtils.isEmpty(className))
        {
            return null;
        }
        
        try
        {
            final Class<?> artifactHandlerClass = Class.forName(className);
            final List<Class<?>> interfaces = ClassUtils.getAllInterfaces(artifactHandlerClass);
            
            for (Class<?> artifactHandlerInterface : interfaces)
            {
                if(artifactHandlerInterface.isAssignableFrom(implementingInterface))
                {
                    return (I) artifactHandlerClass.newInstance();
                }
            }
            
            throw new IllegalStateException("The class '" +  className + "' does not implements " + implementingInterface.getName());
        }
        catch (Exception e)
        {
            throw new IllegalStateException("Failed to create artifact handler instance", e);
        }
    }
}
