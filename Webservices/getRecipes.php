<?php
	require "dbConfig.php";

	$query = "SELECT * FROM recipesimages RIGHT JOIN recipes ON recipes.IDRecipes = recipesimages.IDRecipes";

	$data = mysqli_query($connect, $query);


	class Recipes{
		function __construct($IDRecipes, $RecipesAuthor, $RecipesTitle, $RecipesContent, $NutritionalIngredients, $Ingredients, $Steps, $Time, $IDRecipesImages, $RecipesImages, $Status){
			$this -> IDRecipes = $IDRecipes;
			$this -> RecipesAuthor = $RecipesAuthor;
			$this -> RecipesTitle = $RecipesTitle;
			$this -> RecipesContent = $RecipesContent;
			$this -> NutritionalIngredients = $NutritionalIngredients;
			$this -> Ingredients = $Ingredients;
			$this -> Steps = $Steps;
			$this -> Time = $Time;
			$this -> IDRecipesImages = $IDRecipesImages;
			$this -> RecipesImages = $RecipesImages;
			$this -> Status = $Status;
			
		}
	}
	///Tạo mảng
	$recipes_array = array();

	//Thêm phần tử vào màng
	while ($row = mysqli_fetch_assoc($data)){
		array_push($recipes_array, new Recipes($row ['IDRecipes'], $row ['RecipesAuthor'], $row ['RecipesTitle'],  $row ['RecipesContent'], $row ['NutritionalIngredients'], $row ['Ingredients'], $row ['Steps'], $row ['Time'], $row ['IDRecipesImages'], $row ['RecipesImages'], $row ['Status']));
	}

	//Chuyển định dạng mảng qua json
	echo json_encode($recipes_array)
?>