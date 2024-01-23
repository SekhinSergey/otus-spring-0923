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
    // Параллельно с этим дана рекомендация добавить граф для авторов
    // В итоге получается абсурдная ситуация. Вдобавок граф увеличивает объем кода и
    // не позволяет получать комментарии с книгами при определенных случаях, поэтому рекомендации проигнорированы
    // То же самое касается графа для комментариев
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "author_id")
    @ManyToOne(targetEntity = Author.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Author author;

    // Получил рекомендацию загрузить жанры "вызовом в цикле метода size". Не понял, о чем речь
    // Видимо, фетч не был замечен
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "books_genres",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    @OneToMany(targetEntity = Genre.class, fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private List<Genre> genres;
}
