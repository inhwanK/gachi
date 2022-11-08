package org.deco.gachicoding.post.answer.presentation;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.deco.gachicoding.post.answer.application.AnswerService;
import org.deco.gachicoding.post.answer.presentation.dto.AnswerAssembler;
import org.deco.gachicoding.post.answer.presentation.dto.request.AnswerSaveRequest;
import org.deco.gachicoding.post.answer.presentation.dto.request.AnswerUpdateRequest;
import org.deco.gachicoding.post.answer.presentation.dto.response.AnswerResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Api(tags = "가치답변 정보 처리 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AnswerController {

    private static final String REDIRECT_URL = "/api/question/%d";

    private final AnswerService answerService;

    @ApiOperation(value = "답변 등록", notes = "답변 등록 수행")
    @ApiResponses(
            @ApiResponse(code = 200, message = "등록된 답변 번호 반환")
    )
    @PostMapping("/answer")
    public ResponseEntity<Void> registerAnswer(
            @ApiParam(value = "질문 요청 body 정보")
            @RequestBody AnswerSaveRequest request
    ) {

        // 답변이 소속된 queIdx를 받아 redirect
        Long queIdx = answerService.registerAnswer(
                AnswerAssembler.answerSaveRequestDto(request)
        );

        String redirectUrl = String.format(REDIRECT_URL, queIdx);

        return ResponseEntity.created(URI.create(redirectUrl)).build();
    }

//    @ApiOperation(value = "답변 리스트")
//    @GetMapping("/answer/list")
//    public Page<AnswerResponseDto> getAnswerList(@RequestParam(value = "keyword", defaultValue = "") String keyword, @PageableDefault(size = 10) Pageable pageable) {
//        return answerService.getAnswerList(keyword, pageable);
//    }

//    @ApiOperation(value = "답변 디테일")
//    @ApiResponses(
//            @ApiResponse(code = 200, message = "답변 상세 정보 봔한")
//    )
//    @GetMapping("/answer/{ansIdx}")
//    public AnswerResponseDto getAnswerDetail(@ApiParam(value = "답변 번호", example = "1") @PathVariable Long ansIdx) {
//        return answerService.getAnswerDetail(ansIdx);
//    }

    @ApiOperation(value = "답변 수정")
    @ApiResponses(
            @ApiResponse(code = 200, message = "수정 후 답변 상세 정보 봔한")
    )
    @PutMapping("/answer/modify")
    public ResponseEntity<Void> modifyAnswer(
            @ApiParam(value = "답변 수정 요청 body 정보")
            @RequestBody AnswerUpdateRequest request
    ) {

        Long queIdx = answerService.modifyAnswer(
                AnswerAssembler.answerUpdateRequestDto(request)
        );

        String redirectUrl = String.format(REDIRECT_URL, queIdx);

        return ResponseEntity.created(URI.create(redirectUrl)).build();
    }

    @ApiOperation(value = "답변 채택")
    @ApiResponses({
            @ApiResponse(code = 200, message = "채택 성공"),
            @ApiResponse(code = 409, message = "해결이 완료된 질문입니다"),
            @ApiResponse(code = 500, message = "권한이 없는 유저입니다")
    })
    @PutMapping("/answer/select")
    public ResponseEntity<Void> selectAnswer(
            @ApiParam(value = "질문 번호", example = "1") @RequestParam Long ansIdx,
            @ApiParam(value = "userEmail") @RequestParam(value = "userEmail", defaultValue = "") String userEmail
    ) {

        Long queIdx = answerService.selectAnswer(
                AnswerAssembler.answerSelectRequestDto(ansIdx, userEmail)
        );

        String redirectUrl = String.format(REDIRECT_URL, queIdx);

        return ResponseEntity.created(URI.create(redirectUrl)).build();
    }

//    @ApiOperation(value = "답변 비활성화")
//    @ApiResponses(
//            @ApiResponse(code = 200, message = "비활성화 성공")
//    )
//    @PutMapping("/answer/disable/{ansIdx}")
//    public ResponseEntity<Void> disableAnswer(@ApiParam(value = "답변 번호", example = "1") @PathVariable Long ansIdx) {
////        return answerService.disableAnswer(ansIdx);
//        return ResponseEntity.noContent().build();
//    }
//
//    @ApiOperation(value = "답변 활성화")
//    @ApiResponses(
//            @ApiResponse(code = 200, message = "활성화 성공")
//    )
//    @PutMapping("/answer/enable/{ansIdx}")
//    public ResponseEntity<Void> enableAnswer(@ApiParam(value = "답변 번호", example = "1") @PathVariable Long ansIdx) {
////        return answerService.enableAnswer(ansIdx);
//        return ResponseEntity.noContent().build();
//    }
//
//    @ApiOperation(value = "답변 삭제")
//    @ApiResponses(
//            @ApiResponse(code = 200, message = "삭제 성공")
//    )
//    @DeleteMapping("/answer/{ansIdx}")
//    public ResponseEntity<Void> removeAnswer(@ApiParam(value = "답변 번호", example = "1") @PathVariable Long ansIdx) {
////        return answerService.removeAnswer(ansIdx);
//        return ResponseEntity.noContent().build();
//    }
}
