<?php
	require "dbConfig.php";

	$IDRecipesImages = $_GET['IDRecipesImages'];
	$RecipesImages = $_POST['RecipesImages'];

	$query = " UPDATE recipesimages SET RecipesImages = '$RecipesImages'
							WHERE IDRecipesImages = '$IDRecipesImages' ";
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
