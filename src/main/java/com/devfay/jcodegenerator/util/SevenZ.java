package com.devfay.jcodegenerator.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
public class SevenZ {

    private static final int BUFFER_SIZE = 1024;

    /**
     * Compresses a file into a 7z archive.
     *
     * @param outputFileName the name of the output archive
     * @param fileToCompress the file to compress
     * @return true if the compression was successful, false otherwise
     * @throws IOException if an I/O error occurs
     */
    public static boolean compress(File fileToCompress, String outputFileName) throws IOException {
        try (SevenZOutputFile sevenZOutput = new SevenZOutputFile(new File(outputFileName));
             FileInputStream fis = new FileInputStream(fileToCompress)) {

            log.debug("Comprimiendo Archivo: {}", FileUtil.truncateMessage(fileToCompress.toString()));

            SevenZArchiveEntry entry = sevenZOutput.createArchiveEntry(fileToCompress, fileToCompress.getName());
            sevenZOutput.putArchiveEntry(entry);

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) > 0) {
                sevenZOutput.write(buffer, 0, bytesRead);
            }
            sevenZOutput.closeArchiveEntry();
            log.debug("Archivo Comprimido OK: {}", FileUtil.truncateMessage(fileToCompress.toString()));
            sevenZOutput.finish();
            return true;
        } catch (Exception e) {
            log.error("Error al comprimir archivo: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Decompresses a 7z archive to a specified destination.
     *
     * @param inputFilePath the path to the 7z archive
     * @param destination   the destination directory
     * @throws IOException if an I/O error occurs
     */
    public static void decompress(String inputFilePath, File destination) throws IOException {
        try (SevenZFile sevenZFile = new SevenZFile(new File(inputFilePath))) {
            SevenZArchiveEntry entry;

            while ((entry = sevenZFile.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }

                File curFile = new File(destination, entry.getName());
                File parent = curFile.getParentFile();
                if (!parent.exists()) {
                    parent.mkdirs();
                }

                try (FileOutputStream out = new FileOutputStream(curFile)) {
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int bytesRead;
                    while ((bytesRead = sevenZFile.read(buffer)) > 0) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Error al descomprimir archivo: {}", e.getMessage(), e);
            throw e;
        }
    }
}
