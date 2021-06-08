<?php
	require "dbConfig.php";

	$query = "SELECT * FROM blogimages";

	$data = mysqli_query($connect, $query);


	class BlogImages{
		function __construct($IDBlogImages, $BlogImages, $IDBlog){
			$this -> IDBlogImages = $IDBlogImages;
			$this -> BlogImages = $BlogImages;
			$this -> IDBlog = $IDBlog;
		}
	}
	///Tạo mảng
	$blog_array = array();
	if ($query){
		//Thêm phần tử vào màng
		while ($row = mysqli_fetch_assoc($data)){
			array_push($blog_array, new BlogImages($row ['IDBlogImages'], $row ['BlogImages'], $row ['IDBlog']));
		}
		//Chuyển định dạng mảng qua json
		echo json_encode($blog_array);
	}
	else {
			 $response = array(
            'message' => 'Có lỗi xảy ra trong quá trình truy vấn dữ liệu'
        );
	}
?>

	