--@Silent
update TABLE2 set Col1='Zzz' where Col1='Aaa';
--@Silent
delete from TABLE2 where Col1='Bbb';
select * from table2;
