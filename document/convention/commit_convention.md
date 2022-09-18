# 1. 커밋 메시지 구조

커밋은 `Type(작업도메인): 커밋내용 (#이슈번호)`으로 작성해 주세요.

이슈번호가 없다면 생략해 주세요.

# 2. 커밋 타입

- `Feat` : 새로운 기능 추가
- `Fix` : 버그 수정
- `Docs` : 문서 작성, 수정
- `Refactor` : 코드 리팩토링
- `Test` : 테스트 코드
- `Style` : 서식(줄 바꿈, 세미콜론 누락, 오타 수정)
- `Chore` : 잡일(빌드 변경, 패키지 구조 변경), 간략한 코드 수정(메서드, 변수 명 변경)

# 3. 주의 사항

 - `Type`의 맨 앞 글자는 대문자
 - `: `뒤와 ` (#이슈번호)`의 앞에는 띄어쓰기

# 4. 커밋 예시

- ex) `Docs(document): commit_convention.md 작성 (#115)`
- ex) `Style(notice): NoticeService.class 오타 수정`

## 참고
[AngularJS commit convention](https://gist.github.com/stephenparish/9941e89d80e2bc58a153)