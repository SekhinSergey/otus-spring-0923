package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.User;
import ru.otus.spring.io.LocalizedIOService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String SURNAME_REQUEST = "UserService.surname.request.message";

    private static final String FIRSTNAME_REQUEST = "UserService.firstname.request.message";

    private final LocalizedIOService localizedIoService;

    public User getUser() {
        User user = new User();
        localizedIoService.skipPrintLn();
        localizedIoService.localizedPrintLn(SURNAME_REQUEST);
        user.setSurname(localizedIoService.readLine());
        localizedIoService.localizedPrintLn(FIRSTNAME_REQUEST);
        user.setFirstName(localizedIoService.readLine());
        return user;
    }
}
