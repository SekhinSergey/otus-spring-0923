package ru.otus.spring.mapper;

import org.mapstruct.Mapper;
import ru.otus.spring.model.jpa.RefreshToken;
import ru.otus.spring.model.mongo.RefreshTokenDoc;

@Mapper
public interface RefreshTokenMapper {
    RefreshTokenDoc toDoc(RefreshToken refreshToken);
}
