package ru.otus.spring.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, unique = true)
    private String fullName;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        Author author = (Author) o;
        if (o == null || getClass() != o.getClass() || id != author.id) {
            return false;
        }
        return fullName.equals(author.fullName);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        return result;
    }
}
