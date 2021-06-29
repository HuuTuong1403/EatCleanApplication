<?php
	require "dbConfig.php";

	$IDUser = $_GET['IDUser'];
	
	$Status = "denied";

	$query = " UPDATE users SET Status = '$Status'
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