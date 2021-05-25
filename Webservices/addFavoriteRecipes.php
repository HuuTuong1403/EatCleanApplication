<?php
	require "dbConfig.php";

	$IDUser = $_POST['IDUser'];
	$IDRecipes	= $_POST['IDRecipes'];
	//$IDUser = "ID-U-11864";
	//$IDRecipes = "F008";
	$Description = "";

	class users{
		function __construct($IDUser, $IDRecipes, $Description){
			$this -> IDUser = $IDUser;
			$this -> IDRecipes = $IDRecipes;
			$this -> Description = $Description;		
		}
	}

	$query_add = " INSERT INTO favoriterecipes VALUES ('$IDRecipes','$IDUser', '$Description') ";


	if (mysqli_query($connect, $query_add)){
			//Thành công
			echo "success";
	}else{
			//lỗi
			echo "error";
	}
	
?>	