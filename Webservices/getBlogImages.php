<?php
	require "dbConfig.php";

	$query = "SELECT * FROM blogimages";

	$data = mysqli_query($connect, $query);


	class RecipeImages{
		function __construct($IDBlogImages, $BlogImages, $IDBlog){
			$this -> IDBlogImages = $IDBlogImages;
			$this -> BlogImages = $BlogImages;
			$this -> BlogImages = $IDBlog;
		}
	}
	///Tạo mảng
	$recipes_array = array();
	if ($query){
		//Thêm phần tử vào màng
		while ($row = mysqli_fetch_assoc($data)){
			array_push($recipes_array, new RecipeImages($row ['IDBlogImages'], $row ['BlogImages'], $row ['BlogImages']));
		}
		//Chuyển định dạng mảng qua json
		echo json_encode($recipes_array);
	}
	else {
			 $response = array(
            'message' => 'Có lỗi xảy ra trong quá trình truy vấn dữ liệu'
        );
	}
?>

	