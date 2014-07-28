mysql -h 10.73.43.123 -u shard_practice -pdb1004 shard_practice < global.sql
mysql -h 10.73.45.50 -u shard_practice -pdb1004 shard_practice < shard_0.sql
mysql -h 10.73.45.54 -u shard_practice -pdb1004 shard_practice < shard_1.sql
