<?php
	require "dbConfig.php";

	$IDRecipes = $_POST['IDRecipes'];
	$RecipesTitle = $_POST['RecipesTitle'];
	$RecipesAuthor = $_POST['RecipesAuthor'];
	$RecipesContent =  $_POST['RecipesContent'];
	$NutritionalIngredients = $_POST['NutritionalIngredients'];
	$Ingredients = $_POST['Ingredients'];
	$Steps = $_POST['Steps'];
	$Time = $_POST['Time'];
	$Status = $_POST['Status'];
	$createTime = $_POST['createTime'];

	class Receipes {
		function __construct($IDRecipes, $RecipesTitle ,$RecipesAuthor, $RecipesContent, $NutritionalIngredients, $Ingredients, $Steps, $Time, $Status, $createTime){
			$this -> IDRecipes = $IDRecipes;
			$this -> RecipesAuthor = $RecipesAuthor;
			$this -> RecipesTitle = $RecipesTitle;
			$this -> RecipesContent = $RecipesContent;
			$this -> NutritionalIngredients = $NutritionalIngredients;
			$this -> Ingredients = $Ingredients;
			$this -> Steps = $Steps;
			$this -> Time = $Time;
			$this -> Status = $Status;
			$this -> createTime = $createTime;
		}
	}
	$query_add = "INSERT INTO recipes VALUES ('$IDRecipes', '$RecipesTitle', '$RecipesAuthor', '$RecipesContent', '$NutritionalIngredients', '$Ingredients', '$Steps', '$Time', '$Status', '$createTime')";

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