package org.opensaga.plugin.release;

import java.util.Properties;

import org.codehaus.plexus.components.interactivity.PrompterException;

public interface ReleaseConfigurator
{

    ReleaseConfiguration configureRelease(ReleaseRequest releaseRequest, Boolean interactiveMode, Properties userProperties) throws PrompterException;
    
}
