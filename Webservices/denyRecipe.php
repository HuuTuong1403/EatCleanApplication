<?php
	require "dbConfig.php";

	$IDRecipes = $_GET['IDRecipes'];
	
	$Status = "denied";

	$query = " UPDATE recipes SET Status = '$Status'
							WHERE IDRecipes = '$IDRecipes' ";
							
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