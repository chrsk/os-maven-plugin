package org.opensaga.plugin.builder.meta.parser;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_UNDERSCORE;

import org.apache.commons.lang.StringUtils;
import org.opensaga.plugin.builder.meta.HandlerType;
import org.opensaga.plugin.builder.meta.generator.model.DomainTypeMetaModel;
import org.opensaga.plugin.builder.meta.generator.model.MetaModel;
import org.opensaga.plugin.builder.meta.generator.model.ProcessMetaModel;
import org.opensaga.plugin.builder.meta.generator.model.StartStateMetaModel;
import org.opensaga.plugin.builder.meta.generator.model.ViewComponentMetaModel;
import org.opensaga.plugin.builder.meta.generator.model.ViewMetaModel;
import org.opensaga.plugin.builder.meta.generator.model.ViewStateMetaModel;

public class HandlerTypeBasedJavaNamingStrategy
    implements ModelNamingStrategy
{

    private static final String CLASS_NAME_BASE = "MetaModel";

    private final HandlerType type;


    public HandlerTypeBasedJavaNamingStrategy(HandlerType type)
    {
        this.type = type;
    }


    @Override
    public String getJavaConstantNameForMetaModel(MetaModel metaModel)
    {
        String name = metaModel.getName();
        name = replaceSpecialCharacter(name);
        
        if (name.length() == 2)
        {
            return name.toUpperCase();
        }
        else if (name.toUpperCase().equals(name))
        {
            // Nothing more to do here.
            return name;
        }

        final String UNDERSCORE = "_";
        if (name.contains(UNDERSCORE))
        {
            return LOWER_UNDERSCORE.to(UPPER_UNDERSCORE, name);
        }
        else
        {
            return LOWER_CAMEL.to(UPPER_UNDERSCORE, name);
        }
    }

    @Override
    public String getJavaClassNameForDomainTypeModel(DomainTypeMetaModel domainTypeMetaModel)
    {
        return getStringAsJavaClassName(domainTypeMetaModel.getName()) + type.getName() + CLASS_NAME_BASE;
    }


    @Override
    public String getJavaConstantNameForDomainTypeModel(DomainTypeMetaModel domainTypeMetaModel)
    {
        return getStringAsJavaConstant(domainTypeMetaModel.getName());
    }


    @Override
    public String getJavaClassNameForProcessModel(ProcessMetaModel processMetaModel)
    {
        return getDefaultClassNameFromModelWithPrefix(processMetaModel, "p_") + "ProcessMetaModel";
    }
    
    
    @Override
    public String getJavaClassNameForViewWithProcess(ViewMetaModel viewMetaModel, ProcessMetaModel processMetaModel)
    {
        final String processModelName = getDefaultClassNameFromModelWithPrefix(processMetaModel, "p_");
        return processModelName + getJavaClassNameForViewModel(viewMetaModel);
    }


    @Override
    public String getJavaConstantNameForProcessModel(ProcessMetaModel processMetaModel)
    {
        return getDefaultConstantNameFromModelWithPrefix(processMetaModel, "p_");
    }


    @Override
    public String getJavaConstantNameForStartState(StartStateMetaModel startStateMetaModel)
    {
        return getDefaultConstantNameFromModelWithPrefix(startStateMetaModel, "ss_");
    }
    
    @Override
    public String getJavaConstantNameForViewState(ViewStateMetaModel metaModel)
    {
        return getDefaultConstantNameFromModelWithPrefix(metaModel, "vs_");
    }

    @Override
    public String getJavaClassNameForViewState(ViewStateMetaModel metaModel)
    {
        final ViewMetaModel viewMetaModel = new ViewMetaModel();
        viewMetaModel.setId(metaModel.getViewReference());
        return getDefaultClassNameFromModelWithPrefix(viewMetaModel, "p_", "v_") + "View";
    }
    
    @Override
    public String getJavaClassNameForViewModel(ViewMetaModel metaModel)
    {
        return getDefaultClassNameFromModelWithPrefix(metaModel, "p_", "v_") + "View";
    }

    
    public String getJavaVariableNameForComponent(ViewComponentMetaModel metaModel)
    {
        String identifier = metaModel.getIdentifier();
        
        if(StringUtils.contains(identifier, "v_"))
        {
            final int indexOfViewEnd = StringUtils.indexOf(identifier, '.', StringUtils.indexOf(identifier, "v_"));
            identifier = StringUtils.substring(identifier, indexOfViewEnd);
        }
        
        return StringUtils.uncapitalize(getStringAsJavaClassName(metaModel.getTagName())) + getStringAsJavaClassName(identifier);
    }


    protected String getDefaultConstantNameFromModelWithPrefix(MetaModel metaModel, String prefix)
    {
        if (StringUtils.isNotEmpty(metaModel.getName()))
        {
            return getStringAsJavaConstant(metaModel.getName());
        }
        
        String processId = metaModel.getId();
        
        if(processId.contains(prefix))
        {
            processId = processId.substring(processId.indexOf(prefix) + prefix.length());
        }
        
        return getStringAsJavaConstant(processId);
    }

    protected String getDefaultClassNameFromModelWithPrefix(MetaModel metaModel, String prefix, String removePart)
    {
        String modelName = metaModel.getName();
        
        if (StringUtils.isNotEmpty(modelName))
        {
            if(StringUtils.isNotEmpty(removePart))
            {
                modelName = modelName.replaceFirst(removePart, "");
            }
            return getStringAsJavaClassName(modelName);
        }
        
        String processId = metaModel.getId();
        
        if(processId.contains(prefix))
        {
            if(StringUtils.isNotEmpty(removePart))
            {
                processId = processId.replaceFirst(removePart, "");
            }
            processId = processId.substring(processId.indexOf(prefix) + prefix.length());
        }
        
        return getStringAsJavaClassName(processId);
    }

    protected String getDefaultClassNameFromModelWithPrefix(MetaModel metaModel, String prefix)
    {
        return getDefaultClassNameFromModelWithPrefix(metaModel, prefix, null);
    }
    

    protected String getStringAsJavaClassName(String string)
    {
        String representativeString;
        final String UNDERSCORE = "_";
        
        string = replaceSpecialCharacter(string);

        if (string.contains(UNDERSCORE))
        {
            representativeString = LOWER_UNDERSCORE.to(UPPER_CAMEL, string);
        }
        else
        {
            representativeString = LOWER_CAMEL.to(UPPER_CAMEL, string);
        }

        return representativeString;
    }


    protected String getStringAsJavaConstant(String string)
    {
        String representativeString;
        final String UNDERSCORE = "_";

        string = replaceSpecialCharacter(string);
        
        if (string.contains(UNDERSCORE))
        {
            representativeString = LOWER_UNDERSCORE.to(UPPER_UNDERSCORE, string);
        }
        else
        {
            representativeString = LOWER_CAMEL.to(UPPER_UNDERSCORE, string);
        }

        return representativeString;
    }


    protected String replaceSpecialCharacter(String string)
    {
        string = string.replace(".", "_");
        string = string.replace(" ", "");
        string = string.replace("-", "_");
        string = string.replace("/", "_");
        string = string.replace("(", "_");
        string = string.replace(")", "_");
        
        if(Character.isDigit(string.substring(0, 1).toCharArray()[0]))
        {
             string = "A" + string; 
        }
        
        return string;
    }

}
