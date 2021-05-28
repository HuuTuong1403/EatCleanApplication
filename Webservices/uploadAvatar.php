<?php
    require "dbConfig.php";
    $url = 'https://msteatclean.000webhostapp.com/';
    $target_dir = "uploads/";
    $target_file = $target_dir.basename($_FILES["fileToUpload"]["name"]);
    $imageFileType = strtolower(pathinfo($target_file,PATHINFO_EXTENSION));

    move_uploaded_file($_FILES["fileToUpload"]["tmp_name"], $target_file);


    $IDUser = $_GET["IDUser"];
    $Image = $url.$target_file;

    $query = " UPDATE users SET Image = '$Image'
							WHERE IDUser = '$IDUser' ";
        
    $response = array();
    if (mysqli_query($connect, $query)){
            //Thành công
        $response = array(
            'message' => 'Thành công'
        );
    }else{
        //lỗi
        $response = array(
            'message' => 'Thất bại'
        );
    }
    echo json_encode($response);
?>