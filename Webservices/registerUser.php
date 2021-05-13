<?php
	require "dbConfig.php";

	$IDUser = $_POST['IDUser'];
	$Email = $_POST['Email'];
	$Password =  $_POST['Password'];
	$FullName = $_POST['FullName'];
	$Image = $_POST['Image'];
	$LoginFB = $_POST['LoginFB'];
	$IDRole = $_POST['IDRole'];

	$query = " INSERT INTO users VALUES ('$IDUser', '$Email', '$Password', '$FullName'
											, '$Image', '$LoginFB', '$IDRole')";


	if (mysqli_query($connect, $query)){
		//Thành công
		echo "success";
	}else{
		//lỗi
		echo "error";
	}
?>	