<?php
	include 'stringValidation.php';
	include 'connectToDatabase.php';
	
	$conn = connectToDatabase();
	
	$requestInformation = $_POST['request'];
	
	
	$response = array();
	
	date_default_timezone_set('AMERICA/Chicago');
	$today = getdate();
	$today = $today['year']. "-" . $today['mon']. "-" . $today['day'];
	
	
	
	if($requestInformation=="onlyGetEventsToday"){
		
		$eventDate = $_POST['eventDate'];
		$userID = (int)$_POST['userId'];
		if(!isset($eventDate) || !isset($userID)){
			$response["success"] = false;
			echo json_encode($response);
		}
		
		echoBackTheEventsToday($eventDate);
        
		
	}else if($requestInformation=="addAnEvent"){
		
		
		$userID = (int)$_POST['userId'];
		$eventName = $_POST['eventName'];
		$eventDescription = $_POST['eventDescription'];
		$eventStartTime	= $_POST['eventStartTime'];
		$eventDate = $_POST['eventDate'];
                                                       
		if(!isset($userID)||!isset($eventName)||
		!isset($eventDescription)||!isset($eventStartTime)||
		!isset($eventDate)){
			$response["success"] = false;
			echo json_encode($response);
		}
		
		insertTheNewEventAndResponse($userID, $eventName, $eventDescription, 
						$eventStartTime, $eventDate);
		$conn->close();
	}
	
	
	
	function insertTheNewEventAndResponse($userID, $eventName, $eventDescription, 
						$eventStartTime, $eventDate){					
				
		$sql = "INSERT INTO events (event_author_id, event_date, event_start_time, event_name, 
			event_description) VALUES ('".$userID."','".$eventDate."','".$eventStartTime."',
			'".$eventName."','".$eventDescription."')";

		/*	
		$servername = "mysql.cs.iastate.edu";
		$username = "dbu309sbc4";
		$password = "sXrWaR#@";
		$schema = "db309sbc4";

		// Create connection
		$mysqli = new mysqli($servername, $username, $password, $schema);
		if(!$mysqli->query($sql)){
			echo "bad";
		}	
		*/
			
		global $conn;
	
		
		//test
		//echo "".$userID.$eventName.$eventDescription.$eventStartTime.$eventDate;

		if($conn->query($sql)===TRUE){
			
			//test
			echo "here";
			echoBackTheEventsToday($eventDate);
			
		}
		else{		
			$response["success"] = false;	
			echo json_encode($response);
		}
		
							
	}
						
	function echoBackTheEventsToday($eventDate){
		
		global $conn;
	
		
		$response = array();
		$response["success"] = true;
		$response["eventsInformation"];
		
		//not convert to Date type
		//$eventDate=DATE_FORMAT(STR_TO_DATE($eventDate, '%Y-%m-%d'), '%Y-%m-%d');
		
		$sql = "SELECT event_start_time, event_name, event_description
		FROM events WHERE event_date = '".$eventDate."'";
		
		

		$result = $conn->query($sql);
		if ($result->num_rows > 0) 
		{
			$count = 0;
			
			
   			while($row = mysql_fetch_row($result)) {
				array_push($response["eventsInformation"], 
					"".$row['event_start_time']." ".$row['event_name']." ".$row['event_description']."\n");	
			}

		}

		echo json_encode($response);
	}
	

?>
