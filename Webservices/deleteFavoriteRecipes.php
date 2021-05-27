<?php
	require "dbConfig.php";

	$IDUser = $_GET['IDUser']; 
	$IDRecipes = $_GET['IDRecipes']; 

	$query = "DELETE FROM favoriterecipes
				WHERE favoriterecipes.IDUser = '$IDUser' and  favoriterecipes.IDRecipes = '$IDRecipes' ";
				
    $response = array();
	if (mysqli_query($connect, $query)){
		//Thành công
		$response = array(
            'message' => 'Bạn đã xóa món ăn trong mục yêu thích thành công'
        );
	}
	else{
	    $response = array(
            'message' => 'Bạn đã xóa món ăn trong mục yêu thích không thành công'
        );
	}
	echo json_encode($response);
?>