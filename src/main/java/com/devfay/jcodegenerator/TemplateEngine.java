package com.devfay.jcodegenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Map;

public class TemplateEngine {
    private static final String ENTITY_TEMPLATE_PATH = "src/main/resources/templates/EntityTemplate.java";
    private static final String REPOSITORY_TEMPLATE_PATH = "src/main/resources/templates/RepositoryTemplate.java";

    public static void generateEntity(String entityName, String entityBody, Map<String, String> relationships) throws IOException {
        String template = new String(Files.readAllBytes(Paths.get(ENTITY_TEMPLATE_PATH)));
        String classContent = template.replace("{{ENTITY_NAME}}", entityName)
                .replace("{{ENTITY_BODY}}", processEntityBody(entityName, entityBody, relationships));

        Path entityPath = Paths.get("src/main/java/com/example/jdlparser/entities");
        if (!Files.exists(entityPath)) {
            Files.createDirectories(entityPath);
        }

        Files.write(Paths.get(entityPath.toString(), entityName + ".java"), classContent.getBytes());
    }

    private static String processEntityBody(String entityName, String entityBody, Map<String, String> relationships) {
        StringBuilder processedBody = new StringBuilder();

        // Add fields from entity body
        String[] fields = entityBody.split(",");
        for (String field : fields) {
            String[] parts = field.trim().split(" ");
            String fieldName = parts[0];
            String fieldType = mapJDLTypeToJavaType(parts[1]);
            boolean isRequired = parts.length > 2 && parts[2].equals("required");

            if (isRequired) {
                processedBody.append("    @NotNull\n");
            }

            processedBody.append("    private ")
                    .append(fieldType)
                    .append(" ")
                    .append(fieldName)
                    .append(";\n");
        }

        // Add relationships
        for (Map.Entry<String, String> entry : relationships.entrySet()) {
            String relationshipType = entry.getKey();
            String relationshipBody = entry.getValue();
            String[] relations = relationshipBody.split(",");
            for (String relation : relations) {
                String[] relationParts = relation.trim().split(" to ");
                if (relationParts.length < 2) continue;  // Skip invalid relations

                String fromEntity = getEntityName(relationParts[0]);
                String fromField = getFieldName(relationParts[0]);
                String toEntity = getEntityName(relationParts[1]);
                String toField = getFieldName(relationParts[1]);

                if (fromEntity.equals(entityName)) {
                    processedBody.append(generateRelationshipAnnotation(relationshipType, toEntity, fromField, toField));
                }
            }
        }

        return processedBody.toString();
    }

    private static String getEntityName(String part) {
        int startIndex = part.indexOf('{');
        if (startIndex == -1) return part.trim();
        return part.substring(0, startIndex).trim();
    }

    private static String getFieldName(String part) {
        int startIndex = part.indexOf('{');
        int endIndex = part.indexOf('}');
        if (startIndex == -1 || endIndex == -1) return "";
        return part.substring(startIndex + 1, endIndex).trim();
    }

    private static String generateRelationshipAnnotation(String relationshipType, String toEntity, String fromField, String toField) {
        String annotation = "";
        switch (relationshipType) {
            case "OneToMany":
                annotation = "    @OneToMany(mappedBy = \"" + toField + "\")\n";
                break;
            case "ManyToOne":
                annotation = "    @ManyToOne\n    @JoinColumn(name = \"" + fromField + "\")\n";
                break;
            case "OneToOne":
                annotation = "    @OneToOne\n    @JoinColumn(name = \"" + fromField + "\")\n";
                break;
            case "ManyToMany":
                annotation = "    @ManyToMany\n    @JoinTable(\n        name = \"" + fromField + "_" + toField + "\",\n" +
                        "        joinColumns = @JoinColumn(name = \"" + fromField + "_id\"),\n" +
                        "        inverseJoinColumns = @JoinColumn(name = \"" + toField + "_id\")\n" +
                        "    )\n";
                break;
        }
        annotation += "    private Set<" + toEntity + "> " + fromField + ";\n";
        return annotation;
    }

    private static String mapJDLTypeToJavaType(String jdlType) {
        switch (jdlType) {
            case "String":
                return "String";
            case "BigDecimal":
                return "BigDecimal";
            case "Instant":
                return "Instant";
            case "Boolean":
                return "Boolean";
            // add more mappings as needed
            default:
                return "String"; // default type
        }
    }

    public static void generateRepository(String entityName) throws IOException {
        String template = new String(Files.readAllBytes(Paths.get(REPOSITORY_TEMPLATE_PATH)));
        String classContent = template.replace("{{ENTITY_NAME}}", entityName);

        Path repositoryPath = Paths.get("src/main/java/com/example/jdlparser/repositories");
        if (!Files.exists(repositoryPath)) {
            Files.createDirectories(repositoryPath);
        }

        Files.write(Paths.get(repositoryPath.toString(), entityName + "Repository.java"), classContent.getBytes());
    }
}
