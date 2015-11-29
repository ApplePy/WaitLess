<?php

    function leastSquares($theInputArray)
    {
        // Least squares calculation to get function that represents the wait time
        
        $myarray = array();
        $otherarray = array();
        
        // Construct arrays
        foreach ($theInputArray as $timestamp => $distanceArray) {
            $myarray[] = $timestamp;
            $otherarray[] = $distanceArray[0];
        }      
        
        //$myarray = array_keys($theInputArray["Timestamp"]); //X-array

        
        // Shift the resulting equation to the left so that the first distance reading is at time 0
        $smallestValue = min($myarray);
        foreach($myarray as &$value) {
            $value = $value - $smallestValue;
        }
        
        //$otherarray = $theInputArray["Distance"]; //Y-array
        $sumX=0;
        $sumY=0;
        $sumXX=0;
        $sumXY=0;
        $n=count($myarray); //number of items
        for ($i=0; $i<$n; $i++) {
            $sumX +=$myarray[$i];
            $sumY +=$otherarray[$i];
            $meanX = $sumX/$n;
            $meanY = $sumY/$n;
            $sumXX += pow($myarray[$i],2);
            $sumXY += ($myarray[$i])*($otherarray[$i]);
            $m =($sumXY - $meanY*$sumX)/($sumXX-$meanX*$sumX);
            $b =$meanY-$meanX*$m;
        
        }
        //echo ("Sum of X : " .$sumX);
        //echo "<br>";
        //echo ("Sum of Y : " .$sumY);
        //echo "<br>";
        //echo ("Mean of X: " .$meanX);
        //echo "<br>";
        //echo ("Mean of Y: " .$meanY);
        //echo "<br>";
        //echo ("Sums of Squares for X: " .$sumXX);
        //echo "<br>";
        //echo ("A_hat : " .$m); //Estimation of line passing through two points
        //echo "<br>";
        //echo ("B_hat : " .$b);
        //echo "<br>";
        //echo ("The equation of the regression line is : y = " . $m );
        //echo ("x + " . $b);
        return array('m' => $m,'b' => $b);
    }

	
	$database = NULL;  
    
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
	
        // Get Estimotes in teh database
    try {
        $query = $database->prepare("SELECT EstimoteID FROM Output");
        
        /*$query->bindParam( ":LatMin",		$_GET["GPSLat"] - $_GET["GPSLat"] );
        $query->bindParam( ":LatMax",		$_GET["GPSLat"] + $_GET["GPSLat"] );
        $query->bindParam( ":LongMin",		$_GET["GPSLong"] - $_GET["GPSLong"] );
        $query->bindParam( ":LongMax",		$_GET["GPSLong"] - $_GET["GPSLong"] );
        //$query->bindParam( ":time",         time() );
        //$query->bindParam( ":URL",          $url );*/
        
        $query->execute();
    }
    catch (PDOException $e) {
        throw new Exception( "Database submission error! Error: ".$e->getMessage() );
    }
    
    if ($query != FALSE) {
        $estimotes = $query->fetchAll(PDO::FETCH_COLUMN, 0); //There will be no duplicate variables because VariableName is a Primary Key
        
         // For each estimote, get all the devices that connected to it in the last 10 minutes, create an average wait time function, and post
        foreach ($estimotes as $EstimoteID) {
            try {
                // Get all recent devices
                $newQuery = $database->prepare("SELECT DeviceID, Timestamp FROM Input WHERE EstimoteID = :EstimoteID AND (Timestamp BETWEEN :tenMinutesAgo AND :now)");
                
                $currentTime = time();
                $tenMinutesAgo = $currentTime - (10 * 60);
                
                $newQuery->bindParam( ":EstimoteID",        $EstimoteID );
                $newQuery->bindParam( ":now",               $currentTime );
                $newQuery->bindParam( ":tenMinutesAgo",     $tenMinutesAgo );
            
                $newQuery->execute();
            }
            catch (PDOException $e) {
               throw new Exception( "Database submission error! Error: ".$e->getMessage() );
            }
            
            $waitsInLine = $newQuery->fetchAll(PDO::FETCH_COLUMN|PDO::FETCH_GROUP);
            $theTime = array();
            foreach ($waitsInLine as $device => $checkins) {
                $theTime[$device] = max($checkins) - min($checkins);
                print_r($theTime[$device]."<br/>");
            }
            print_r($theTime);
            $finalWait = max($theTime); // This holds the waiting in line time.
            print_r($finalWait);
            
            
            try {
                $finalQuery = $database->prepare("UPDATE Output SET WaitTime=:wait WHERE EstimoteID = :EstimoteID");
 
                $computedValue = $finalWait/60;
            
                $finalQuery->bindParam( ":EstimoteID",      $EstimoteID );
                $finalQuery->bindParam( ":wait",        $computedValue );
            
                $finalQuery->execute();
            }
            catch (PDOException $e) {
                throw new Exception( "Database submission error! Error: ".$e->getMessage() );
            }       
        }
        unset($EstimoteID);
    }
    
    // Clear obsolete input data!
    try {
        $finalQuery = $database->prepare("DELETE FROM Input WHERE Timestamp < :tenMinutesAgo");
                
        $tenMinutesAgo = time() - (10 * 60);
            
        $finalQuery->bindParam( ":tenMinutesAgo",      $tenMinutesAgo );
            
        $finalQuery->execute();
    }
    catch (PDOException $e) {
        throw new Exception( "Database submission error! Error: ".$e->getMessage() );
    }
?>