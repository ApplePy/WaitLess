<?php
    $database = NULL;
    
    
        // THIS NEEDS TO BE EDITED WITH THE RIGHT CREDS!
    $database = new PDO("mysql:host=localhost:8889;dbname=WaitLess", "root", "root");
    
    if (is_object($database))
    {
        $database->setAttribute (PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION); //WARNING! This can cause SQL credentials to be dumped to the screen if exception is not caught!
    }
    else
    {
        throw new exception("So, database connection failed...");
    }
    
        // Insert information into "Input" SQL database
    //try {
        $query = $database->prepare("INSERT INTO Input SET DeviceID = :DeviceID, EstimoteID = :EstimoteID, Timestamp = :time");
        
        $currentTime = time();
        
            //$query->bindParam( ":RID",          NewGuid() );
        $query->bindParam( ":DeviceID",     $_GET["DeviceID"] );
        $query->bindParam( ":EstimoteID",   $_GET["EstimoteID"] );
        //$query->bindParam( ":Distance",     $_GET["Distance"] );
        $query->bindParam( ":time",         $currentTime );
            //$query->bindParam( ":URL",          $url );
        
        $query->execute();
    //}
    //catch (PDOException $e) {
    //    throw new Exception( "Database submission error! Error: ".$e->getMessage() );
    //}  
?>