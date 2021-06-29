<?php
	require "dbConfig.php";
	$Year = $_GET['Year'];

	$query = "SELECT count(*) as count , MONTH(createTime) FROM  recipes WHERE Status = 'approval' AND YEAR(createTime) = '$Year' GROUP BY MONTH(createTime) ";

	$data = mysqli_query($connect, $query);


	class Statistic {
		function __construct ($count, $month){
			$this -> count = $count;
	 		$this -> month = $month;
		}
	}

	///Tạo mảng
	$recipes_array = array();

	//Thêm phần tử vào màng
	while ($row = mysqli_fetch_assoc($data)){
		array_push($recipes_array, new Statistic ($row ['count'], $row ['MONTH(createTime)'] ));
	}

	//Chuyển định dạng mảng qua json
	echo json_encode($recipes_array)
?>