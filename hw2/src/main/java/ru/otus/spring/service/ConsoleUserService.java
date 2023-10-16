package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.User;
import ru.otus.spring.out.Printer;

import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class ConsoleUserService implements UserService {

    private static final String SURNAME_REQUEST = "What is your surname?";

    private static final String FIRST_NAME_REQUEST = "What is your first name?";

    private final Printer printer;

    public User getUser() {
        Scanner scanner = new Scanner(System.in);
        User user = new User();
        printer.print(SURNAME_REQUEST);
        user.setSurname(scanner.nextLine());
        printer.print(FIRST_NAME_REQUEST);
        user.setFirstName(scanner.nextLine());
        return user;
    }
}
