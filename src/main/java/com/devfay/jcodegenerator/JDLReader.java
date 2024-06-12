package com.devfay.jcodegenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class JDLReader {

    public static Map<String, String> readJDL(String filePath) throws IOException {
        Map<String, String> entities = new HashMap<>();
        String content = new String(Files.readAllBytes(Paths.get(filePath)));

        String[] parts = content.split("entity ");
        for (String part : parts) {
            if (part.trim().isEmpty()) continue;
            String entityName = part.substring(0, part.indexOf('{')).trim();
            String entityBody = part.substring(part.indexOf('{') + 1, part.indexOf('}')).trim();
            entities.put(entityName, entityBody);
        }

        return entities;
    }

    public static Map<String, String> readRelationships(String filePath) throws IOException {
        Map<String, String> relationships = new HashMap<>();
        String content = new String(Files.readAllBytes(Paths.get(filePath)));

        String[] parts = content.split("relationship ");
        for (String part : parts) {
            if (part.trim().isEmpty()) continue;
            String relationshipType = part.substring(0, part.indexOf('{')).trim();
            String relationshipBody = part.substring(part.indexOf('{') + 1, part.indexOf('}')).trim();
            relationships.put(relationshipType, relationshipBody);
        }

        return relationships;
    }
}
