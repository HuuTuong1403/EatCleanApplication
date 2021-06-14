<?php
	require "dbConfig.php";

	$IDBlogImages = $_GET['IDBlogImages'];
	$BlogImages = $_POST['BlogImages'];

	$query = " UPDATE blogimages SET BlogImages = '$BlogImages'
							WHERE IDBlogImages = '$IDBlogImages' ";
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
