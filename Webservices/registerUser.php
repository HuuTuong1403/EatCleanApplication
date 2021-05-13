<?php
	require "dbConfig.php";

	$IDUser = $_POST['IDUser'];
	$Email = $_POST['Email'];
	$Phone = $_POST['Phone'];
	$Password =  $_POST['Password'];
	$FullName = $_POST['FullName'];
	$Gender = $_POST['Gender'];
	$Image = $_POST['Image'];
	$LoginFB = $_POST['LoginFB'];
	$IDRole = $_POST['IDRole'];

	$query = " INSERT INTO users VALUES ('$IDUser', '$Email', '$Phone', '$Password', '$FullName',
											'$Gender', '$Image', '$LoginFB', '$IDRole')";


	if (mysqli_query($connect, $query)){
		//Thành công
		echo "success";
	}else{
		//lỗi
		echo "error";
	}
?>	