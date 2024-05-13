package ru.otus.spring.security.component;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.exception.NotFoundException;
import ru.otus.spring.repository.UserRepository;
import ru.otus.spring.security.config.UserDetailsImpl;

import static ru.otus.spring.constant.Constants.NO_USER_BY_EMAIL_ERROR_MESSAGE;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) {
        return repository
                .findByEmail(email)
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new NotFoundException(NO_USER_BY_EMAIL_ERROR_MESSAGE.formatted(email)));
    }
}
