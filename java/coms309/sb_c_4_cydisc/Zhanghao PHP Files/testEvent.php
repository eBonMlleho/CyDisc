<?php
	include 'stringValidation.php';
	include 'connectToDatabase.php';
	
	$conn = connectToDatabase();
	
	$requestInformation = $_POST['request'];
	
	
	$response = array();
	
	date_default_timezone_set('AMERICA/Chicago');
	$today = getdate();
	$today = $today['year']. "-" . $today['mon']. "-" . $today['day'];
	/////////////echoBackTheEventsToday('2017-12-29');
	
	if($requestInformation=="onlyGetEventsToday"){
		
		$eventDate = $_POST['eventDate'];
		$userID = (int)$_POST['playerid'];
		if(!isset($eventDate) || !isset($userID)){
			$response["success"] = false;
			echo json_encode($response);
		}
		
		//$response["eventsInformation"] = "blablablablabla";
		
		echoBackTheEventsToday($eventDate);
        $conn->close();
		
	}else if($requestInformation=="addAnEvent"){
		
		
		$userID = (int)$_POST['playerid'];
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
		
		
		/*
		$sql = "INSERT INTO events (event_author_id, event_name, 
			event_description) VALUES ('".$userID."',
			'".$eventName."','".$eventDescription."')";
		*/
		
				
		$sql = "INSERT INTO events (event_author_id, event_date, event_start_time, event_name, 
			event_description) VALUES ('".$userID."','".$eventDate."','".$eventStartTime."',
			'".$eventName."','".$eventDescription."')";
		

			
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
		
		
		//not convert to Date type
		//$eventDate=DATE_FORMAT(STR_TO_DATE($eventDate, '%Y-%m-%d'), '%Y-%m-%d');
		
		$sql = "SELECT event_start_time, event_name, event_description
		FROM events WHERE event_date = '".$eventDate."'";
		$result = $conn->query($sql);
		//I added
		//$returnValues =[];
	
		if ($result->num_rows > 0) 
		{
			/*
   			while($row = mysql_fetch_row($result)) {
				array_push($response["eventsInformation"], 
					"".$row['event_start_time']." ".$row['event_name']." ".$row['event_description']."\n");	
			}
			*/
			
			$AlleventsInformation = array();
			while($row = $result->fetch_assoc()) {
				$eventsInformation=array();
				$eventsInformation["eventStartTime"] = $row["event_start_time"];
				$eventsInformation["eventName"] = $row["event_name"];
				$eventsInformation["eventDescription"] = $row["event_description"];
				//array_push($response, $eventsInformation);
				$AlleventsInformation[]=$eventsInformation;
				
				
	
				
				//[
				// 'eventstarttime'=>$row['event_start_time'],
				// 'eventName'=>$row['event_name'],
				// 'eventDescription'=>$row['event_description']
				//];
		
			}
			$response["eventsInformation"]= $AlleventsInformation;
			//echo($eventsInformation);
		}
		//echo json_encode($returnValues);
		echo json_encode($response);
		$conn->close();
	}
	

?>
