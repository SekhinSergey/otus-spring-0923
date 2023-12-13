merge into authors(id, full_name) key(id)
values (1, 'Author_1'), (2, 'Author_2'), (3, 'Author_3');
ALTER TABLE authors ALTER COLUMN id RESTART WITH 4;

merge into genres(id, name) key(id)
values (1, 'Genre_1'), (2, 'Genre_2'), (3, 'Genre_3'),
       (4, 'Genre_4'), (5, 'Genre_5'), (6, 'Genre_6');
ALTER TABLE genres ALTER COLUMN id RESTART WITH 7;

insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

merge into books_genres(book_id, genre_id) key(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);

merge into comments(id, text, book_id) key(id)
values (1, 'Good', 1), (2, 'Bad', 2), (3, 'I want to read', 3);
ALTER TABLE comments ALTER COLUMN id RESTART WITH 4;