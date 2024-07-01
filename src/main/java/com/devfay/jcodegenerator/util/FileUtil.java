package com.devfay.jcodegenerator.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.core.codec.DecodingException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static com.devfay.jcodegenerator.util.Constantes.*;


@Slf4j
@Component
public class FileUtil {

    public static File decodeAndSaveFile(AppProperties props, String parseObj) {

        byte[] byteArray = Base64.decodeBase64(parseObj.getBytes());
        Path compressedFilePath = Paths.get(props.getPathComprimidos().toString(), parseObj + "." + parseObj);
        File compressedFile = compressedFilePath.toFile();
        deleteIfExists(compressedFilePath);
        try (FileOutputStream fileOutputStream = new FileOutputStream(compressedFile)) {
            fileOutputStream.write(byteArray);
        } catch (IOException e) {
            log.error("Error al escribir en el archivo: {}", compressedFilePath, e);
        }
        return compressedFile;
    }

    public static void decompressCifFile(AppProperties props, String parseObj, File fiComprimido7z) throws IOException {

        Path pathFileTedCifrados = Paths.get(props.getPathCifrados().toString(), parseObj + CIF);
        deleteIfExists(pathFileTedCifrados);
        decompressFile(fiComprimido7z.toString(), props.getPathCifrados());
    }

    private static void decompressFile(String origen, Path destinoDescomprimir) throws IOException {
        SevenZ.decompress(origen, destinoDescomprimir.toFile());
    }

    public static File getDecodeFile(AppProperties props, String nombreArchivo) {

        Path inputFilePath = Paths.get(props.getPathCifrados().toString(), nombreArchivo + "CIF");
        Path outputFilePath = Paths.get(props.getPathFinales().toString(), props.getTipoOrden() + "_" + nombreArchivo + FINAL_TXT);

        deleteIfExists(outputFilePath);
        decryptFile(props.getLlaveAes(), inputFilePath, outputFilePath);
        return outputFilePath.toFile();
    }

    public static void decryptFile(String llaveAes, Path inputFilePath, Path outputFilePath) {

        int ENCRYPT_MODE = 2;
        boolean decryptionSuccessful = Crypto.fileProcessorB64_API(
                ENCRYPT_MODE,
                llaveAes,
                inputFilePath.toFile(),
                outputFilePath.toFile()
        );
        if (!decryptionSuccessful) {
            log.debug("Fallo Descifrado del Archivo");
            throw new DecodingException("Fallo Descifrado del Archivo");
        }
    }

    private static void deleteIfExists(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            log.error("Error al eliminar el archivo existente: {}", path, e);
        }
    }

    public static String getChecksum(File file) {
        try {
            MessageDigest shaDigest = MessageDigest.getInstance(SHA_256);
            return getFileChecksum(shaDigest, file);
        } catch (NoSuchAlgorithmException | IOException e) {
            log.error("Error al calcular el checksum: {}", file.getPath(), e);
            return "";
        }
    }

    private static String getFileChecksum(MessageDigest digest, File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] byteArray = new byte[1024];
            int bytesCount;

            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }

            byte[] bytes = digest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(String.format("%02x", aByte));
            }

            return sb.toString();
        }
    }

    public static void validateChecksum(String shaChecksum, String codigoArchivo, String routeFile) {

        if (shaChecksum.equals(codigoArchivo)) {
            log.debug("ARCHIVO DESCARGADO COMPLETO: " + FileUtil.truncateMessage(routeFile));
        } else {
            log.debug("ARCHIVO DESCARGADO INCOMPLETO: " + FileUtil.truncateMessage(routeFile));
            throw new RuntimeException("ARCHIVO DESCARGADO INCOMPLETO");
        }
        log.debug("shaChecksum: {}", shaChecksum);
        log.debug("Codigo Archivo: {}", FileUtil.truncateMessage(codigoArchivo));
    }

    public static String truncateMessage(String message) {
        if (null == message) {
            log.debug("El mensaje a truncar es nulo");
        } else if (message.length() > MAX_LOG_LENGTH) {
            return message.substring(0, MAX_LOG_LENGTH) + "...[TRUNCATED]";
        }
        return message;
    }
    public static String truncateMessage(String message, int maxLogLength) {
        if (null == message) {
            log.debug("El mensaje a truncar es nulo");
        } else if (message.length() > maxLogLength) {
            return message.substring(0, maxLogLength) + "...[TRUNCATED]";
        }
        return message;
    }

    public static void getFileAsBase64String(String filePath, File outputFile) throws IOException {
        Path path = Paths.get(filePath);
        byte[] byteArray = Files.readAllBytes(path);

        String base64String = Base64.encodeBase64String(byteArray);
        byte[] outputBytes = base64String.getBytes(StandardCharsets.UTF_8);

        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            outputStream.write(outputBytes);
        }
    }

    /**
     * Borra recursivamente un directorio y todo su contenido.
     *
     * @param directoryPath La ruta del directorio a borrar
     * @throws IOException Si ocurre un error durante la operación
     */
    private static void deleteDirectoryRecursively(Path directoryPath) throws IOException {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directoryPath)) {
            for (Path path : directoryStream) {
                if (Files.isDirectory(path)) {
                    deleteDirectoryRecursively(path);
                } else {
                    Files.delete(path);
                }
            }
        }
        Files.delete(directoryPath);
    }

    public static void createFolders(AppProperties appProperties) {
        List<Path> pathList = new ArrayList<>();
        pathList.add(appProperties.getPathCifrados());
        pathList.add(appProperties.getPathComprimidos());
        pathList.add(appProperties.getPathFinales());
        pathList.add(appProperties.getPathTemporal());
        pathList.forEach(path -> {
            if (Files.notExists(path)) {
                try {
                    Files.createDirectories(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    public static void cleanFolders(AppProperties props) {
        List<Path> pathList = new ArrayList<>();
        pathList.add(props.getPathCifrados());
        pathList.add(props.getPathComprimidos());
        pathList.add(props.getPathTemporal());
        pathList.forEach(path -> {
            try {
                cleanFolder(path.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    /**
     * Limpia todos los archivos y subdirectorios dentro del directorio especificado.
     *
     * @param directoryPath La ruta del directorio a limpiar
     * @throws IOException Si ocurre un error durante la operación
     */
    public static void cleanFolder(String directoryPath) throws IOException {
        Path dirPath = Paths.get(directoryPath);
        if (!Files.isDirectory(dirPath)) {
            throw new IllegalArgumentException("La ruta especificada no es un directorio: " + directoryPath);
        }
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dirPath)) {
            for (Path path : directoryStream) {
                if (Files.isDirectory(path)) {
                    deleteDirectoryRecursively(path);
                } else {
                    Files.delete(path);
                }
            }
        }
    }
}