update TABLE2 set Col1='Zzz' where Col1='Aaa';
1 records updated/deleted

delete from TABLE2 where Col1='Bbb';
1 records updated/deleted

select * from table2;
+----+
|COL1|
+----+
|Ccc |
|Zzz |
+----+

