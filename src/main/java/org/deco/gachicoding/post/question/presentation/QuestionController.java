package org.deco.gachicoding.post.question.presentation;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.post.question.application.dto.QuestionDto;
import org.deco.gachicoding.post.question.application.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class QuestionController {

    private final QuestionService questionService;

    @ApiOperation(value = "질문 등록")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/question")
    public Long registerQuestion(
            @RequestBody
            @Valid QuestionDto.SaveRequestDto request
    ) {
        log.info("{} Register Controller", "Question");
        String loginUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return questionService.registerQuestion(loginUserEmail, request);
    }


    // 이거 키워드만 받으면 안되나?
//    @ApiOperation(value = "질문 리스트")
//    @GetMapping("/question/list")
//    public List<QuestionDto.ListResponseDto> getQuestionList(
//            @RequestParam String keyword,
//            @PageableDefault(size = 10) Pageable pageable
//    ) {
//        return QuestionAssembler.questionListResponse(questionService.getQuestionList(keyword, pageable));
//    }

    @ApiOperation(value = "질문 디테일")
    @GetMapping("/question/{queIdx}")
    public QuestionDto.DetailResponseDto getQuestionDetail(
            @PathVariable Long queIdx
    ) {
        return questionService.getQuestionDetail(queIdx);
    }

    @ApiOperation(value = "질문 수정")
    @PreAuthorize("isAuthenticated() and (#request.userEmail eq principal.username)")
    @PatchMapping("/question/modify")
    public ResponseEntity modifyQuestion(
            @RequestBody @Valid QuestionDto.UpdateRequestDto request
    ) {
        Long modifyTargetIdx = questionService.modifyQuestion(request);
        String redirectUrl = String.format("/api/question/%d", modifyTargetIdx);

        return ResponseEntity
                .created(URI.create(redirectUrl))
                .build();
    }

    @ApiOperation(value = "질문 비활성화")
    @PreAuthorize("isAuthenticated() and (userEmail eq principal.username)")
    @PatchMapping("/question/disable")
    public Long disableQuestion(
            @RequestParam Long queIdx,
            @RequestParam String userEmail
    ) {
        return questionService.disableQuestion(queIdx);
    }

    @ApiOperation(value = "질문 활성화")
    @PreAuthorize("isAuthenticated() and (userEmail eq principal.username)")
    @PatchMapping("/question/enable")
    public Long enableQuestion(
            @RequestParam Long queIdx,
            @RequestParam String userEmail
    ) {
        return questionService.enableQuestion(queIdx);
    }

    @ApiOperation(value = "질문 삭제")
    @PreAuthorize("isAuthenticated() and (userEmail eq principal.username)")
    @DeleteMapping("/question")
    public void removeQuestion(
            @RequestParam Long queIdx,
            @RequestParam String userEmail
    ) {
        questionService.removeQuestion(queIdx);
    }
}
