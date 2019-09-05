<?php
	function connectToDatabase()
	{
		$servername = "mysql.cs.iastate.edu";
		$username = "dbu309sbc4";
		$password = "sXrWaR#@";
		$schema = "db309sbc4";

		// Create connection
		$conn = new mysqli($servername, $username, $password, $schema);

		// Check connection
		if ($conn->connect_error) {
			die("Connection failed: " . $conn->connect_error);
		} 
		
		return $conn;
	}

?>
