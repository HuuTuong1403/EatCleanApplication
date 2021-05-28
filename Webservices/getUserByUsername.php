<?php
	require "dbConfig.php";

	$Username = $_GET['Username']; 

	class users{
		function __construct($IDUser, $Email, $Password, $FullName, $Image, $LoginFB, $IDRole, $Username){
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

	$query = "SELECT * FROM users WHERE users.Username = '$Username'";

	$data = mysqli_query($connect, $query);

	///Tạo mảng
	$user_array = array();

	$row = mysqli_fetch_assoc($data);
	$user_array = array(
	   'IDUser' => $row ['IDUser'],
	   'Email' => $row ['Email'],
	   'Password' => $row ['Password'],
	   'FullName' => $row ['FullName'],
	   'Image' => $row ['Image'],
	   'LoginFB' => $row ['LoginFB'],
	   'IDRole' => $row ['IDRole'],
	   'Username' => $row ['Username'],
	);

	//Chuyển định dạng mảng qua json
	echo json_encode($user_array);
?>