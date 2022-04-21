package org.deco.gachicoding.service.file;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;

@Service
public interface FileService {
    String copyTempImage(MultipartHttpServletRequest mpRequest) throws IOException;
}
