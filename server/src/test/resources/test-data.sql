INSERT INTO users
VALUES (DEFAULT, 'userFirst', 'userFirst@mail.ru'),
       (DEFAULT, 'userSecond', 'userSecond@mail.ru'),
       (DEFAULT, 'userThird', 'userThird@mail.ru');

INSERT INTO requests
VALUES (DEFAULT, 'needSecondItem', '2024-01-01 10:00:00', 2);

INSERT INTO items
VALUES (DEFAULT, 'itemFirst', 'itemFirstDescription', true, 1, null),
       (DEFAULT, 'itemSecond', 'itemSecondDescription', false, 1, 1);

INSERT INTO bookings
VALUES (DEFAULT, '2024-01-01 11:00:00', '2024-01-01 11:01:00',
        2, 2, 'WAITING'),
       (DEFAULT, '2024-01-01 11:00:00', '2024-01-01 11:10:00',
        1, 3, 'APPROVED'),
       (DEFAULT, '2000-01-01 11:00:00', '2000-01-01 12:00:00',
        1, 3, 'APPROVED'),
       (DEFAULT, '2075-01-01 11:00:00', '2075-01-01 12:00:00',
        1, 3, 'APPROVED'),
       (DEFAULT, '2025-01-01 11:00:00', '2025-01-01 11:00:50',
        2, 3, 'REJECTED');

INSERT INTO comments
VALUES (DEFAULT, 'niceItem', 1, 3, '2024-01-01 11:50:00');
