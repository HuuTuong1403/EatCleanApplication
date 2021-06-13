<?php
	require "dbConfig.php";

	$url = 'https://msteatclean.000webhostapp.com/';
    $target_dir = "uploads/";
    $target_file = $target_dir.basename($_FILES["fileToUpload"]["name"]);
    $imageFileType = strtolower(pathinfo($target_file,PATHINFO_EXTENSION));

    move_uploaded_file($_FILES["fileToUpload"]["tmp_name"], $target_file);

    $Images = $url.$target_file;

							
	echo json_encode($Images);
?>
