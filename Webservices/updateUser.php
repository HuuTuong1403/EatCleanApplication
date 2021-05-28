<?php
	require "dbConfig.php";

	$IDUser = $_GET['IDUser'];
	$Email = $_POST['Email'];
	$FullName = $_POST['FullName'];	

	$query = " UPDATE users SET Email = '$Email', 
								FullName = '$FullName'
							WHERE IDUser = '$IDUser' ";
							
	$response = array();
	if (mysqli_query($connect, $query)){
		//Thành công
		$response = array(
            'message' => 'Thành công'
        );
	}else{
		//lỗi
		$response = array(
            'message' => 'Thất bại'
        );
	}
	echo json_encode($response);
?>