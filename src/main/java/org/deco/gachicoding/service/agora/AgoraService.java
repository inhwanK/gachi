package org.deco.gachicoding.service.agora;

import org.deco.gachicoding.dto.agora.AgoraResponseDto;
import org.deco.gachicoding.dto.agora.AgoraSaveRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface AgoraService {

    Page<AgoraResponseDto> getAgoraList(Pageable pageable);
    AgoraResponseDto getAgoraDetail(Long agoraIdx);
    Long registerAgora(AgoraSaveRequestDto dto);
    Long removeAgora(Long agoraIdx);
}
