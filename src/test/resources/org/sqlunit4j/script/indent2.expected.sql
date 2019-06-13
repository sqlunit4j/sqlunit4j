select * from table2;
  select table1.*,? extra from table1;
  +-+-+-----+
  |X|Y|EXTRA|
  +-+-+-----+
  |A|1|Aaa  |
  +-+-+-----+


  select table1.*,? extra from table1;
  +-+-+-----+
  |X|Y|EXTRA|
  +-+-+-----+
  |A|1|Bbb  |
  +-+-+-----+


  select table1.*,? extra from table1;
  +-+-+-----+
  |X|Y|EXTRA|
  +-+-+-----+
  |A|1|Ccc  |
  +-+-+-----+


select * from table1;
+-+-+
|X|Y|
+-+-+
|A|1|
+-+-+

