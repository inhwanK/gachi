package org.deco.gachicoding.file.infrastructure;

import org.apache.commons.codec.binary.Hex;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.deco.gachicoding.exception.file.FileExtensionException;
import org.deco.gachicoding.exception.file.HashFailureException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class FileNameSupporter {

    private static final Tika tika = new Tika();

//    public String generate(MultipartFile multipartFile) {
////        String generate = md5(multipartFile, userEmail) + extension(multipartFile);
//
//        return uuid(multipartFile) + extension(multipartFile);
//    }

    public static String md5(String fileName) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update((fileName + LocalDateTime.now())
                    .getBytes(StandardCharsets.UTF_8));
            return Hex.encodeHexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new HashFailureException();
        }
    }

    public static String uuid(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();

        return uuid+"_"+fileName;
    }

    public static String ExtensionExtractor(String filename) {
        return filename.substring(filename.lastIndexOf("."));
    }

    // 변조 확인으로 바꾸자
    private String extension(MultipartFile multipartFile) {
        MimeTypes defaultMimeTypes = MimeTypes.getDefaultMimeTypes();
        try {
            MimeType mimeType = defaultMimeTypes.forName(
                    tika.detect(multipartFile.getBytes())
            );
            return mimeType.getExtension();
        } catch (MimeTypeException | IOException e) {
            throw new FileExtensionException();
        }
    }
}
