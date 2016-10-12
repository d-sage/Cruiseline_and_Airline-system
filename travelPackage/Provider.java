package travelPackage;

import java.util.*;
import java.security.*;
import java.io.*;

public abstract class Provider{

   protected List<Event> events;
   protected String name;
   protected List<Price> priceList;
   
   /*--------------------------------------
   returns the name of the provider
   --------------------------------------*/
   String getName(){
      return this.name;
   }
   
   /*--------------------------------------
   checks for valid naming
   --------------------------------------*/
   protected static void checkParams(String name) throws InvalidParameterException{
      if(name.length() > 5){
         throw new InvalidParameterException("!!\n~Airline names are five characters or less in length.\n" +
                                             "Violation: " + name);
      }
   }
   
   /*--------------------------------------
   adds event to current provider
   --------------------------------------*/
   void addEvent(Port ori, Port des, int year, int month, int day, int hour, int min, String eventID) throws InvalidParameterException{
      for(Event event: events){
         if(event.getID().equals(eventID)){
            throw new InvalidParameterException("!!\n~Flight ID's are unique to flights.\n" +
                                                "Violation: " + eventID);
         }
      }
      events.add(UtilityFactory.getEventType(ori, des, year, month, day, hour, min, eventID, this.getTypeName()));
   }
   
   /*--------------------------------------
   adds a section to an event in current
   provider
   --------------------------------------*/
   void addSection(String eventID, int rows, SeatClass s, int price, char layout) throws InvalidParameterException{
      
      boolean foundEvent = false;
      for(Event event: events){
         if(event.getID().equals(eventID)){
            foundEvent = true;
            
            
            String ori = event.getOrigin();
            String des = event.getDestination();
            Price tempPrice = this.checkPrices(ori, des, s);
            if(tempPrice == null){
               tempPrice = new Price(price, ori, des, s);
               this.priceList.add(tempPrice);
            }
            
            
            event.addSection(rows, s, tempPrice, layout);
         }
      }
      if(!(foundEvent)){
         throw new InvalidParameterException("!!\n~Flight not found.\n" +
                                             "Violation: " + eventID);
      }
      
   }
   
   /*--------------------------------------------------
   checks prices for ori and des of seatclass s
   --------------------------------------------------*/
   private Price checkPrices(String ori, String des, SeatClass s){
      for(Price price: priceList){
         if(price.matches(ori, des, s)){
            return price;
         }
      }
      return null;
   }
   
   /*--------------------------------------
   finds available events with specified
   origin and destination
   --------------------------------------*/
   void findAvailableEvent(String ori, String des, String type){
      String availableFlights = "";
      
      for(Event event: events){
         if(event.getOrigin().equals(ori) && event.getDestination().equals(des)){
            availableFlights += "\n" + getLineType(type) + ": " + this.name;
            
            if(event.hasAvailablePlacement()){            
               availableFlights += "\n\n" + type + " ID: " + event.getID() + "\nHas Available Seating";
               availableFlights += "\n" + event.showAvailability();
               
            }
            else{
               availableFlights += "\n\n" + type + " ID: " + event.getID() + "\nNone Available\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";
            }
         }
      }
      
      System.out.print(availableFlights);
   }
   
   /*--------------------------------------
   finds all events of specified type
   --------------------------------------*/
   String findAllEvents(String type){
      String allEvents = "";
      
      for(Event event: events){
            allEvents += event.showAll(type);         
      }
      
      if(allEvents.isEmpty())
         return "\nNo flights for this Airline";
         
      return allEvents;
   }
   
   /*--------------------------------------
   finds events with matching ori and des
   --------------------------------------*/
   String findSpecificAvailableEvents(String ori, String des, int[] givenDate, SeatClass s){
   
      String eventsAndPrices = "";
      int tempPrice = 0;
      for(Event event: events){
         tempPrice = event.findSpecificAvailableEvents(ori, des, givenDate, s);
         if(tempPrice != -1){
            eventsAndPrices += "~ID: " + event.getID() + "\n" + SeatClass.getClassName(s.getType()) + " Class: $" + tempPrice;
         }
      }
      if(eventsAndPrices.isEmpty()){
         eventsAndPrices = "Nothing exists within the specifications for " + this.name + "\n";
      }
      return eventsAndPrices;
   
   }
   
   /*--------------------------------------
   books a specific place
   --------------------------------------*/
   void bookPlacement(String eventID, SeatClass s, int row, char col) throws InvalidParameterException, IllegalStateException{
      this.bookingTemplateMethod((Object)eventID, (Object)s, (Object)'N', (Object)row, (Object)col);
   }
   
   /*--------------------------------------------------
   books a place with a preference
   --------------------------------------------------*/
   void bookPlacementWithPref(String eventID, SeatClass s, char seatPref) throws InvalidParameterException{
      this.bookingTemplateMethod((Object)eventID, (Object)s, (Object)'P', (Object)seatPref);
   }
   
   /*--------------------------------------------------
   template method for booking
   --------------------------------------------------*/
   void bookingTemplateMethod(Object ... params) throws InvalidParameterException{
      boolean foundEvent = false;
      for(Event event: events){
         if(event.getID().equals((String)params[0])){
            foundEvent = true;
            if((char)params[2] == 'N')
               event.bookPlacement((SeatClass)params[1], (int)params[3], (char)params[4]);
            else
               event.bookPlacementWithPref((SeatClass)params[1], (char)params[3]);//
         }
      }
      if(!foundEvent){
         throw new InvalidParameterException("!!\n~Event not found for " + this.getName() + ".\n" +
                                             "Violation: " + (String)params[0]);
      }
   }
   
   /*--------------------------------------------------
   changes the price of seat/class prices for a 
   specified class, des, and ori
   --------------------------------------------------*/
   void changeSeatClassPrice(String ori, String des, SeatClass s, int price) throws InvalidParameterException{
      for(Price storedPrice: priceList){
         if(storedPrice.matches(ori, des, s)){
            System.out.println("Change price from: $" + storedPrice.getPrice() + " to $" + price + " for " + this.name + ": " + ori + " to " + des + " for " + SeatClass.getClassName(s.getType()) + " Class\n");
            storedPrice.setPrice(price);
         }
      }
   }
   
   /*--------------------------------------------------
   returns line name of type specified type
   --------------------------------------------------*/
   protected String getLineType(String type){
      if(type.equals("Flight"))
         return "Airline";
      return "Cruiseline";
   }
   
   /*-------------------------------------
   writes info to file
   -------------------------------------*/
   void infoToFile(PrintStream writer){
      writer.print("[");
      
      for(int cur = 0; cur < events.size(); cur++){
         events.get(cur).infoToFile(writer);
         if(cur != events.size()-1){
            writer.print(", ");
         }
      }
   }
   
   
   /*--------------------------------------
   makes sure subclasses implement
   --------------------------------------*/   
   protected abstract String getTypeName();

}//end provider