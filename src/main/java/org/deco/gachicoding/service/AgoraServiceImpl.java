package org.deco.gachicoding.service;


import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.domain.agora.Agora;
import org.deco.gachicoding.domain.agora.AgoraRepository;
import org.deco.gachicoding.dto.agora.AgoraResponseDto;
import org.deco.gachicoding.dto.agora.AgoraSaveRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AgoraServiceImpl {

    private final AgoraRepository agoraRepository;

    @Transactional
    public Page<AgoraResponseDto> getAgoraList(Pageable pageable) {
        Page<AgoraResponseDto> agoraList = agoraRepository.findAllByOrderByAgoraIdxAsc(pageable)
                .map(entity -> new AgoraResponseDto(entity));

        return agoraList;
    }

    @Transactional
    public AgoraResponseDto getAgoraDetail(Long agoraIdx) {
        Agora entity = agoraRepository.findById(agoraIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다. agoraIdx = " + agoraIdx));
        return new AgoraResponseDto(entity);
    }

    @Transactional
    public Long registerAgora(AgoraSaveRequestDto dto) {
        return agoraRepository.save(dto.toEntity()).getAgoraIdx();
    }

    @Transactional
    public Long removeAgora(Long agoraIdx) {
        agoraRepository.deleteById(agoraIdx);
        return agoraIdx;
    }
}
