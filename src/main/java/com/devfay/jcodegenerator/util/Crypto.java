package com.devfay.jcodegenerator.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Slf4j
public class Crypto {

    private static final String cypherInstance = "AES/ECB/PKCS5Padding";
    private static byte[] key_API;
    private static SecretKeySpec secretKey_API;

    public static String encryptApi(String llaveAes, String strToEncrypt) {
        String sRetorno = "";
        try {
            log.debug("encrypt_API masterKey and strToEncrypt {} - {}", llaveAes, FileUtil.truncateMessage(strToEncrypt));
            setKey_API(llaveAes);
            Cipher cipher = Cipher.getInstance(cypherInstance);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey_API);
            log.debug("encrypt_API Cipher {}", FileUtil.truncateMessage(cipher.toString()));
            sRetorno = Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
            log.debug("encrypt_API Retorno {}", FileUtil.truncateMessage(sRetorno));
        } catch (Exception e) {
            log.debug("encrypt_API Error while encrypting: " + e);
        }
        return sRetorno;
    }

    public static String decryptApi(String llaveAes, String strToDecrypt) {
        String sRetorno = "";
        try {
            log.debug("decrypt_API masterKey and strToEncrypt {} - {}", llaveAes, FileUtil.truncateMessage(strToDecrypt));
            setKey_API(llaveAes);
            Cipher cipher = Cipher.getInstance(cypherInstance);
            log.debug("decrypt_API Cipher {}", FileUtil.truncateMessage(cipher.toString()));
            cipher.init(Cipher.DECRYPT_MODE, secretKey_API);
            sRetorno = new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)), StandardCharsets.UTF_8);
            log.debug("decrypt_API Retorno {}", FileUtil.truncateMessage(sRetorno));
        } catch (Exception e) {
            log.debug("Error while decrypting: " + e);
            sRetorno = null;
        }
        return sRetorno;
    }

    public static boolean fileProcessorB64_API(int cipherMode, String llaveAes, File inputFile, File outputFile) {
        boolean bRetorno = true;
        try {
            setKey_API(llaveAes);
            Cipher cipher = Cipher.getInstance(cypherInstance);
            cipher.init(cipherMode, secretKey_API);
            log.debug("fileProcessorB64_API(). Inicio");

            try (FileInputStream inputStream = new FileInputStream(inputFile);
                 FileOutputStream outputStream = new FileOutputStream(outputFile)) {

                if (cipherMode == Cipher.ENCRYPT_MODE) {
                    processAndEncode(inputStream, outputStream, cipher);
                } else if (cipherMode == Cipher.DECRYPT_MODE) {
                    decodeAndProcess(inputStream, outputStream, cipher);
                }
            } catch (IOException e) {
                e.printStackTrace();
                bRetorno = false;
            }
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            bRetorno = false;
        }

        log.debug("fileProcessorB64_API(). Fin. resultado: {}", bRetorno);
        return bRetorno;
    }
    public static void processAndEncode(InputStream inputStream, OutputStream outputStream, Cipher cipher) throws IOException {
        try (CipherOutputStream cipherOut = new CipherOutputStream(outputStream, cipher);
             Base64OutputStream base64Out = new Base64OutputStream(cipherOut, true)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                base64Out.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            log.error("Error during encoding and processing", e);
            throw e; // Rethrow the exception after logging
        }
    }
    public static void decodeAndProcess(InputStream inputStream, OutputStream outputStream, Cipher cipher) throws IOException {
        try (Base64InputStream base64In = new Base64InputStream(inputStream);
             CipherInputStream cipherIn = new CipherInputStream(base64In, cipher)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = cipherIn.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }
    static void setKey_API(String myKey) {
        if (myKey == null || myKey.isEmpty()) {
            throw new IllegalArgumentException("The key cannot be null or empty");
        }
        try {
            key_API = myKey.getBytes(StandardCharsets.UTF_8);
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            key_API = sha.digest(key_API);
            key_API = Arrays.copyOf(key_API, 16); // Use only first 128 bit
            secretKey_API = new SecretKeySpec(key_API, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
