<?php
	require "dbConfig.php";

	$IDUser = $_POST['IDUser'];
	$Username = $_POST['Username'];
	$Email = $_POST['Email'];
	$Password =  $_POST['Password'];
	$FullName = $_POST['FullName'];
	$Image = $_POST['Image'];
	$LoginFB = $_POST['LoginFB'];
	$IDRole = $_POST['IDRole'];
	$createTime = $_POST['createTime'];
	$Status = $_POST['Status'];

	class users{
		function __construct($IDUser, $Email, $Password, $FullName, $Image, $LoginFB, $IDRole, $Username, $createTime, $Status){
			$this -> IDUser = $IDUser;
			$this -> Email = $Email;
			$this -> Password = $Password;
			$this -> FullName = $FullName;
			$this -> Image = $Image;
			$this -> LoginFB = $LoginFB;
			$this -> IDRole = $IDRole;
			$this -> Username = $Username;	
			$this -> createTime = $createTime;	
			$this -> Status = $Status;		
		}
	}
	$query_register = " INSERT INTO users VALUES ('$IDUser', '$Email', '$Password', '$FullName'
											, '$Image', '$LoginFB', '$IDRole', '$Username', '$createTime', '$Status')";


		if (mysqli_query($connect, $query_register)){
			//Thành công
			echo "success";
		}else{
			//lỗi
			echo "error";
		}
	
?>	