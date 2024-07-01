package com.devfay.jcodegenerator.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Slf4j
public class Utilitarios {

    private static final TimeZone TIME_ZONE = TimeZone.getTimeZone("GMT-4");
    private static final Locale LOCALE = Locale.US;

    /**
     * Purpose: Obtener dirección IP del equipo
     *
     * @return Dirección IP
     */
    public static String getDireccionIP() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("Error al obtener la dirección IP: {}", e.getMessage(), e);
            return null;
        }
    }
    /**
     * Purpose: Devuelve un string conteniendo la fecha con el formato
     * especificado. Devuelve null si el formato no puede ser interpretado.
     *
     * @param formato El formato deseado para la fecha
     * @return La fecha en el formato especificado o null si el formato es inválido
     */
    public static String getDateTime(String formato) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(formato, LOCALE);
            formatter.setTimeZone(TIME_ZONE);
            return formatter.format(new Date());
        } catch (IllegalArgumentException e) {
            log.error("Formato de fecha inválido: {}", formato, e);
            return null;
        }
    }
    /**
     * Purpose: Guardar registro en archivo
     *
     * @param tempPathFile  El camino al archivo temporal
     * @param sLineaRegistro La línea a registrar
     * @param bAnexar       Si se debe anexar al archivo existente
     */
    public static void msSaveFile(String tempPathFile, String sLineaRegistro, boolean bAnexar) {
        File file = new File(tempPathFile);
        try (FileWriter fw = new FileWriter(file, bAnexar);
             BufferedWriter bw = new BufferedWriter(fw)) {

            if (!file.exists() && !file.createNewFile()) {
                log.error("No se pudo crear el archivo: {}", tempPathFile);
                return;
            }
            log.debug("Guardando en archivo: {}", FileUtil.truncateMessage(file.getAbsolutePath()));
            log.debug("Registro: {}", FileUtil.truncateMessage(sLineaRegistro));
            bw.write(sLineaRegistro);
            bw.newLine();
        } catch (IOException e) {
            log.error("Error al guardar en archivo: {}", e.getMessage(), e);
        }
    }
    public static String getActualTime() {
        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return now.format(formatter);
    }
    public static void measureExecutionTime(Runnable task) {
        long startTime = System.currentTimeMillis();
        task.run();
        long endTime = System.currentTimeMillis();
        long durationInMilliseconds = (endTime - startTime);
        double durationInSeconds = (double) durationInMilliseconds / 1000.0;
        log.info("Tiempo total: {} segundos", String.format("%.3f", durationInSeconds));
    }
}
