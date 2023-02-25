package org.deco.gachicoding.post.question.presentation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deco.gachicoding.post.question.QuestionDto;
import org.deco.gachicoding.post.question.application.QuestionService;
import org.deco.gachicoding.post.question.application.dto.request.QuestionBasicRequestDto;
import org.deco.gachicoding.post.question.application.dto.request.QuestionUpdateRequestDto;
import org.deco.gachicoding.post.question.presentation.dto.QuestionAssembler;
import org.deco.gachicoding.post.question.presentation.dto.request.QuestionUpdateRequest;
import org.deco.gachicoding.post.question.presentation.dto.response.QuestionDetailResponse;
import org.deco.gachicoding.post.question.presentation.dto.response.QuestionListResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

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
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return questionService.registerQuestion(userEmail, request);
    }


    // 이거 키워드만 받으면 안되남?
    @ApiOperation(value = "질문 리스트")
    @GetMapping("/question/list")
    public ResponseEntity<List<QuestionListResponse>> getQuestionList(
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @ApiIgnore @PageableDefault(size = 10) Pageable pageable
    ) {

        return ResponseEntity.ok(
                QuestionAssembler.questionListResponse(
                        questionService.getQuestionList(keyword, pageable)
                )
        );
    }

    @ApiOperation(value = "질문 디테일")
    @GetMapping("/question/{queIdx}")
    public ResponseEntity<QuestionDto.DetailResponseDto> getQuestionDetail(
            @PathVariable Long queIdx
    ) {
        return ResponseEntity.ok(questionService.getQuestionDetail(queIdx));
    }

    @ApiOperation(value = "질문 수정")
    @PreAuthorize("isAuthenticated()") // 작성자 맞는지 체크 해야함
    @PatchMapping("/question/modify")
    public ResponseEntity<Object> modifyQuestion(
            @RequestBody @Valid QuestionUpdateRequest request
    ) {

        QuestionUpdateRequestDto dto = QuestionAssembler.questionUpdateRequestDto(request);

        Long queIdx = questionService.modifyQuestion(dto);

        String redirectUrl = String.format("/api/question/%d", queIdx);

        return ResponseEntity
                .created(URI.create(redirectUrl))
                .build();
    }

    @ApiOperation(value = "질문 비활성화")
    @PreAuthorize("isAuthenticated()") // 작성자 맞는지 체크 해야함
    @PatchMapping("/question/disable")
    public ResponseEntity<Void> disableQuestion(
            @RequestParam Long queIdx,
            @RequestParam String userEmail
    ) {

        QuestionBasicRequestDto dto =
                QuestionAssembler.questionBasicRequestDto(queIdx, userEmail);

        questionService.disableQuestion(dto);

        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "질문 활성화")
    @PreAuthorize("isAuthenticated()") // 작성자 맞는지 체크 해야함
    @PatchMapping("/question/enable")
    public ResponseEntity<Void> enableQuestion(
            @RequestParam Long queIdx,
            @RequestParam(value = "userEmail", defaultValue = "") String userEmail
    ) {

        QuestionBasicRequestDto dto = QuestionAssembler.questionBasicRequestDto(queIdx, userEmail);

        questionService.enableQuestion(dto);

        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "질문 삭제")
    @PreAuthorize("isAuthenticated()") // 작성자 맞는지 체크 해야함
    @DeleteMapping("/question")
    public void removeQuestion(
            @RequestParam Long queIdx,
            @RequestParam(value = "userEmail", defaultValue = "") String userEmail
    ) {

        QuestionBasicRequestDto dto = QuestionAssembler.questionBasicRequestDto(queIdx, userEmail);

        questionService.removeQuestion(dto);
    }
}
