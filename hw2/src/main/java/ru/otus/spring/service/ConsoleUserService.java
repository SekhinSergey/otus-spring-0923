package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.User;
import ru.otus.spring.out.IOService;

@Service
@RequiredArgsConstructor
public class ConsoleUserService implements UserService {

    private static final String SURNAME_REQUEST = "What is your surname?";

    private static final String FIRST_NAME_REQUEST = "What is your first name?";

    private final IOService ioService;

    public User getUser() {
        User user = new User();
        ioService.printLn(SURNAME_REQUEST);
        user.setSurname(ioService.readLine());
        ioService.printLn(FIRST_NAME_REQUEST);
        user.setFirstName(ioService.readLine());
        return user;
    }
}
