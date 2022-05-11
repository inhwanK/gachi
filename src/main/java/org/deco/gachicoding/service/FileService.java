package org.deco.gachicoding.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.net.URI;

@Service
public interface FileService {
    URI copyTempImage(MultipartHttpServletRequest mpRequest) throws IOException;
}
