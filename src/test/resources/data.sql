INSERT INTO BLOG(id, title, text)
VALUES  (2, 'Test Title2', 'Test Text2'),
        (3, 'Test Title3','Test Tile3');

INSERT INTO TAG(id,name)
VALUES (2,'Test Tag2'),
       (3,'Test Tag3'),
       (4,'Test Tag4');

INSERT INTO blog_tags(blog_id,tag_id)
VALUES (2,2),
       (2,3),
       (3,3),
       (3,4);

