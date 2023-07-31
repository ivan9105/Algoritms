-- дана табличка events (user_id, search_ts, location_id, category_id),
-- содержащая данные о поисковых запросах пользователей, 
-- где location - город, по которому идет поиск,
-- category - категория товара, который юзер ищет.
-- Нужно посчитать в разрезе каждого дня четыре запроса:
-- кол-во уникальных пользователей
-- кол-во уникальных юзеров, искавших конкретный товар по всей стране
-- кол-во уникальных юзеров, искавших в конкретном городе любую категорию
-- кол-во уникальных юзеров, искавших конкретный товар в конкретном городе

--сначала надо написать запросы отдельно, сами по себе они несложные,
--но затем встает вопрос, какие проблемы могут возникать со скоростью их выполнения,
--и как все 4 запроса объединить в 1, используя только одну группировку.
--То есть суть задачи не в написании сложного запроса, а в понимании того,
--как запросы используют ресурсы и как их можно оптимизировать

CREATE TABLE events (
    user_id INT,
    search_ts date,
    location_id INT,
    category_id INT
);

INSERT INTO events
    WITH RECURSIVE
        generate_data(user_id, search_ts, location_id, category_id) AS
            (
                SELECT
                    abs(random() % 10),
                    abs(random() % (strftime('%s', '2023-02-08 23:59:59') - strftime('%s', '2023-02-10 00:00:00'))),
                    abs(random() % 300),
                    abs(random() % 25)
                UNION ALL
                    SELECT
                        abs(random() % 10),
                        abs(random() % (strftime('%s', '2023-02-08 23:59:59') - strftime('%s', '2023-02-10 00:00:00'))),
                        abs(random() % 300),
                        abs(random() % 25) FROM generate_data LIMIT 10000000
            )
    SELECT * FROM generate_data

--кол-во уникальный пользователей

SELECT count(*) from (SELECT DISTINCT user_id from events)
SELECT count(DISTINCT user_id) from events

--кол-во уникальных юзеров, искавших конкретный товар по всей стране
--вся страна == все 300 городов

SELECT COUNT(user_id) FROM (
    SELECT user_id, count (location_id) AS cnt FROM (
        SELECT DISTINCT user_id, location_id FROM events
        WHERE category_id = 1
        ORDER by user_id, location_id)
    GROUP BY user_id)
WHERE cnt = 300

-- кол-во уникальных юзеров, искавших в конкретном городе любую категорию
SELECT count(DISTINCT user_id) from events WHERE location_id = 7

-- кол-во уникальных юзеров, искавших конкретный товар в конкретном городе
SELECT count(DISTINCT user_id) from events WHERE location_id = 7 and category_id = 2

--ВСЕ ВМЕСТЕ
SELECT COUNT(user_id) FROM (
    SELECT user_id, count (location_id) AS cnt FROM (
        SELECT DISTINCT user_id, location_id FROM events
        WHERE category_id = 3
        ORDER by user_id, location_id)
    GROUP BY user_id)
WHERE cnt = 300