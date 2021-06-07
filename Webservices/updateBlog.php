<?php
	require "dbConfig.php";

	$IDBlog = $_GET['IDBlog'];
	$BlogTitle = $_POST['BlogTitle'];
	$BlogAuthor = $_POST['BlogAuthor'];	
	$BlogContent = $_POST['BlogContent'];
	$Status = $_POST['Status'];

	$query = " UPDATE blogs SET BlogTitle = '$BlogTitle', 
								BlogAuthor = '$BlogAuthor',
								BlogContent = '$BlogContent', 
								Status = '$Status'
							WHERE IDBlog = '$IDBlog' ";
							
	$response = array();
	if (mysqli_query($connect, $query)){
		//Thành công
		$response = array(
            'message' => 'Thành công'
        );
	}else{
		echo "fail";
		//lỗi
		$response = array(
             'message' => 'Thất bại'
       );
	}
	echo json_encode($response);
?>