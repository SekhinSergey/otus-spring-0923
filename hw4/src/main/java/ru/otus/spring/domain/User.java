package ru.otus.spring.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class User {

    private String surname;

    private String firstName;

    public String getFullName() {
        return surname + " " + firstName;
    }
}
