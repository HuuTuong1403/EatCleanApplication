<?php
	require "dbConfig.php";

	$IDComment = $_GET['IDComment'];


	

	
	$query_add = "DELETE FROM comments WHERE IDComment = '$IDComment'";

	$response = array();

	if (mysqli_query($connect, $query_add)){
				//Thành công
		$response = array(
            'message' => 'Thành công'
        );

	}
	else{
		$response = array(
            'message' => 'Thất bại'
        );
	}
	echo json_encode($response);
?>