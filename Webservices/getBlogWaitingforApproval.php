<?php
	require "dbConfig.php";

	$query = "SELECT * FROM blogs WHERE Status = 'waitingforapproval' ";

	$data = mysqli_query($connect, $query);
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

	///Tạo mảng
	$blogs_array = array();

	while ($row = mysqli_fetch_assoc($data)){
		array_push($blogs_array, new Blog($row ['IDBlog'], $row ['BlogTitle'], $row ['BlogAuthor'],  $row ['BlogContent'], $row ['Time'], $row ['Status']));
	}

	//Chuyển định dạng mảng qua json
	echo json_encode($blogs_array)
?>