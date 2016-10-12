package travelPackage;

import java.util.*;
import java.security.*;

public class Flight extends Event{
   
   
   /*--------------------------------------
   constructor
   --------------------------------------*/
   Flight(Airport ori, Airport des, int year, int month,
                 int day, int hour, int min, String eventID) throws InvalidParameterException{
                 
      checkDate(year, month, day, hour, min);
                 
      this.origin = ori;
      this.destination = des;
      this.date = new int[]{month,day,year,hour,min};
      this.eventID = eventID;
      this.sections = new ArrayList<Section>();
   
   }
   
   /*--------------------------------------
   this method returns "Flight"
   --------------------------------------*/
   protected String getTypeName(){
      return "Flight";
   }
   
   

}//end class