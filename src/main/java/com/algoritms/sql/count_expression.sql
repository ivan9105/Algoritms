CREATE TABLE table1
(
id1 int,
id2 varchar(255)
);

insert into table1 (id1, id2) values (1, 'a');
insert into table1 (id1, id2) values (2, 'b');
insert into table1 (id1, id2) values (3, NULL);
insert into table1 (id1, id2) values (4, 'c');
insert into table1 (id1, id2) values (5, 'd');
insert into table1 (id1, id2) values (NULL, 'e');
insert into table1 (id1, id2) values (NULL, 'f');

select
  count(*),     -- 7
  count(1),     -- 5
  count(case when id1 = 1 then '1' else '' end),
  count(10),    -- error
  count(id1),    -- 5
  count('abc'), -- 7
  count(NULL)   -- 2
from table1;