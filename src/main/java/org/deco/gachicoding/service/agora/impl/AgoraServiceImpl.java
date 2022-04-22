package org.deco.gachicoding.service.agora.impl;


import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.domain.agora.Agora;
import org.deco.gachicoding.domain.agora.AgoraRepository;
import org.deco.gachicoding.dto.agora.AgoraResponseDto;
import org.deco.gachicoding.dto.agora.AgoraSaveRequestDto;
import org.deco.gachicoding.service.agora.AgoraService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgoraServiceImpl implements AgoraService {

    private final AgoraRepository agoraRepository;

    @Override
    public Page<AgoraResponseDto> getAgoraList(Pageable pageable) {
        Page<AgoraResponseDto> agoraList = agoraRepository.findAllByOrderByAgoraIdxAsc(pageable)
                .map(entity -> new AgoraResponseDto(entity));

        return agoraList;
    }

    @Override
    public AgoraResponseDto getAgoraDetail(Long agoraIdx) {
        Agora entity = agoraRepository.findById(agoraIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다. agoraIdx = " + agoraIdx));
        return new AgoraResponseDto(entity);
    }

    @Override
    public Long registerAgora(AgoraSaveRequestDto dto) {
        return agoraRepository.save(dto.toEntity()).getAgoraIdx();
    }

    @Override
    public Long removeAgora(Long agoraIdx) {
        agoraRepository.deleteById(agoraIdx);
        return agoraIdx;
    }
}
