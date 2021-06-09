<?php
	require "dbConfig.php";

	$query = "SELECT * FROM comments";

	$data = mysqli_query($connect, $query);

	class Comment {
		function __construct($IDUser, $IDRecipes , $IDComment ,$Comment){
			$this -> IDUser = $IDUser;
			$this -> IDRecipes = $IDRecipes;
			$this -> IDComment = $IDComment;
			$this -> Comment = $Comment;	
		}
	}
	///Tạo mảng
	$comments_array = array();

	while ($row = mysqli_fetch_assoc($data)){
		array_push($comments_array, new Comment($row ['IDUser'], $row ['IDRecipes'], $row ['IDComment'],$row ['Comment']));
	}

	//Chuyển định dạng mảng qua json
	echo json_encode($comments_array)
?>