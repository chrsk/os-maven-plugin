package org.opensaga.plugin.builder.meta.parser;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides an easy and simplified implementation of the OpenSAGA types. The
 * types are mapped from the OpenSAGA text represantation to the matching Java
 * type.
 * <p>
 * <strong>Warning</strong>: The GIS types are not available.
 * 
 * @author cklewes
 */
public class OpenSAGATypeMapper
{
    private static final Logger log = LoggerFactory.getLogger(OpenSAGATypeMapper.class);

    private static final Map<String, PropertyType> TYPE_MAPPING = new HashMap<String, PropertyType>()
    {
        private static final long serialVersionUID = 1L;

        {
            put("Attachment", new PropertyType("AttachmentInfo"));
            put("Binary", new PropertyType("Byte"));
            put("Boolean", new PropertyType("Boolean"));
            put("Currency", new PropertyType("BigDecimal"));
            put("Date", new PropertyType("Date"));
            put("DateTime", new PropertyType("Date"));
            put("Double", new PropertyType("Double"));
            put("Email", new PropertyType("String"));
            put("Float", new PropertyType("Float"));
            put("Integer", new PropertyType("Integer"));
            put("LargePlainText", new PropertyType("String"));
            put("LargeRichText", new PropertyType("String"));
            put("LargeWikiText", new PropertyType("String"));
            put("Locale", new PropertyType("Locale"));
            put("Long", new PropertyType("Long"));
            put("Number", new PropertyType("BigDecimal"));
            put("PlainText", new PropertyType("String"));
            put("RichText", new PropertyType("String"));
            put("Time", new PropertyType("Date"));
            put("TimeZone", new PropertyType("Date"));
            put("Url", new PropertyType("String"));
            put("WikiText", new PropertyType("String"));
        }
    };


    public static final PropertyType getType(String typeName)
    {
        if ("Point".equals(typeName) || "Polygon".equals(typeName))
        {
            log.warn("The GIS domain types are currently not supported");
            return new PropertyType("Object");
        }

        return TYPE_MAPPING.get(typeName);
    }
}
