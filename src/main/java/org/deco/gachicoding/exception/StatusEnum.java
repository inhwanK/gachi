package org.deco.gachicoding.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum StatusEnum {
    SAVE_SUCCESS(OK, "저장 성공"),
    REMOVE_SUCCESS(OK, "삭제 성공"),
    NOTICE_REMOVE_SUCCESS(OK, "공지사항 삭제 성공"),
    DISABLE_SUCCESS(OK, "비활성화 성공"),
    NOTICE_DISABLE_SUCCESS(OK, "공지사항 비활성화 성공"),
    ENABLE_SUCCESS(OK, "활성화 성공"),
    NOTICE_ENABLE_SUCCESS(OK, "공지사항 활성화 성공"),
    SELECT_SUCCESS(OK, "채택 성공"),

    /* 400 BAD_REQUEST : 잘못된 요청*/
    MAXIMUM_LENGTH_OVER_TITLE(BAD_REQUEST, "제목의 길이가 제한을 넘었습니다."),
    MAXIMUM_LENGTH_OVER_CONTENTS(BAD_REQUEST, "내용의 길이가 제한을 넘었습니다."),
    NULL_TITLE(BAD_REQUEST, "제목이 널일 수 없습니다."),
    EMPTY_TITLE(BAD_REQUEST, "제목이 공백일 수 없습니다."),
    NULL_CONTENTS(BAD_REQUEST, "내용이 널일 수 없습니다."),
    EMPTY_CONTENTS(BAD_REQUEST, "내용이 공백일 수 없습니다."),

    /* 401 UNAUTHORIZED : 권한이 없는 요청 */
    INVALID_AUTH_USER(UNAUTHORIZED, "권한이 없는 유저입니다."),

    /**
     * 404 NOT_FOUND : 리소스를 찾을 수 없음
     * <br>
     * 참고 : https://tecoble.techcourse.co.kr/post/2020-08-31-http-status-code/
     *
     */
    USER_NOT_FOUND(NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다."),
    NOTICE_NOT_FOUND(NOT_FOUND, "해당 공지사항이 존재하지 않습니다."),
    DATA_NOT_EXIST(NOT_FOUND, "데이터가 존재하지 않습니다."),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(CONFLICT, "데이터가 이미 존재합니다"),
    ALREADY_SOLVE(CONFLICT, "해결이 완료된 질문입니다."),
    DATA_VIOLATION_EXCEPTION(CONFLICT, "제약 조건에 위배된 요청 입니다."),
    ALREADY_ACTIVE(CONFLICT, "이미 활성화 된 글 입니다."),
    ALREADY_INACTIVE(CONFLICT, "이미 비활성화 된 글 입니다."),
//    INACTIVE_RESOURCE(CONFLICT, "비활성 처리 된 글입니다."),
    INACTIVE_NOTICE(CONFLICT, "비활성 처리 된 공지사항입니다."),

    /* 500 INTERNAL_SERVER_ERROR : 서버 내부 오류. 웹 서버가 요청사항을 수행할 수 없을 경우 발생  */
    NULL_POINTER(INTERNAL_SERVER_ERROR, "참조 변수에 값이 존재하지 않습니다."),
    INPUT_OUTPUT_EXCEPTION(INTERNAL_SERVER_ERROR, "파일 읽기, 쓰기 실패"),
    AMAZONS_S3_EXCEPTION(INTERNAL_SERVER_ERROR, "S3에 파일이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}
