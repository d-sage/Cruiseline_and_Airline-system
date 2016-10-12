package travelPackage;

import java.util.*;
import java.security.*;

public class CruiseLine extends Provider{

   /*--------------------------------------
   constructor
   --------------------------------------*/
   CruiseLine(String name) throws InvalidParameterException{
      checkParams(name);   
   
      this.events = new ArrayList<Event>();
      this.name = name;
      this.priceList = new ArrayList<Price>();
   }
   
   /*--------------------------------------
   this method returns "CruiseLine"
   --------------------------------------*/
   protected String getTypeName(){
      return "CruiseLine";
   }

}//end class