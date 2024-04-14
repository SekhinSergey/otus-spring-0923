package ru.otus.spring.mongock;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.mongodb.client.MongoDatabase;

import java.util.Set;

import static ru.otus.spring.utils.EntityUtils.getFifthGenre;
import static ru.otus.spring.utils.EntityUtils.getFirstAuthor;
import static ru.otus.spring.utils.EntityUtils.getFirstBook;
import static ru.otus.spring.utils.EntityUtils.getFirstComment;
import static ru.otus.spring.utils.EntityUtils.getFirstGenre;
import static ru.otus.spring.utils.EntityUtils.getFourthGenre;
import static ru.otus.spring.utils.EntityUtils.getSecondAuthor;
import static ru.otus.spring.utils.EntityUtils.getSecondBook;
import static ru.otus.spring.utils.EntityUtils.getSecondComment;
import static ru.otus.spring.utils.EntityUtils.getSecondGenre;
import static ru.otus.spring.utils.EntityUtils.getSixthGenre;
import static ru.otus.spring.utils.EntityUtils.getThirdAuthor;
import static ru.otus.spring.utils.EntityUtils.getThirdBook;
import static ru.otus.spring.utils.EntityUtils.getThirdComment;
import static ru.otus.spring.utils.EntityUtils.getThirdGenre;

@ChangeLog
@SuppressWarnings("all")
public class Changelog {

    @ChangeSet(author = "SekhinSergey", id = "dropDatabase", order = "001", runAlways = true)
    public void dropDatabase(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(author = "SekhinSergey", id = "createData", order = "002", runAlways = true)
    public void createData(MongockTemplate mongoTemplate) {
        mongoTemplate.insertAll(Set.of(getFirstAuthor(), getSecondAuthor(), getThirdAuthor()));

        mongoTemplate.insertAll(Set.of(
                getFirstGenre(),
                getSecondGenre(),
                getThirdGenre(),
                getFourthGenre(),
                getFifthGenre(),
                getSixthGenre()));

        mongoTemplate.insertAll(Set.of(getFirstBook(), getSecondBook(), getThirdBook()));

        mongoTemplate.insertAll(Set.of(getFirstComment(), getSecondComment(), getThirdComment()));
    }
}
