<?php
	require "dbConfig.php";

	$IDUser = $_GET['IDUser'];
	$Password = $_POST['Password'];	

	$query = " UPDATE users SET Password = '$Password'
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