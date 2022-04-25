package org.deco.gachicoding.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.dto.question.QuestionResponseDto;
import org.deco.gachicoding.dto.question.QuestionSaveRequestDto;
import org.deco.gachicoding.dto.question.QuestionUpdateRequestDto;
import org.deco.gachicoding.service.question.QuestionService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RestQuestionController {
    private final QuestionService questionService;

    @ApiOperation(value = "질문 리스트")
    @GetMapping("/question/list")
    public Page<QuestionResponseDto> getQuestionListByKeyword(@RequestParam(value = "keyword", defaultValue = "") String keyword, @PathVariable int page){
        return questionService.getQuestionListByKeyword(keyword, page);
    }

    @ApiOperation(value = "질문 디테일")
    @GetMapping("/question/{questionIdx}")
    public QuestionResponseDto getQuestionDetailById(@PathVariable Long questionIdx){
        return questionService.getQuestionDetailById(questionIdx);
    }

    @ApiOperation(value = "질문 등록")
    @PostMapping("/question")
    public Long registerQuestion(@Valid @RequestBody QuestionSaveRequestDto dto){
        return questionService.registerQuestion(dto);
    }

    @ApiOperation(value = "질문 수정")
    @PutMapping("/question/modify/{idx}")
    public QuestionResponseDto modifyQuestionById(@PathVariable Long questionIdx, @RequestBody QuestionUpdateRequestDto dto){
        return questionService.modifyQuestionById(questionIdx, dto);
    }

    @ApiOperation(value = "질문 비활성화")
    @DeleteMapping("/question/{questionIdx}")
    public Long removeQuestion(@PathVariable Long questionIdx){
        return questionService.removeQuestion(questionIdx);
    }
}
