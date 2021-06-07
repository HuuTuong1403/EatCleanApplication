<?php
	require "dbConfig.php";

	$IDBlog = $_POST['IDBlog'];
	$BlogTitle = $_POST['BlogTitle'];
	$BlogAuthor = $_POST['BlogAuthor'];
	$BlogContent =  $_POST['BlogContent'];
	$Time = $_POST['Time'];
	$Status = $_POST['Status'];


	class Blog {
		function __construct($IDBlog, $BlogTitle ,$BlogAuthor, $BlogContent, $Time, $Status){
			$this -> IDBlog = $IDBlog;
			$this -> BlogTitle = $BlogTitle;
			$this -> BlogAuthor = $BlogAuthor;
			$this -> BlogContent = $BlogContent;
			$this -> Time = $Time;
			$this -> Status = $Status;
		}
	}
	$query_add = "INSERT INTO blogs VALUES ('$IDBlog', '$BlogTitle', '$BlogAuthor', '$BlogContent', '$Time', '$Status')";

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
?>