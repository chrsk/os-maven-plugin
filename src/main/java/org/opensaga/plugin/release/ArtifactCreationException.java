package org.opensaga.plugin.release;

/**
 * Can be thrown whenever a artifact creation fails. The artifact creations
 * fails when the artifact couldn't be created or handled afterwards.
 * 
 * @author cklewes
 */
public class ArtifactCreationException
    extends Exception
{

    private static final long serialVersionUID = 4333035172294125888L;


    public ArtifactCreationException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public ArtifactCreationException(String message)
    {
        super(message);
    }

}
