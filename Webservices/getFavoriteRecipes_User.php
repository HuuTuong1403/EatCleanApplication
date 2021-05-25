<?php
	
	require "dbConfig.php";

	$IDUser = $_GET['IDUser']; 

	$query = "SELECT * FROM favoriterecipes INNER JOIN recipes ON recipes.IDRecipes = favoriterecipes.IDRecipes
						WHERE favoriterecipes.IDUser = '$IDUser' ";

	$data = mysqli_query($connect, $query);
	class Recipes{
		function __construct($IDRecipes, $RecipesAuthor, $RecipesTitle, $RecipesContent, $NutritionalIngredients, $Ingredients, $Steps, $Time, $Status, $IDUser){
			$this -> IDRecipes = $IDRecipes;
			$this -> RecipesAuthor = $RecipesAuthor;
			$this -> RecipesTitle = $RecipesTitle;
			$this -> RecipesContent = $RecipesContent;
			$this -> NutritionalIngredients = $NutritionalIngredients;
			$this -> Ingredients = $Ingredients;
			$this -> Steps = $Steps;
			$this -> Time = $Time;
			$this -> Status = $Status;
			$this -> IDUser = $IDUser;
		}
	}

	///Tạo mảng
	$recipes_array = array();
	if ($query){
		//Thêm phần tử vào màng
		while ($row = mysqli_fetch_assoc($data)){
			array_push($recipes_array, new Recipes($row ['IDRecipes'], $row ['RecipesAuthor'], $row ['RecipesTitle'],  $row ['RecipesContent'], $row ['NutritionalIngredients'], $row ['Ingredients'], $row ['Steps'], $row ['Time'], $row ['Status'], $row['IDUser']));
		}
		//Chuyển định dạng mảng qua json
		echo json_encode($recipes_array);
	}
	else {
			 echo "Xảy ra lỗi khi truy vấn dữ liệu";
	}

?>