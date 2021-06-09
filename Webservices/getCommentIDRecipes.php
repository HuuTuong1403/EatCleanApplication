<?php
	require "dbConfig.php";
	$IDRecipes = $_GET['IDRecipes'];

	$query = "SELECT comments.IDUser, users.Username, users.Image, comments.IDRecipes, comments.IDComment ,comments.Comment  FROM comments INNER JOIN users ON comments.IDUser =  users.IDUser WHERE comments.IDRecipes = '$IDRecipes' ";

	$data = mysqli_query($connect, $query);

	class Comment {
		function __construct($IDUser, $Username, $Image, $IDRecipes , $IDComment ,$Comment){
			$this -> IDUser = $IDUser;
			$this -> Username = $Username;
			$this -> Image = $Image;	
			$this -> IDRecipes = $IDRecipes;
			$this -> IDComment = $IDComment;
			$this -> Comment = $Comment;	
		}
	}
	///Tạo mảng
	$comments_array = array();

	while ($row = mysqli_fetch_assoc($data)){
		array_push($comments_array, new Comment($row ['IDUser'], $row ['Username'], $row ['Image'], $row ['IDRecipes'], $row ['IDComment'] , $row ['Comment']));
	}

	//Chuyển định dạng mảng qua json
	echo json_encode($comments_array);
?>