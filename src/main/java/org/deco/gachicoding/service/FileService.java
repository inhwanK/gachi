package org.deco.gachicoding.service;

import org.deco.gachicoding.dto.ResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface FileService {
    public ResponseDto getFiles(Long boardIdx, String boardCategory, ResponseDto dto);
}
