package org.deco.gachicoding.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.dto.question.QuestionResponseDto;
import org.deco.gachicoding.dto.question.QuestionSaveRequestDto;
import org.deco.gachicoding.dto.question.QuestionUpdateRequestDto;
import org.deco.gachicoding.dto.response.ResponseState;
import org.deco.gachicoding.service.QuestionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RestQuestionController {
    private final QuestionService questionService;

    @ApiOperation(value = "질문 등록", notes = "")
    @PostMapping("/question")
    public Long registerQuestion(@Valid @RequestBody QuestionSaveRequestDto dto){
        return questionService.registerQuestion(dto);
    }

    @ApiOperation(value = "질문 리스트")
    @GetMapping("/question/list")
    public Page<QuestionResponseDto> getQuestionList(@RequestParam(value = "keyword", defaultValue = "") String keyword, @PageableDefault(size = 10) Pageable pageable){
        return questionService.getQuestionList(keyword, pageable);
    }

    @ApiOperation(value = "질문 디테일")
    @GetMapping("/question/{queIdx}")
    public QuestionResponseDto getQuestionDetail(@PathVariable Long queIdx){
        return questionService.getQuestionDetail(queIdx);
    }

    @ApiOperation(value = "질문 수정")
    @PutMapping("/question/modify")
    public QuestionResponseDto modifyQuestion(@RequestBody QuestionUpdateRequestDto dto){
        return questionService.modifyQuestion(dto);
    }

    @ApiOperation(value = "질문 비활성화")
    @PutMapping("/question/disable/{queIdx}")
    public ResponseEntity<ResponseState> disableQuestion(@PathVariable Long queIdx){
        return questionService.disableQuestion(queIdx);
    }

    @ApiOperation(value = "질문 활성화")
    @PutMapping("/question/enable/{queIdx}")
    public ResponseEntity<ResponseState> enableQuestion(@PathVariable Long queIdx){
        return questionService.enableQuestion(queIdx);
    }

    @ApiOperation(value = "질문 삭제")
    @DeleteMapping("/question/{queIdx}")
    public ResponseEntity<ResponseState> removeQuestion(@PathVariable Long queIdx){
        return questionService.removeQuestion(queIdx);
    }
}
