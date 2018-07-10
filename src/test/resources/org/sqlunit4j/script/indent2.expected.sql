select * from table2;
  select *,? extra from table;
  +-+-+-----+
  |X|Y|EXTRA|
  +-+-+-----+
  |A|1|Aaa  |
  +-+-+-----+


  select *,? extra from table;
  +-+-+-----+
  |X|Y|EXTRA|
  +-+-+-----+
  |A|1|Bbb  |
  +-+-+-----+


  select *,? extra from table;
  +-+-+-----+
  |X|Y|EXTRA|
  +-+-+-----+
  |A|1|Ccc  |
  +-+-+-----+


select * from table;
+-+-+
|X|Y|
+-+-+
|A|1|
+-+-+

