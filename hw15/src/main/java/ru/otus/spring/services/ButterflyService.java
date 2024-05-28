package ru.otus.spring.services;

import ru.otus.spring.domain.Butterfly;
import ru.otus.spring.domain.Caterpillar;

public interface ButterflyService {

    Butterfly transform(Caterpillar caterpillar);
}
