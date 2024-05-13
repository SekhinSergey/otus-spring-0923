MERGE INTO authors(id, full_name) KEY(id)
    VALUES (1, 'Author_1'), (2, 'Author_2'), (3, 'Author_3');

MERGE INTO genres(id, NAME) KEY(id)
    VALUES (1, 'Genre_1'), (2, 'Genre_2'), (3, 'Genre_3'), (4, 'Genre_4'), (5, 'Genre_5'), (6, 'Genre_6');

INSERT INTO books(title, author_id)
    VALUES ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

MERGE INTO books_genres(book_id, genre_id) KEY(book_id, genre_id)
    VALUES (1, 1),   (1, 2),
           (2, 3),   (2, 4),
           (3, 5),   (3, 6);
