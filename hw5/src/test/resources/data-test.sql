insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

-- Очень долго гуглил, но не понимаю, как поменять на merge. Остановился на этом
-- merge into authors a using books b on b.author_id = a.id when not matched then insert values (a.id, 'Some_Author');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);