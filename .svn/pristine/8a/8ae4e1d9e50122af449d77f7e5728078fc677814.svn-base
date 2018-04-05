package org.opensaga.plugin.builder.meta;

import java.text.MessageFormat;

public class MissingDependencyException
    extends RuntimeException
{
    private static final long serialVersionUID = -181836553036370600L;

    public MissingDependencyException(String groupId, String artifactId)
    {
        super(MessageFormat.format("The actual configuration of the 'opensaga-maven-plugin' requires " +
            "the direct dependency of {0}:{1}. Please add this dependency to your project configuration.", groupId, artifactId));
    }

}
