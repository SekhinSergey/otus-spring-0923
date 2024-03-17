package ru.otus.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.model.Comment;

import java.util.List;
import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByBookId(long bookId);

    void deleteAllByBookId(long bookId);

    void deleteAllByBookIdIn(Set<Long> bookIds);

    int countByBookId(long bookId);
}
