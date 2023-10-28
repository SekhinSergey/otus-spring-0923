package ru.otus.spring.domain;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
@NoArgsConstructor
public class User {

    private String surname;

    private String firstName;

    public String getFullName() {
        return surname + " " + firstName;
    }
}
