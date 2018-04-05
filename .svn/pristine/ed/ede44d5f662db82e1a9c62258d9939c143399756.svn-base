package org.opensaga.plugin.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.opensaga.plugin.util.Iterate.Index;

public class NameUtils
{
    public static String toJavaClassName(String text)
    {
        final StringBuilder result = new StringBuilder(text.length());
        text = text.replaceAll("-", "");
        String[] words = text.split("\\s");
        
        for (Index<String> word : Iterate.over(words))
        {
            result.append(Character.toUpperCase(words[word.index].charAt(0))).append(word.value.substring(1));
        }
        
        return result.toString();
    }
    
    public static String toLowerUnderscore(String name)
    {
        List<String> wordList = new ArrayList<String>();
        String currentWord = "";

        for (char character : name.toCharArray())
        {
            if (Character.isUpperCase(character))
            {
                if (!currentWord.isEmpty())
                {
                    wordList.add(currentWord);
                }
                currentWord = String.valueOf(character);
            }
            else
            {
                currentWord += character;
            }
        }

        wordList.add(currentWord);
        StringBuilder output = new StringBuilder();

        boolean previousWordWasSingleCharacter = true;
        for (String word : wordList)
        {
            if (word.length() == 1)
            {
                if (!previousWordWasSingleCharacter)
                {
                    output.append("_");
                }
                
                previousWordWasSingleCharacter = true;
                output.append(word.toLowerCase());
            }
            else
            {
                if (output.length() > 0)
                {
                    output.append("_");
                }
                previousWordWasSingleCharacter = false;
                output.append(word.toLowerCase());
            }
        }
        return output.toString();
    }
    
    public static String toUpperCamelCase(String name)
    {
        String normalizedName = replaceSeparatorsWithDash(name);
        String[] words = normalizedName.split("-");
        StringBuilder nameBuilder = new StringBuilder(normalizedName.length());

        for (Iterate.Index<String> word : Iterate.over(words))
        {
            if(StringUtils.isNotBlank(words[word.index])) {
                nameBuilder.append(words[word.index].substring(0, 1).toUpperCase());
                nameBuilder.append(words[word.index].substring(1));
            }
        }

        return nameBuilder.toString();
    }
    
    private static String replaceSeparatorsWithDash(String value)
    {
        String stringWithDash = value.replaceAll("_", "-");

        if (stringWithDash.toCharArray()[0] == '-')
        {
            return stringWithDash.replaceFirst("-", "");
        }

        return stringWithDash;
    }
}
