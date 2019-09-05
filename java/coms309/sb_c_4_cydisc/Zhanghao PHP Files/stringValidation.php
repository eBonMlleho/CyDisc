<?php
	
	function isValidEmail($email)
	{
		if(false !== filter_var($email, FILTER_VALIDATE_EMAIL))
		{
			return 0;
		}
		return 1;
	}
	function isValidName($username)
	{
		if(preg_match('/^[a-zA-Z]+$/', $username))
		{
			return 0;
		}		
		return 1;
	}