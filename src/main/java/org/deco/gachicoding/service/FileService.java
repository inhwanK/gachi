package org.deco.gachicoding.service;

import org.deco.gachicoding.dto.file.FileResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Service
public interface FileService {
    public List<FileResponseDto> getFiles(String boardCategory, Long boardIdx);
}
