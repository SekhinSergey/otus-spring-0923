package ru.otus.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.model.Comment;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByText(String text);

    List<Comment> findAllByBookId(long bookId);

    List<Comment> findAllByBookTitle(String title);

    void deleteAllByBookId(long bookId);

    void deleteAllByBookIdIn(Set<Long> bookIds);

    void deleteAllByBookTitle(String title);

    int countByBookId(long bookId);

    int countByBookTitle(String title);
}
