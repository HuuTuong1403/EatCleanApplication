<?php
	require "dbConfig.php";

	$IDRecipes = $_POST['IDRecipes'];
	$IDRecipesImages = $_POST['IDRecipesImages'];
	$RecipesImages = $_POST['RecipesImages'];

	$query_add = "INSERT INTO recipesimages VALUES ('$IDRecipesImages', '$RecipesImages', '$IDRecipes')";

	$response = array();

	if (mysqli_query($connect, $query_add)){
				//Thành công
		$response = array(
            'message' => 'Thành công'
        );

	}
	else{
		$response = array(
            'message' => 'Thất bại'
        );
		
	}
	echo json_encode($response);

?>
