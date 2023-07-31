--create table employee
--(
--    id integer, -- ID сотрудника
--    department_id integer, -- ID подразделения, в котором работает сотрудник
--    chief_flg boolean, -- флаг того, что сотрудник занимает руководящую позицию
--    birth_dt date -- дата рождения
--)
--
--
--Вывести список сотрудников, которые старше своего непосредственного руководителя.

CREATE TABLE employee
(
id INT,
department_id INT,
chief_flg BIT(1),
birth_dt DATE
);

-- шеф
INSERT INTO employee (id, department_id, chief_flg, birth_dt) VALUES (1, 1, 1, '1987-05-03');
INSERT INTO employee (id, department_id, chief_flg, birth_dt) VALUES (2, 1, 0, '1986-01-03');
INSERT INTO employee (id, department_id, chief_flg, birth_dt) VALUES (3, 1, 0, '1988-02-03');
INSERT INTO employee (id, department_id, chief_flg, birth_dt) VALUES (4, 1, 0, '1991-09-03');

INSERT INTO employee (id, department_id, chief_flg, birth_dt) VALUES (5, 2, 1, '1995-12-03');
INSERT INTO employee (id, department_id, chief_flg, birth_dt) VALUES (5, 2, 0, '1998-12-03');
INSERT INTO employee (id, department_id, chief_flg, birth_dt) VALUES (6, 2, 0, '1934-12-03');
INSERT INTO employee (id, department_id, chief_flg, birth_dt) VALUES (7, 2, 0, '1949-12-03');

WITH chiefs_births AS (
    SELECT MAX(birth_dt) as birth_dt, department_id FROM (SELECT * FROM employee WHERE chief_flg = 1)
    GROUP BY department_id
)
SELECT e.* from employee e
JOIN chiefs_births cb ON cb.department_id = e.department_id AND e.birth_dt < cb.birth_dt AND e.chief_flg = 0

SELECT e.* from employee e
JOIN (SELECT MAX(birth_dt) as birth_dt, department_id FROM (SELECT * FROM employee WHERE chief_flg = 1)
          GROUP BY department_id) cb ON cb.department_id = e.department_id AND e.birth_dt < cb.birth_dt AND e.chief_flg = 0