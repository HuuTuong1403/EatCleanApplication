<?php
	require "dbConfig.php";

	$query = "SELECT count(*) as count , MONTH(Time) FROM  blogs WHERE Status = 'approval' GROUP BY MONTH(Time) ";

	$data = mysqli_query($connect, $query);


	class Statistic {
		function __construct ($count, $month){
			$this -> count = $count;
	 		$this -> month = $month;
		}
	}

	///Tạo mảng
	$blogs_array = array();

	//Thêm phần tử vào màng
	while ($row = mysqli_fetch_assoc($data)){
		array_push($blogs_array, new Statistic ($row ['count'], $row ['MONTH(Time)'] ));
	}

	//Chuyển định dạng mảng qua json
	echo json_encode($blogs_array)
?>