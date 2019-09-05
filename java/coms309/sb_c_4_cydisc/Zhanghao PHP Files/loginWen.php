<?php
	ini_set('display_errors', 1);
	include 'connectToDatabase.php';
	$conn = connectToDatabase();
	$name = $_POST['username'];
	$password = $_POST['password'];

	$response = array();
	
	$sql = "SELECT player_name, player_email,
	player_id FROM players WHERE player_name = '$name' 
	AND player_password = '$password'"; 
	$result =$conn->query($sql);
	if ($result->num_rows > 0) 
	{
   
		while($row = $result->fetch_assoc())
		{
			$response["username"] = $row["player_name"];
			$response["email"] = $row["player_email"];
			$response["success"] = true;
			$response["userID"] = $row["player_id"];
		}
	}
	else 
	{
		$response["success"] = false;
		$response["error_msg"] = "Invalid username or password";
	}

	echo json_encode($response);
	$conn->close();
	
?>