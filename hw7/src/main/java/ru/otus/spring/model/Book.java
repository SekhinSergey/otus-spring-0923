package ru.otus.spring.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")
@SuppressWarnings("all")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    // Получал рекомендацию удалить. Якобы это по умолчанию есть, хотя это не так, в чем убеждался неоднократно
    // Параллельно с этим дана рекомендация добавить граф для автора
    // Граф не позволяет получить автора книги в комментарии. Указания аннотации графа над каждым методом засоряет код
    // Поэтому рекомендации проигнорированы
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "author_id")
    @ManyToOne(targetEntity = Author.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Author author;

    @JoinTable(
            name = "books_genres",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    @OneToMany(targetEntity = Genre.class, fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private List<Genre> genres;
}
