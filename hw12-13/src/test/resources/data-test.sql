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

MERGE INTO users (id, email, roles, password) KEY(id)
    VALUES (1, 'mail1@mail.com', 'ROLE_1', 'Password_1'),
           (2, 'mail2@mail.com', 'ROLE_2', 'Password_2'),
           (3, 'mail3@mail.com', 'ROLE_3', 'Password_3');
ALTER TABLE users ALTER COLUMN id RESTART WITH 4;

MERGE INTO comments(id, text, book_id, user_id) KEY(id)
    VALUES (1, 'Comment_1', 1, 1), (2, 'Comment_2', 2, 2), (3, 'Comment_3', 3, 3);
ALTER TABLE comments ALTER COLUMN id RESTART WITH 4;

MERGE INTO tokens (id, user_id, token, revoked) KEY(id)
    VALUES (1, 1, 'Token_1', false),
           (2, 2, 'Token_2', false),
           (3, 3, 'Token_3', false);
ALTER TABLE users ALTER COLUMN id RESTART WITH 4;
