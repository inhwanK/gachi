package org.deco.gachicoding.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.dto.answer.AnswerResponseDto;
import org.deco.gachicoding.dto.answer.AnswerSaveRequestDto;
import org.deco.gachicoding.dto.answer.AnswerUpdateRequestDto;
import org.deco.gachicoding.service.AnswerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Api
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestAnswerController {
    private final AnswerService answerService;

    @ApiOperation(value = "답변 등록")
    @PostMapping("/answer")
    public Long registerAnswer(@RequestBody AnswerSaveRequestDto dto){
        return answerService.registerAnswer(dto);
    }

    @ApiOperation(value = "답변 리스트")
    @GetMapping("/answer/list")
    public Page<AnswerResponseDto> getAnswerList(@RequestParam(value = "keyword", defaultValue = "") String keyword, @PageableDefault(size = 10) Pageable pageable){
        return answerService.getAnswerList(keyword, pageable);
    }

    @ApiOperation(value = "답변 디테일")
    @GetMapping("/answer/{ansIdx}")
    public AnswerResponseDto getAnswerDetail(@PathVariable Long ansIdx){
        return answerService.getAnswerDetail(ansIdx);
    }

    @ApiOperation(value = "답변 수정")
    @PutMapping("/answer/modify")
    public AnswerResponseDto modifyAnswer(@RequestBody AnswerUpdateRequestDto dto){
        return answerService.modifyAnswer(dto);
    }

    @ApiOperation(value = "답변 비활성화")
    @PutMapping("/answer/disable/{ansIdx}")
    public void disableAnswer(@PathVariable Long ansIdx){
        answerService.disableAnswer(ansIdx);
    }

    @ApiOperation(value = "답변 활성화")
    @PutMapping("/answer/enable/{ansIdx}")
    public void enableAnswer(@PathVariable Long ansIdx){
        answerService.enableAnswer(ansIdx);
    }

    @ApiOperation(value = "답변 삭제")
    @DeleteMapping("/answer/{ansIdx}")
    public Long removeAnswer(@PathVariable Long ansIdx){
        return answerService.removeAnswer(ansIdx);
    }
}
