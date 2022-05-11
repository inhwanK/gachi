package org.deco.gachicoding.service.impl;


import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.domain.agora.Agora;
import org.deco.gachicoding.domain.agora.AgoraRepository;
import org.deco.gachicoding.dto.agora.AgoraResponseDto;
import org.deco.gachicoding.dto.agora.AgoraSaveRequestDto;
import org.deco.gachicoding.service.AgoraService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AgoraServiceImpl implements AgoraService {

    private final AgoraRepository agoraRepository;

    @Transactional
    @Override
    public Page<AgoraResponseDto> getAgoraList(Pageable pageable) {
        Page<AgoraResponseDto> agoraList = agoraRepository.findAllByOrderByAgoraIdxAsc(pageable)
                .map(entity -> new AgoraResponseDto(entity));

        return agoraList;
    }

    @Transactional
    @Override
    public AgoraResponseDto getAgoraDetail(Long agoraIdx) {
        Agora entity = agoraRepository.findById(agoraIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다. agoraIdx = " + agoraIdx));
        return new AgoraResponseDto(entity);
    }

    @Transactional
    @Override
    public Long registerAgora(AgoraSaveRequestDto dto) {
        return agoraRepository.save(dto.toEntity()).getAgoraIdx();
    }

    @Transactional
    @Override
    public Long removeAgora(Long agoraIdx) {
        agoraRepository.deleteById(agoraIdx);
        return agoraIdx;
    }
}
