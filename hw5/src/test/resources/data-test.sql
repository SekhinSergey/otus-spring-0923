merge into authors(id, full_name) key(id)
values (1, 'Author_1'), (2, 'Author_2'), (3, 'Author_3');

merge into genres(id, name) key(id)
values (1, 'Genre_1'), (2, 'Genre_2'), (3, 'Genre_3'),
       (4, 'Genre_4'), (5, 'Genre_5'), (6, 'Genre_6');

insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

merge into books_genres(book_id, genre_id) key(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);