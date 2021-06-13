<?php
	require "dbConfig.php";

	$IDBlogImages = $_POST['IDBlogImages'];
	$BlogImages = $_POST['BlogImages'];
	$IDBlog = $_POST['IDBlog'];

	$query_add = "INSERT INTO blogimages VALUES ('$IDBlogImages', '$BlogImages', '$IDBlog')";

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
