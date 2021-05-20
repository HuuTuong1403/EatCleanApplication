<?php
	require "dbConfig.php";

	$IDUser = $_POST['IDUser'];
	$Email = $_POST['Email'];
	$Password = $_POST['Password'];
	$FullName = $_POST['FullName'];
	$NutritionalIngredients = $_POST['NutritionalIngredients']; 
	$Image = $_POST['Image'];
	$Username = $_POST['Username'];

	$query = " UPDATE users SET IDUser = '$IDUser', Email = '$Email', 
								Password = '$Password' , FullName = '$FullName' ,
								NutritionalIngredients = '$NutritionalIngredients' ,
								Image = '$Image' , Username = '$Username' 
							WHERE  IDUser = '$IDUser' ";
							
	if (mysqli_query($connect, $query)){
		//Thành công
		echo "success";
	}else{
		//lỗi
		echo "error";
	}
?>