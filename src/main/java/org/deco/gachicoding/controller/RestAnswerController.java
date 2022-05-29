package org.deco.gachicoding.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.dto.answer.AnswerResponseDto;
import org.deco.gachicoding.dto.answer.AnswerSaveRequestDto;
import org.deco.gachicoding.dto.answer.AnswerUpdateRequestDto;
import org.deco.gachicoding.dto.response.ResponseState;
import org.deco.gachicoding.service.AnswerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "가치답변 정보 처리 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestAnswerController {
    private final AnswerService answerService;

    @ApiOperation(value = "답변 등록", notes = "답변 등록 수행")
    @ApiResponses(
            @ApiResponse(code = 200, message = "등록된 답변 번호 반환")
    )
    @PostMapping("/answer")
    public Long registerAnswer(@ApiParam(name = "") @RequestBody AnswerSaveRequestDto dto) {
        return answerService.registerAnswer(dto);
    }

    @ApiOperation(value = "답변 리스트")
    @GetMapping("/answer/list")
    public Page<AnswerResponseDto> getAnswerList(@RequestParam(value = "keyword", defaultValue = "") String keyword, @PageableDefault(size = 10) Pageable pageable) {
        return answerService.getAnswerList(keyword, pageable);
    }

    @ApiOperation(value = "답변 디테일")
    @GetMapping("/answer/{ansIdx}")
    public AnswerResponseDto getAnswerDetail(@PathVariable Long ansIdx) {
        return answerService.getAnswerDetail(ansIdx);
    }

    @ApiOperation(value = "답변 수정")
    @PutMapping("/answer/modify")
    public AnswerResponseDto modifyAnswer(@RequestBody AnswerUpdateRequestDto dto) {
        return answerService.modifyAnswer(dto);
    }

    @ApiOperation(value = "답변 채택")
    @PutMapping("/answer/select/{ansIdx}")
    public ResponseEntity<ResponseState> selectAnswer(@PathVariable Long ansIdx) {
        return answerService.selectAnswer(ansIdx);
    }

    @ApiOperation(value = "답변 비활성화")
    @PutMapping("/answer/disable/{ansIdx}")
    public ResponseEntity<ResponseState> disableAnswer(@PathVariable Long ansIdx) {
        return answerService.disableAnswer(ansIdx);
    }

    @ApiOperation(value = "답변 활성화")
    @PutMapping("/answer/enable/{ansIdx}")
    public ResponseEntity<ResponseState> enableAnswer(@PathVariable Long ansIdx) {
        return answerService.enableAnswer(ansIdx);
    }

    @ApiOperation(value = "답변 삭제")
    @DeleteMapping("/answer/{ansIdx}")
    public ResponseEntity<ResponseState> removeAnswer(@PathVariable Long ansIdx) {
        return answerService.removeAnswer(ansIdx);
    }
}
