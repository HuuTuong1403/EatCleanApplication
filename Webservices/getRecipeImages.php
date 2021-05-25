<?php
	require "dbConfig.php";

	$query = "SELECT * FROM recipesimages";

	$data = mysqli_query($connect, $query);


	class RecipeImages{
		function __construct($IDRecipesImages, $RecipesImages, $IDRecipes){
			$this -> IDRecipesImages = $IDRecipesImages;
			$this -> RecipesImages = $RecipesImages;
			$this -> IDRecipes = $IDRecipes;
		}
	}
	///Tạo mảng
	$recipes_array = array();
	if ($query){
		//Thêm phần tử vào màng
		while ($row = mysqli_fetch_assoc($data)){
			array_push($recipes_array, new RecipeImages($row ['IDRecipesImages'], $row ['RecipesImages'], $row ['IDRecipes']));
		}
		//Chuyển định dạng mảng qua json
		echo json_encode($recipes_array);
	}
	else {
			 echo "Xảy ra lỗi khi truy vấn dữ liệu";
	}
	
?>