<?php
	require "dbConfig.php";

	$IDUser = $_POST['IDUser'];
	$IDRecipes	= $_POST['IDRecipes'];
	$Description = "";

	class users{
		function __construct($IDUser, $IDRecipes, $Description){
			$this -> IDUser = $IDUser;
			$this -> IDRecipes = $IDRecipes;
			$this -> Description = $Description;		
		}
	}

	$query_add = " INSERT INTO favoriterecipes VALUES ('$IDRecipes','$IDUser', '$Description') ";

    $response = array();
	if (mysqli_query($connect, $query_add)){
			//Thành công
		$response = array(
		    'message' => 'Bạn thêm món ăn vào mục yêu thích thành công'
	    );
	}else{
			//lỗi
		$response = array(
            'message' => 'Bạn thêm món ăn vào mục yêu thích không thành công'
        );
	}
	echo json_encode($response);
?>	