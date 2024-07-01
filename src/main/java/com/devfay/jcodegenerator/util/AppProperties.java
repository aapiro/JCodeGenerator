package com.devfay.jcodegenerator.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String idSocio;
    private String tipoOrden;
    private String llaveAes;
    private String pathFiles;
    private WorkFolder workFolder;

    public Path getPathCifrados() {
        return Paths.get(pathFiles, String.valueOf(WorkFolder.CIFRADOS.getValue()));
    }
    public Path getPathComprimidos() {
        return Paths.get(pathFiles, String.valueOf(WorkFolder.COMPRIMIDOS.getValue()));
    }
    public Path getPathFinales() {
        return Paths.get(pathFiles, String.valueOf(WorkFolder.FINALES.getValue()));
    }
    public Path getPathTemporal() {
        return Paths.get(pathFiles, String.valueOf(WorkFolder.TEMP.getValue()));
    }
}
