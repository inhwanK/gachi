package org.deco.gachicoding.service;

import org.deco.gachicoding.dto.ResponseDto;
import org.deco.gachicoding.dto.file.FileSaveDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface FileService {
    List<String> uploadTempImg(List<MultipartFile> files) throws IOException;

    void registerFile(FileSaveDto fileSaveDto);

    ResponseDto getFiles(Long boardIdx, String boardCategory, ResponseDto dto);

    String extractImgSrc(Long boardIdx, String content, String category);
}
