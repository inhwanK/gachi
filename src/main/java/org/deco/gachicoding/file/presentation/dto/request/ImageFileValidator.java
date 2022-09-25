package org.deco.gachicoding.file.presentation.dto.request;

import org.apache.tika.Tika;
import org.deco.gachicoding.exception.file.FileExtensionException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class ImageFileValidator {
    private static final String MIME_TYPE = "image";
    private static final Tika tika = new Tika();

    public void execute(MultipartFile multipartFile) {
        try{
            String MIMEType = tika.detect(multipartFile.getInputStream());
            if (!MIMEType.startsWith(MIME_TYPE)) {
                throw new FileExtensionException();
            }
        } catch (IOException e) {
            throw new FileExtensionException();
        }
    }
}
