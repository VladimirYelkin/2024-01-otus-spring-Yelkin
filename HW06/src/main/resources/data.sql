MERGE INTO AUTHORS A
    USING (VALUES ('Author_1'),
                  ('Author_2'),
                  ('Author_2')) S(V)
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

MERGE INTO COMMENTS C
    USING (VALUES ('BookComment_1_1', 1),
                  ('BookComment_1_2', 1),
                  ('BookComment_2_1', 2)) S(V,BOOK_ID)
ON C.FULL_TEXT = S.V
WHEN NOT MATCHED THEN INSERT (FULL_TEXT,BOOK_ID) VALUES (S.V,S.BOOK_ID);