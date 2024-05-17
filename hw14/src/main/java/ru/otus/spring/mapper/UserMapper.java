package ru.otus.spring.mapper;

import org.mapstruct.Mapper;
import ru.otus.spring.model.jpa.User;
import ru.otus.spring.model.mongo.UserDoc;

@Mapper
public interface UserMapper {
    UserDoc toDoc(User user);
}
