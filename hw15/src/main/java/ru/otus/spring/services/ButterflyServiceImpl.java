package ru.otus.spring.services;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Butterfly;
import ru.otus.spring.domain.Caterpillar;

@Service
public class ButterflyServiceImpl implements ButterflyService {
    @Override
    public Butterfly transform(Caterpillar caterpillar) {
        return new Butterfly(caterpillar.nickname());
    }
}
