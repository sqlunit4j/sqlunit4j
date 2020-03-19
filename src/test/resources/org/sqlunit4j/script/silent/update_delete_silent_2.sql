--@Silent
connection2:update TABLE2 set Col1='Zzz' where Col1='Aaa';
--@Silent
connection2:delete from TABLE2 where Col1='Bbb';
--@Silent
connection2:select * from table2;
--@Silent
connection2:call simpleProc('');