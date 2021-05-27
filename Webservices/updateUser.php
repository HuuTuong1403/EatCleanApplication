<?php
	require "dbConfig.php";

	$IDUser = $_POST['IDUser'];
	$Email = $_POST['Email'];
	$Password = $_POST['Password'];
	$FullName = $_POST['FullName'];
	$Image = $_POST['Image'];
	$Username = $_POST['Username'];

	$query = " UPDATE users SET IDUser = '$IDUser', Email = '$Email', 
								Password = '$Password' , FullName = '$FullName',
								Image = '$Image' , Username = '$Username' 
							WHERE  IDUser = '$IDUser' ";
							
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