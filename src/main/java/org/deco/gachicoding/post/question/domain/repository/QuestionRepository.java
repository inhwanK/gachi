package org.deco.gachicoding.post.question.domain.repository;

import org.deco.gachicoding.post.question.domain.Question;
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


    @Query("select q " +
            "from Question q join fetch q.answers")
    List<Question> searchQuestionByContent();
}
