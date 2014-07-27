<!DOCTYPE html>
<?php
	$shard1 = '10.73.45.50';
	$shard2 = '10.73.45.54';
	$user = 'shard_practice';
	$pass = 'db1004';
	$db = 'shard_practice';

	// DB connections
	$db1 = new mysqli( $shard1, $user, $pass, $db );
	$db2 = new mysqli( $shard2, $user, $pass, $db );

	if( $db1->connect_errno > 0 )
		die( 'Unable to connect to database' . $db1->connect_error . ']');
	if( $db2->connect_errno > 0 )
		die( 'Unable to connect to database' . $db2->connect_error . ']');
?>
<html>
	<head>
		<title>Galaxy War</title>
		<script>
			setTimeout( function() { location.reload(); }, 1000 );
		</script>
	</head>
	<body>
			<h3>Galaxy status</h3>
		<ul>
<?
// DB SELECT query
$sql = <<<SQL
	SELECT gid, hp
	FROM galaxy
SQL;

// Send query and get results.
if( ! ( $result1 = $db1->query( $sql ) ) )
	die( 'Query error [' . $db1->error .'] ' . $sql );

if( ! ( $result2 = $db2->query( $sql ) ) )
	die( 'Query error [' . $db2->error .'] ' . $sql );

	// Echo results
	$result_count = [ true, true ];
	while( $result_count[0] && $result_count[1] )
	{
		if ( $result_count[0] && $row1 = $result1->fetch_assoc() )
			echo '<li>Galaxy #' . $row1['gid'] . ' : ' . $row1['hp'] . '</li>';
		else $result_count[0] = false;

		if ( $result_count[1] && $row2 = $result2->fetch_assoc() )
			echo '<li>Galaxy #' . $row2['gid'] . ' : ' . $row2['hp'] . '</li>';
		else $result_count[1] = false;
	}
?>
		</ul>
	</body>
</html>
