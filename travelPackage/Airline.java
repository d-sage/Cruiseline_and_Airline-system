package travelPackage;

import java.util.*;
import java.security.*;

public class Airline extends Provider{
   
   
   /*--------------------------------------
   constructor
   --------------------------------------*/
   Airline(String name) throws InvalidParameterException{
      checkParams(name);   
   
      this.events = new ArrayList<Event>();
      this.name = name;
      this.priceList = new ArrayList<Price>();
   }
   
   /*--------------------------------------
   this method returns "Flight"
   --------------------------------------*/
   protected String getTypeName(){
      return "Flight";
   }

}//end class