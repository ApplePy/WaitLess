<?php
    header('Content-Type: application/json');
	
	$database = NULL;
    
    
        // THIS NEEDS TO BE EDITED WITH THE RIGHT CREDS!
    $database = new PDO("mysql:host=localhost;dbname=WaitLess", "root", "root");
    
    if (is_object($database))
    {
        $database->setAttribute (PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION); //WARNING! This can cause SQL credentials to be dumped to the screen if exception is not caught!
    }
    else
    {
        throw new exception("So, database connection failed...");
    }
    
	$query = NULL;
	
        // Insert information into "Input" SQL database
    try {
        $query = $database->prepare("SELECT * FROM Output");// WHERE (EstimoteGPSLat BETWEEN :LatMin AND :LatMax) AND (EstimoteGPSLong BETWEEN :LongMin AND :LongMax)");
        
        /*$LatMin = $_GET["GPSLat"] - 0.001;
        $LatMax = $_GET["GPSLat"] + 0.001;
        $LongMin = $_GET["GPSLong"] - 0.001;
        $LongMax = $_GET["GPSLong"] + 0.001;
        
        $query->bindParam( ":LatMin",		$LatMin );
        $query->bindParam( ":LatMax",		$LatMax );
        $query->bindParam( ":LongMin",		$LongMin );
        $query->bindParam( ":LongMax",		$LongMax );*/
        //$query->bindParam( ":time",         time() );
        //$query->bindParam( ":URL",          $url );
        
        $query->execute();
    }
    catch (PDOException $e) {
        throw new Exception( "Database submission error! Error: ".$e->getMessage() );
    }
    
    if ($query != FALSE) {
    $trInfo = $query->fetchAll(PDO::FETCH_ASSOC); //There will be no duplicate variables because VariableName is a Primary Key
        
	echo json_encode($trInfo);
    }
?>