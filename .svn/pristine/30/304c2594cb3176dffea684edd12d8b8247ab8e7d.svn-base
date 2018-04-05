package org.opensaga.plugin.release;

import java.io.Console;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.components.interactivity.DefaultOutputHandler;
import org.codehaus.plexus.components.interactivity.InputHandler;
import org.codehaus.plexus.components.interactivity.OutputHandler;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

@Component(role = Prompter.class)
public class ReleaseConfigurationPrompter
    implements Prompter
{

    private OutputHandler outputHandler = new DefaultOutputHandler();

    private InputHandler secureInputHandler = new SecurePasswordInputHandler();


    @Override
    public String prompt(String message) throws PrompterException
    {
        writePrompt(message);

        return readLine();
    }


    @Override
    public String prompt(String message, String defaultReply) throws PrompterException
    {
        writePrompt(formatMessage(message, null, defaultReply));

        String line = readLine();

        if (StringUtils.isEmpty(line))
        {
            line = defaultReply;
        }

        return line;
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public String prompt(String message, List possibleValues, String defaultReply)
        throws PrompterException
    {
        String formattedMessage = formatMessage(message, possibleValues, defaultReply);

        String line;

        do
        {
            writePrompt(formattedMessage);

            line = readLine();

            if (StringUtils.isEmpty(line))
            {
                line = defaultReply;
            }

            if (line != null && !possibleValues.contains(line))
            {
                try
                {
                    outputHandler.writeLine("Invalid selection.");
                }
                catch (IOException e)
                {
                    throw new PrompterException("Failed to present feedback", e);
                }
            }
        } while (line == null || !possibleValues.contains(line));

        return line;
    }


    @SuppressWarnings("rawtypes")
    @Override
    public String prompt(String message, List possibleValues) throws PrompterException
    {
        return prompt(message, possibleValues, null);
    }


    @Override
    public String promptForPassword(String message) throws PrompterException
    {
        writePrompt(message);

        try
        {
            return secureInputHandler.readPassword();
        }
        catch (IOException e)
        {
            throw new PrompterException("Failed to read user response", e);
        }
    }


    private String formatMessage(String message, List<String> possibleValues, String defaultReply)
    {
        StringBuffer formatted = new StringBuffer(message.length() * 2);

        formatted.append(message);

        if (defaultReply != null)
        {
            formatted.append(" ");
            formatted.append(defaultReply);
        }

        return formatted.toString();
    }


    private void writePrompt(String message) throws PrompterException
    {
        showMessage(message + ": ");
    }


    private String readLine() throws PrompterException
    {
        try
        {
            return secureInputHandler.readLine();
        }
        catch (IOException e)
        {
            throw new PrompterException("Failed to read user response", e);
        }
    }


    @Override
    public void showMessage(String message) throws PrompterException
    {
        try
        {
            outputHandler.write(message);
        }
        catch (IOException e)
        {
            throw new PrompterException("Failed to show message", e);
        }
    }

    private final class SecurePasswordInputHandler
        implements InputHandler
    {
        private Console console = System.console();


        @Override
        public String readLine() throws IOException
        {
            return console.readLine();
        }


        @Override
        public String readPassword() throws IOException
        {
            return String.valueOf(console.readPassword());
        }


        @Override
        public List<?> readMultipleLines() throws IOException
        {
            return null;
        }
    }
}
