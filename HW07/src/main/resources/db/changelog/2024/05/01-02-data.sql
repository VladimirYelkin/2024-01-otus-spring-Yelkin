--liquibase formatted sql

--changeset VLadimirYelkin:2024-05-01

MERGE INTO AUTHORS A
    USING (VALUES ('Author_1'),
                  ('Author_2'),
                  ('Author_3')) S(V)
    ON A.FULL_NAME = S.V
    WHEN NOT MATCHED THEN INSERT (FULL_NAME) VALUES (S.V);

MERGE INTO GENRES G
    USING (VALUES ('Genre_1'),
                  ('Genre_2'),
                  ('Genre_3'),
                  ('Genre_4'),
                  ('Genre_5'),
                  ('Genre_6')) S(V)
    ON G.NAME = S.V
    WHEN NOT MATCHED THEN INSERT (NAME) VALUES (S.V);

MERGE INTO BOOKS B
    USING (VALUES ('BookTitle_1', 3),
                  ('BookTitle_2', 1),
                  ('BookTitle_3', 2)) S(V,AUTHOR_ID)
    ON B.TITLE = S.V
    WHEN NOT MATCHED THEN INSERT (TITLE,AUTHOR_ID) VALUES (S.V,S.AUTHOR_ID);

MERGE INTO BOOKS_GENRES BG
    USING ( values (1, 1), (1, 2),
                   (2, 3), (2, 4),
                   (3, 5), (3, 6)) S(BOOK_ID,GENRE_ID)
    ON (BG.BOOK_ID=S.BOOK_ID AND BG.GENRE_ID=S.GENRE_ID)
    WHEN NOT MATCHED THEN INSERT (BOOK_ID,GENRE_ID) VALUES (S.BOOK_ID,S.GENRE_ID);

MERGE INTO COMMENTS CM
    USING (values ('Comment1_ForBook1', 1),
                  ('Comment2_ForBook1', 1),
                  ('Comment1_ForBook2', 2),
                  ('Comment2_ForBook2', 2),
                  ('Comment1_ForBook3', 3),
                  ('Comment2_ForBook3', 3)) S (COMMENT, BOOK_ID)
ON (CM.FULL_TEXT = S.COMMENT AND CM.BOOK_ID = S.BOOK_ID)
WHEN NOT MATCHED THEN
    INSERT (BOOK_ID, FULL_TEXT)
    VALUES (S.BOOK_ID, S.COMMENT);


