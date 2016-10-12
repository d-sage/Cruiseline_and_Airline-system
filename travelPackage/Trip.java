package travelPackage;

import java.util.*;
import java.security.*;

public class Trip extends Event{

   /*--------------------------------------
   constructor
   --------------------------------------*/
   Trip(ShipPort ori, ShipPort des, int year, int month,
                 int day, int hour, int min, String eventID) throws InvalidParameterException{
                 
      checkDate(year, month, day, hour, min);
                 
      this.origin = ori;
      this.destination = des;
      this.date = new int[]{month,day,year,hour,min};
      this.eventID = eventID;
      this.sections = new ArrayList<Section>();
   
   }
   
   /*--------------------------------------
   this method returns "Trip"
   --------------------------------------*/
   protected String getTypeName(){
      return "Trip";
   }

}//end class