package com.devfay.jcodegenerator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PojoGenerator {

    public static void main(String[] args) {
        // Ejemplo de uso
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("packageName", "com.example.demo");
        parameters.put("className", "Person");
        parameters.put("attributes", List.of(
                new Attribute("String", "name"),
                new Attribute("int", "age"),
                new Attribute("String", "email")
        ));

        try {
            String classContent = generateClassFromTemplate(parameters);
            // Escribir el contenido generado en un archivo
            writeFile("src/main/java/com/example/demo/Person.java", classContent);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    public static String generateClassFromTemplate(Map<String, Object> parameters) throws IOException, TemplateException {
        // Configuración de FreeMarker
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setDirectoryForTemplateLoading(new File("src/main/resources/templates"));
        cfg.setDefaultEncoding("UTF-8");

        // Cargar la plantilla
        Template template = cfg.getTemplate("pojo-template.ftl");

        // Procesar la plantilla con los parámetros
        StringWriter out = new StringWriter();
        template.process(parameters, out);
        return out.toString();
    }

    public static void writeFile(String filePath, String content) throws IOException {
        File file = new File(filePath);
        file.getParentFile().mkdirs(); // Crear directorios si no existen
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }

    // Clase interna para representar atributos
    public static class Attribute {
        private final String type;
        private final String name;

        public Attribute(String type, String name) {
            this.type = type;
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }
    }
}
