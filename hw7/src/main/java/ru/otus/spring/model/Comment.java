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

    // Ещё раз обращаю внимание (с прошлого реквеста), что книга без этого не грузится
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "book_id")
    @ManyToOne(targetEntity = Book.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Book book;
}
