package com.devfay.jcodegenerator;

import java.io.IOException;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            String jdlFilePath = "/Users/unifay/Documents/playground/ilimitech/JCodeGenerator/src/main/resources/jdl-models/model.jdl";
            Map<String, String> entities = JDLReader.readJDL(jdlFilePath);
            Map<String, String> relationships = JDLReader.readRelationships(jdlFilePath);

            for (Map.Entry<String, String> entry : entities.entrySet()) {
                String entityName = entry.getKey();
                String entityBody = entry.getValue();

                TemplateEngine.generateEntity(entityName, entityBody, relationships);
                TemplateEngine.generateRepository(entityName);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

