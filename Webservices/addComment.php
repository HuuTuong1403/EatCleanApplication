<?php
	require "dbConfig.php";

	$IDUser = $_POST['IDUser'];
	$IDRecipes = $_POST['IDRecipes'];
	$IDComment	 = $_POST['IDComment'];
	$Comment = $_POST['Comment'];
	

	class Comment {
		function __construct($IDUser, $IDRecipes , $IDComment ,$Comment){
			$this -> IDUser = $IDUser;
			$this -> IDRecipes = $IDRecipes;
			$this -> IDComment = $IDComment;
			$this -> Comment = $Comment;	
		}
	}
	$query_add = "INSERT INTO comments VALUES ('$IDUser', '$IDRecipes', '$IDComment','$Comment')";

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