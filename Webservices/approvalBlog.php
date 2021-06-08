<?php
	require "dbConfig.php";

	$IDBlog = $_GET['IDBlog'];
	
	$Status = "approval";

	$query = " UPDATE blogs SET Status = '$Status'
							WHERE IDBlog = '$IDBlog' ";
							
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