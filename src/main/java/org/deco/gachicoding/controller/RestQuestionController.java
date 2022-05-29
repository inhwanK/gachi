package org.deco.gachicoding.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.dto.question.QuestionDetailResponseDto;
import org.deco.gachicoding.dto.question.QuestionListResponseDto;
import org.deco.gachicoding.dto.question.QuestionSaveRequestDto;
import org.deco.gachicoding.dto.question.QuestionUpdateRequestDto;
import org.deco.gachicoding.dto.response.ResponseState;
import org.deco.gachicoding.service.QuestionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Api(tags = "가치질문 정보 처리 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RestQuestionController {
    private final QuestionService questionService;

    @ApiOperation(value = "질문 등록", notes = "하나의 질문 데이터를 등록.")
    @PostMapping("/question")
    public Long registerQuestion(@ApiParam(name = "질문 요청 DTO", value = "질문 요청 body 정보") @Valid @RequestBody QuestionSaveRequestDto dto) {
        return questionService.registerQuestion(dto);
    }

    @ApiOperation(value = "질문 리스트", notes = "여러 개의 질문 데이터 응답. 이 때, 질문별 답변 데이터는 포함하지 않음.")
    @GetMapping("/question/list")
    public Page<QuestionListResponseDto> getQuestionList(@ApiParam(name = "검색어") @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                                         @ApiIgnore @PageableDefault(size = 10) Pageable pageable) {

        return questionService.getQuestionList(keyword, pageable);
    }

    @ApiOperation(value = "질문 디테일", notes = "하나의 질문 데이터와 해당 질문의 답변들을 응답")
    @GetMapping("/question/{queIdx}")
    public QuestionDetailResponseDto getQuestionDetail(@ApiParam(name = "질문 번호") @PathVariable Long queIdx) {
        return questionService.getQuestionDetail(queIdx);
    }

    @ApiOperation(value = "질문 수정", notes = "질문 데이터를 수정 (리팩토링 필요함)")
    @PutMapping("/question/modify/{userIdx}")
    public QuestionDetailResponseDto modifyQuestion(@ApiParam(name = "사용자 번호") @PathVariable Long userIdx,
                                                    @ApiParam(name = "질문 수정 요청 DTO", value = "질문 수정 요청 body 정보") @RequestBody QuestionUpdateRequestDto dto) {
        return questionService.modifyQuestion(userIdx, dto);
    }

    @ApiOperation(value = "질문 비활성화", notes = "사용자 입장에서 질문 데이터를 삭제")
    @PutMapping("/question/disable/{queIdx}")
    public ResponseEntity<ResponseState> disableQuestion(@ApiParam(name = "질문 번호") @PathVariable Long queIdx) {
        return questionService.disableQuestion(queIdx);
    }

    @ApiOperation(value = "질문 활성화", notes = "사용자 입장에서 삭제된 질문 데이터 복구")
    @PutMapping("/question/enable/{queIdx}")
    public ResponseEntity<ResponseState> enableQuestion(@ApiParam(name = "질문 번호") @PathVariable Long queIdx) {
        return questionService.enableQuestion(queIdx);
    }

    @ApiOperation(value = "질문 삭제", notes = "질문 데이터를 DB에서 완전히 삭제")
    @DeleteMapping("/question/{queIdx}")
    public ResponseEntity<ResponseState> removeQuestion(@ApiParam(name = "질문 번호") @PathVariable Long queIdx) {
        return questionService.removeQuestion(queIdx);
    }
}
