package com.devfay.jcodegenerator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.devfay.jcodegenerator.util.Utilitarios.getActualTime;
import static com.devfay.jcodegenerator.util.Utilitarios.measureExecutionTime;

@Slf4j
@AllArgsConstructor
@Component
public class StartupApplicationRunner implements ApplicationRunner {

    private final JCodeGeneratorService jCodeGeneratorService;

    @Override
    public void run(ApplicationArguments args) {
        measureExecutionTime(() -> {
            try {
                log.info("Inicio Ejecucion del proceso {}", getActualTime());

                String parametro1 = getRequiredArgument(args, "parametro1");
                boolean parametroBooleanOptional = getOptionalArgument(args, "parametroBooleanOptional")
                        .map(Boolean::parseBoolean)
                        .orElse(false);

                jCodeGeneratorService.generateElements(parametro1,parametroBooleanOptional);
            } catch (IllegalArgumentException e) {
                log.error("Argumento requerido faltante: {}", e.getMessage(), e);
            }
        });
    }

    private String getRequiredArgument(ApplicationArguments args, String key) {
        List<String> values = args.getOptionValues(key);
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException(key);
        }
        return values.getFirst();
    }

    private java.util.Optional<String> getOptionalArgument(ApplicationArguments args, String key) {
        List<String> values = args.getOptionValues(key);
        if (values == null || values.isEmpty()) {
            return java.util.Optional.empty();
        }
        return java.util.Optional.of(values.getFirst());
    }
}
