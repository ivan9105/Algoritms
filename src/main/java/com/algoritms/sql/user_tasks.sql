--1)

create table users (
id int,
name varchar(80),
actual_date timestamp
)

--считаем что данные равномерно распределены по кластерной субд по
--ключу id
--считаем что данные хранятся на диске в сортированном по указанному
--ключу вид

INSERT INTO users (id, name, actual_date) VALUES (1, 'Петя', '2020-01-01');
INSERT INTO users (id, name, actual_date) VALUES (1, 'Олег', '2021-01-01');
INSERT INTO users (id, name, actual_date) VALUES (1, 'Глеб', '2022-01-01');
INSERT INTO users (id, name, actual_date) VALUES (1, 'Ваня', '2023-01-01');

--rows between unbounded preceding and unbounded following - ИЗБАВЛЯЕТ ОТ ДУБЛЕЙ

--Все, что до текущей строки/диапазона и само значение текущей строки
--BETWEEN UNBOUNDED PRECEDING
--BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
--Текущая строка/диапазон и все, что после нее
--BETWEEN CURRENT ROW AND UNBOUNDED FOLLOWING
--С конкретным указазнием сколько строк до и после включать (не поддерживается для RANGE)
--BETWEEN N Preceding AND N Following
--BETWEEN CURRENT ROW AND N Following
--BETWEEN N Preceding AND CURRENT ROW

--RANGE VS ROWS - ROWS это preceding и following относительно текущего значения строки - 1 строка
--RANGE как раз объединяет в диапазоны периоды, с совпадающими значениями полей сортировки - агрегация по всей выборке


select distinct
id,
last_value(name) over (partition by id
order by actual_date
rows between unbounded preceding and unbounded following
) as last_name
from users

--В чем проблема данного решения?

-- Производильность?
-- Например нет смысл указывать unbounded preceding за счет order by DESC - мы итак выбираем верхнюю позицию
--rows between unbounded preceding and unbounded following

--2)

--create table clickstream (
--id int,
--dt timestamp,
--event_type_id int,
--user_id int
--) order by id
--segmented by hash(id) all nodes;

--считаем что данные в таблице равномерно распределены по кластеру по
--  ключу id (автоинкремент)
--  считаем что данные хранятся на диске в сортированном виде по ключу id
--  в таблице 4 млрд событий в день.
--  create table users (
--  user_id int,
--  is_test boolean
--  ) order by user_id
--  segmented by hash(user_id) all nodes;

--  считаем что данные в таблице равномерно распределены по кластеру по
--  ключу user_id
--  считаем что данные хранятся на диске в сортированном виде по ключу
--  user_id
--  в таблице 1 млрд уникальных пользователей, из которых тестовых - 10%
--  в чем проблема решения?


--  select dt::date, count(*)
--  from clickstream c
--  join users u on c.user_id = u.user_id
--  where not u.is_test
--  group by 1
--  order by 1
--  предложите более оптимальный способ

-- проблема count и join

---Нужно собрать количество событий в день. Тестовых пользователей
-- исключить

--TODO можно выполнить это с помощью оконной функции с сортировки и учетом rows unbounded preceding / following