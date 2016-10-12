package travelPackage;

import java.security.*;

class UtilityFactory{

   static Event getEventType(Port ori, Port des, int year, int month, int day, int hour, int min, String eventID, String type) throws InvalidParameterException{
      if(type.equals("Flight")){
         return new Flight((Airport)ori, (Airport)des, year, month, day, hour, min, eventID);
      }
      else
         return new Trip((ShipPort)ori, (ShipPort)des, year, month, day, hour, min, eventID);
   }
   
   static Section getSectionType(int rows, int col, SeatClass s, Price price, String type) throws InvalidParameterException{
      if(type.equals("Flight")){
         return new FlightSection(rows, col, price, s);
      }
      else
         return new CabinSection(rows, col, price, s);
   }
   
   //
   static Port getPortType(String name, String type) throws InvalidParameterException{
      if(type.equals("Flight")){
         return new Airport(name);
      }
      else
         return new ShipPort(name);
   }
   
   //
   static Provider getLineType(String name, String type) throws InvalidParameterException{
      if(type.equals("Flight")){
         return new Airline(name);
      }
      else
         return new CruiseLine(name);
   }


}//end class