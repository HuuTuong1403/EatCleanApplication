<?php
	require "dbConfig.php";

	$IDUser = $_GET['IDUser']; 
	$IDRecipes = $_GET['IDRecipes']; 



	$query = "DELETE FROM favoriterecipes
				WHERE favoriterecipes.IDUser = '$IDUser' and  favoriterecipes.IDRecipes = '$IDRecipes' ";


	if (mysqli_query($connect, $query)){
		//Thành công
			echo "Bạn đã xóa món ăn trong mục yêu thích thành công";
	}
	else{
		echo "Bạn đã xóa món ăn trong mục yêu thích không thành công";
	}
?>