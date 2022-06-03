package org.deco.gachicoding.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.dto.agora.AgoraResponseDto;
import org.deco.gachicoding.dto.agora.AgoraSaveRequestDto;
import org.deco.gachicoding.service.AgoraService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Api(tags = "아고라 정보 처리 API(개발 중..)")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RestAgoraController {

    private final AgoraService agoraService;

    @GetMapping("/agora/list")
    public Page<AgoraResponseDto> getAgoraList(@PageableDefault(size = 10) Pageable pageable){
        return agoraService.getAgoraList(pageable);
    }

    @GetMapping("/agora/{agoraIdx}")
    public AgoraResponseDto getAgoraDetail(@PathVariable Long agoraIdx){
        return agoraService.getAgoraDetail(agoraIdx);
    }

    @PostMapping("/agora")
    public Long registerAgora(@RequestBody AgoraSaveRequestDto dto){
        return agoraService.registerAgora(dto);
    }

    @DeleteMapping("/agora/{agoraIdx}")
    public Long removeAgora(@PathVariable Long agoraIdx){
        return agoraService.removeAgora(agoraIdx);
    }
}
