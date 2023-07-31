-- Написать запрос, считающий нарастающий итог по каждому id в хронологическом порядке.

-- Input:
-- Table T
-- +----+------------+-------+
-- | id |     dt     | value |
-- +----+------------+-------+
-- |  1 | 2020-01-01 |    10 |
-- |  1 | 2020-01-15 |    15 |
-- |  1 | 2020-02-01 |     5 |
-- |  2 | 2020-01-01 |    12 |
-- |  2 | 2020-02-05 |    10 |
-- |  2 | 2020-02-28 |     5 |
-- |  3 | 2020-01-10 |     5 |
-- |  3 | 2020-02-01 |     5 |
-- |  3 | 2020-03-01 |    10 |
-- +----+------------+-------+

CREATE TABLE table2
(
id INT,
dt DATE,
value1 INT
);

INSERT INTO table2 (id, dt, value1) VALUES (1, '2020-01-01', 10);
INSERT INTO table2 (id, dt, value1) VALUES (1, '2020-01-15', 15);
INSERT INTO table2 (id, dt, value1) VALUES (1, '2020-02-01', 5);
INSERT INTO table2 (id, dt, value1) VALUES (2, '2020-01-01', 12);
INSERT INTO table2 (id, dt, value1) VALUES (2, '2020-02-05', 10);
INSERT INTO table2 (id, dt, value1) VALUES (2, '2020-02-28', 5);
INSERT INTO table2 (id, dt, value1) VALUES (3, '2020-01-10', 5);
INSERT INTO table2 (id, dt, value1) VALUES (3, '2020-02-01', 5);
INSERT INTO table2 (id, dt, value1) VALUES (3, '2020-03-01', 10);

select id, dt, value1,
sum(value1) over (partition by id order by dt ASC) as chronologic_sum
from table2
