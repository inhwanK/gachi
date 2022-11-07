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

@Component
public class FileNameSupport {

    private static final Tika tika = new Tika();

//    public String generate(MultipartFile multipartFile) {
////        String generate = md5(multipartFile, userEmail) + extension(multipartFile);
//
//        return uuid(multipartFile) + extension(multipartFile);
//    }

    public static String md5(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update((fileName + LocalDateTime.now())
                    .getBytes(StandardCharsets.UTF_8));
            return Hex.encodeHexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new HashFailureException();
        }
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

    public static String korToUni(String kor) {
        StringBuffer result = new StringBuffer();

        for (int i = 0;i < kor.length(); i++) {
            int cd = kor.codePointAt(i);

            if (cd < 128) {
                result.append(String.format("%c", cd));
            } else {
                result.append(String.format("\\u%04x", cd));
            }
        }

        return result.toString();
    }

    public static String uniToKor(String uni) {
        StringBuffer result = new StringBuffer();

        for (int i = 0;i < uni.length(); i++) {
            if (uni.charAt(i) == '\\' && uni.charAt(i+1) == 'u') {
                Character c = (char)Integer.parseInt(uni.substring(i+2, i+6), 16);
                result.append(c);
                i+=5;
            } else {
                result.append(uni.charAt(i));
            }
        }

        return result.toString();
    }
}
