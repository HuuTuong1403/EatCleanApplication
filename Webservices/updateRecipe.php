<?php
	require "dbConfig.php";

	$IDRecipes = $_GET['IDRecipes'];
	$RecipesTitle = $_POST['RecipesTitle'];
	$RecipesAuthor = $_POST['RecipesAuthor'];	
	$RecipesContent = $_GET['RecipesContent'];
	$NutritionalIngredients = $_POST['NutritionalIngredients'];
	$Ingredients = $_POST['Ingredients'];	
	$Steps = $_GET['Steps'];
	$Time = $_POST['Time'];
	$Status = $_POST['Status'];	

	$query = " UPDATE recipes SET RecipesTitle = '$RecipesTitle', 
								RecipesAuthor = '$RecipesAuthor',
								RecipesContent = '$RecipesContent', 
								NutritionalIngredients = '$NutritionalIngredients',
								Ingredients = '$Ingredients',
								Steps = '$Steps',
								Time = '$Time', 
								Status = '$Status'
							WHERE IDRecipes = '$IDRecipes' ";
							
	$response = array();
	if (mysqli_query($connect, $query)){
		Thành công
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