<?php
	require "dbConfig.php";

	$query = "SELECT * FROM users ";

	$data = mysqli_query($connect, $query);

	class users{
		function __construct($IDUser, $Email, $Password, $FullName, $Image, $LoginFB, $IDRole, $Username ){
			$this -> IDUser = $IDUser;
			$this -> Email = $Email;
			$this -> Password = $Password;
			$this -> FullName = $FullName;
			$this -> Image = $Image;
			$this -> LoginFB = $LoginFB;
			$this -> IDRole = $IDRole;
			$this -> Username = $Username;			
		}
	}
	$users_array = array();

	//Thêm phần tử vào màng
	while ($row = mysqli_fetch_assoc($data)){
		array_push($users_array, new users($row ['IDUser'], $row ['Email'], $row ['Password'],  $row ['FullName'], $row ['Image'], $row ['LoginFB'], $row ['IDRole'], $row ['Username'] ));
	}

	//Chuyển định dạng mảng qua json
	echo json_encode($users_array);
?>