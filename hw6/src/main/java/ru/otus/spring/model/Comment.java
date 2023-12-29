package ru.otus.spring.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("all")
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "book_id")
    @ManyToOne(targetEntity = Book.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Book book;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        Comment comment = (Comment) o;
        if (o == null || getClass() != o.getClass() || id != comment.id || !text.equals(comment.text)) {
            return false;
        }
        return book.equals(comment.book);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (book != null ? book.hashCode() : 0);
        return result;
    }
}
