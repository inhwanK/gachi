package org.deco.gachicoding.post.question.domain.repository;

import org.deco.gachicoding.post.question.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

//    @Query("SELECT q " +
//            "FROM Question q JOIN FETCH q.questioner " +
//            "JOIN q.answers a ON a.ansLocked = true " +
//            "WHERE q.queIdx = :queIdx ")
//    Optional<Question> findQuestionByIdx(@Param("queIdx") Long queIdx);

//    @Query("SELECT DISTINCT q " +
//            "FROM Question q JOIN FETCH q.questioner " +
//            "WHERE q.queEnabled = true " +
//            "AND (q.queTitle.queTitle LIKE %:keyword% " +
//            "OR q.queContents.queContents LIKE %:keyword%) ")
//    List<Question> findAllQuestionByKeyword(@Param("keyword") String keyword, Pageable pageable);


    // 특정 키워드를 포함하는 질문 검색
    @Query("select q " +
            "from Question q " +
            "where q.queContents.queGeneralContent like %:keyword%")
    Page<Question> searchQuestionByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // 질문 상세로 들어갈 때는 답변도 바로 나왔으면 좋겠음... 따라서 패치조인 사용하기
    // 패치조인 사용하지 않는 것과 성능 차이 측정
    @Query("select q " +
            "from Question q join fetch Answer a " +
            "where q.queIdx = :queIdx")
    Optional<Question> findQuestionDetail(@Param("queIdx") Long queIdx);
}
