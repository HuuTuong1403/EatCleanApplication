<?php
	require "dbConfig.php";

	$query = "SELECT * FROM ((favoriterecipes INNER JOIN recipes ON recipes.IDRecipes = favoriterecipes.IDRecipes) 
												INNER JOIN users ON favoriterecipes.IDUser = users.IDUser)";

	$data = mysqli_query($connect, $query);


	class Recipes{
		function __construct($IDRecipes, $RecipesAuthor, $RecipesTitle, $RecipesContent, $NutritionalIngredients, $Ingredients, $Steps, $Time, $Status, $IDUser, $Username){
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
			$this -> Username = $Username;
		}
	}
	///Tạo mảng
	$recipes_array = array();
	if ($query){
		//Thêm phần tử vào màng
		while ($row = mysqli_fetch_assoc($data)){
			array_push($recipes_array, new Recipes($row ['IDRecipes'], $row ['RecipesAuthor'], $row ['RecipesTitle'],  $row ['RecipesContent'], $row ['NutritionalIngredients'], $row ['Ingredients'], $row ['Steps'], $row ['Time'], $row ['Status'], $row['IDUser'], $row['Username']));
		}
		//Chuyển định dạng mảng qua json
		echo json_encode($recipes_array);
	}
	else {
			 echo "Xảy ra lỗi khi truy vấn dữ liệu";
	}
	
?>