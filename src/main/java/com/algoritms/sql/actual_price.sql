-- --------------

-- Даны две таблицы:
--   item (id, name)
--   item_price (item_id, price, date)
-- И произвольная фиксированная дата:
--   {dt} = '2020-02-01'

-- Таблица item содержит id товара и его название.

-- Таблица item_price содержит id товара и его цену на указанную дату.
-- Если в какой-то день появляется новый товар или меняется цена у старого, в таблицу добавляется новая строка об этом товаре вместе с новой ценой и датой изменения. Старые записи никогда не меняются.
-- Если цена по товару в какой-то день не меняется, за этот день по данному товару никакой информации в таблицу не добавляется.

-- Нужно написать select-запрос, который выведет все товары и их цены, актуальные на заданную дату {dt}.

-- +----+------+
-- | id | name |
-- +----+------+
-- |  1 | a    |
-- |  2 | b    |
-- |  3 | c    |
-- |  4 | d    |
-- +----+------+

CREATE TABLE item
(
id INT,
name VARCHAR
);

INSERT INTO item (id, name) VALUES (1, 'a');
INSERT INTO item (id, name) VALUES (2, 'b');
INSERT INTO item (id, name) VALUES (3, 'c');
INSERT INTO item (id, name) VALUES (4, 'd');

-- +---------+------------+-------+
-- | item_id |     dt     | price |
-- +---------+------------+-------+
-- |       1 | 2020-01-01 |   100 |
-- |       1 | 2020-01-15 |   150 |
-- |       1 | 2020-02-01 |    50 |
-- |       2 | 2020-01-01 |   120 |
-- |       2 | 2020-02-05 |   100 |
-- |       2 | 2020-02-28 |    70 |
-- |       3 | 2020-01-10 |    50 |
-- |       3 | 2020-02-01 |    55 |
-- |       3 | 2020-03-01 |   100 |
-- +---------+------------+-------+

CREATE TABLE item_price
(
item_id INT,
dt DATE,
price INT
);

INSERT INTO item_price (item_id, dt, price) VALUES (1, '2020-01-01', 100);
INSERT INTO item_price (item_id, dt, price) VALUES (1, '2020-01-15', 150);
INSERT INTO item_price (item_id, dt, price) VALUES (1, '2020-02-01', 50);
INSERT INTO item_price (item_id, dt, price) VALUES (2, '2020-01-01', 120);
INSERT INTO item_price (item_id, dt, price) VALUES (2, '2020-02-05', 100);
INSERT INTO item_price (item_id, dt, price) VALUES (2, '2020-02-28', 70);
INSERT INTO item_price (item_id, dt, price) VALUES (3, '2020-01-10', 50);
INSERT INTO item_price (item_id, dt, price) VALUES (3, '2020-02-01', 55);
INSERT INTO item_price (item_id, dt, price) VALUES (3, '2020-03-01', 100);

with latest_price AS (SELECT * FROM
    (
        select
        item_id,
        dt,
        price,
        ROW_NUMBER() OVER (PARTITION BY item_id ORDER BY dt DESC) AS rn
        FROM item_price
        WHERE dt <= '2020-02-01'
    ) WHERE rn = 1
)
select p.*, i.name from latest_price p
inner join item i
on p.item_id = i.id;
