package travelPackage;

import java.util.*;
import java.security.*;
import java.io.*;

public class SystemManager{

   /*--------------------------------------------------
   Fields:
      arraylist of airports
      arraylist of airlines
   --------------------------------------------------*/
   private List<Port> airports;
   private List<Port> shipports;
   private List<Provider> airlines;
   private List<Provider> cruiselines;
   
   
   /*--------------------------------------------------
   constructor: default
   --------------------------------------------------*/
   public SystemManager(){
      this.airports = new ArrayList<Port>();
      this.airlines = new ArrayList<Provider>();
      this.shipports = new ArrayList<Port>();
      this.cruiselines = new ArrayList<Provider>();
   }
   
   
   /*--------------------------------------------------
   creates port of specified type
   --------------------------------------------------*/
   public void createPort(String n, String type){
      try{
         List<Port> portList = this.getPort(type);
         this.checkPortDuplication(n, portList);
          
         portList.add(UtilityFactory.getPortType(n, type));
         if(type.equals("Flight")){
            airports = portList;
         }
         else
            shipports = portList;
      }
      catch(InvalidParameterException e){
         System.out.println(e.getMessage());
      }
   }
   
   /*--------------------------------------------------
   returns the list of specified ports
   --------------------------------------------------*/
   private List<Port> getPort(String type){
      if(type.equals("Flight")){
         return this.airports;
      }
      else
         return this.shipports;
   }
   
   
   /*--------------------------------------------------
   creates providers of specified type
   --------------------------------------------------*/   
   public void createLine(String n, String type){
      
      try{
         List<Provider> lineList = this.getLine(type);
         this.checkLineDuplication(n, lineList);
         
         lineList.add(UtilityFactory.getLineType(n, type));
         if(type.equals("Flight")){
            airlines = lineList;
         }
         else
            cruiselines = lineList;
      }
      catch(InvalidParameterException e){
         System.out.println(e.getMessage());
      }
      
   }
   
   /*--------------------------------------------------
   returns the list of specified providers
   --------------------------------------------------*/
   private List<Provider> getLine(String type){
      if(type.equals("Flight")){
         return this.airlines;
      }
      else
         return this.cruiselines;
   }
   
   
   /*--------------------------------------------------
   creates an event in the matching provider
   --------------------------------------------------*/
   public void createEvent(String lineName, String ori, String des,
                            int year, int month, int day, int hour, int min, String eventID, String type){
                            
      try{
         List<Port> portList = this.getPort(type);
         
         Port[] ports = this.checkOriginDestination(ori, des, portList);
      
         boolean foundLine = false;
         List<Provider> lineList = this.getLine(type);
         for(Provider provider: lineList){
            if(provider.getName().equals(lineName)){
               foundLine = true;
            
               provider.addEvent(ports[0], ports[1], year, month, day, hour, min, eventID);
            
            }
         }
         if(!(foundLine)){
            throw new InvalidParameterException("!!\n~Airline name was not found.\n" +
                                                "Violation: " + lineName);
         }
      }
      catch(InvalidParameterException e){
         System.out.println(e.getMessage());
      }
   }
   
   
   /*--------------------------------------------------
   creates a section in the matching provider
   --------------------------------------------------*/
   public void createSection(String lineName, String eventID, int rows, SeatClass s, char layout, int price, String type){   
      try{
         List<Provider> lineList = this.getLine(type);
         boolean foundLine = false;
         for(Provider provider: lineList){
            if(provider.getName().equals(lineName)){
               foundLine = true;
               provider.addSection(eventID, rows, s, price, layout);
                       
            }
         }
         if(!(foundLine)){
            throw new InvalidParameterException("!!\n~Cruiseline name was not found.\n" +
                                                "Violation: " + lineName);
         }
      }
      catch(InvalidParameterException e){
         System.out.println(e.getMessage());
      }  
   } 
   
   /*--------------------------------------------------
   finds available events of specified type from a
   given origin to a destination
   --------------------------------------------------*/
   public void findAvailableEvents(String ori, String des, String type){
   
      try{
         List<Port> portList = this.getPort(type);
         this.checkOriginDestination(ori, des, portList);
         
         System.out.println(type + "s from " + ori + " to " + des + "\n>>>");
         
         List<Provider> lineList = getLine(type);
         for(Provider provider: lineList){
            provider.findAvailableEvent(ori, des, type);
               
         } 
      }
      catch(InvalidParameterException e){
         System.out.println(e.getMessage());
      }  
   }
   
   /*--------------------------------------------------
   books a placement given specific place info
   --------------------------------------------------*/
   public void bookPlacement(String lineName, String eventID, SeatClass s,
                        int row, char col, String type){
                        
      this.bookingPlacesTemplate((Object)lineName, (Object)eventID, (Object)s, (Object)type, (Object)'N', (Object)row, (Object)col);
   }
   
   /*--------------------------------------------------
   books a placement given a prefernce of seatclass
   and window or aisle
   --------------------------------------------------*/
   public void bookPlacementWithPref(String lineName, String eventID, SeatClass s, char seatPref, String type){
      
      this.bookingPlacesTemplate((Object)lineName, (Object)eventID, (Object)s, (Object)type, (Object)'P', (Object)seatPref);
      
   }
   
   /*--------------------------------------------------
   template to the booking methods
   --------------------------------------------------*/
   private void bookingPlacesTemplate(Object ... params){
      try{
         boolean foundLine = false;
         List<Provider> lineList = this.getLine((String)params[3]);
         for(Provider p: lineList){
            if(p.getName().equals((String)params[0])){
               foundLine = true;
               if((char)params[4] == 'N')
                  p.bookPlacement((String)params[1], (SeatClass)params[2], (int)params[5], (char)params[6]);
               else
                  p.bookPlacementWithPref((String)params[1], (SeatClass)params[2], (char)params[4]);
            }
         }
         if(!(foundLine)){
            throw new InvalidParameterException("!!\n~Airline was not found.\n" +
                                                "Violation: " + (String)params[0]);
         }
      }
      catch(InvalidParameterException e){
         System.out.println(e.getMessage());
      }
      catch(IllegalStateException e){
         System.out.println(e.getMessage());
      }
   }
   
   /*------------------------------------------------
   displays all the details of the specified type's
   subsystem
   ------------------------------------------------*/
   public void displaySystemDetails(String type){
   
      String portType = getPortType(type);
      String lineType = getLineType(type);
   
      String portInfo = portType + ": ";
      
      List<Port> portList = getPort(type);
      
      for(Port port: portList){
         portInfo += "\n" + port.getName();
      }
      
      String lineInfo = lineType + " Info_______>>>";
      
      List<Provider> lineList = getLine(type);
      
      for(Provider provider: lineList){
         lineInfo += "\n" + lineType + " Name: " + provider.getName();
         lineInfo += provider.findAllEvents(type) + "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n";
      }
      
      System.out.println("\t____All System Details____\n" + portInfo + "\n\n" + lineInfo);
   
   }
   
   
   
   /*--------------------------------------------------
   stores the current airport subsystem into a file
   --------------------------------------------------*/
   public void infoToFile(PrintStream writer){

      if(writer != null){
         //print ports
         writer.print("[");
         for(int x = 0; x < airports.size(); x++){
            writer.print(airports.get(x).getName());
            if(x != (airports.size()-1)){
               writer.print(", ");
            }
         
         }
         writer.print("]{");
         
         //now do airline stuff
         for(int x = 0; x < airlines.size(); x++){
            writer.print(airlines.get(x).getName());
            airlines.get(x).infoToFile(writer);
            if(x != airlines.size()-1){
               writer.print(", ");
            }
            
         }
         
         writer.print("}");
      }   
   }
   
   
   /*--------------------------------------------------
   displays information about given events that have
   available placement in a given class that leave
   from a specified ori and arrives at specified dest
   on a particular date(available flights and price)
   --------------------------------------------------*/
   public void findSpecificAvailableEvents(SeatClass s, String ori, String des, int year, 
                                          int month, int day, int hour, int min, String type){
      int[] tempDates = new int[]{month,day,year,hour,min};
      String all = "";
      List<Provider> lineList = this.getLine(type);
      for(Provider provider: lineList){
         all += provider.findSpecificAvailableEvents(ori,  des, tempDates, s);
      }
      System.out.println(all);
   }
   
   /*--------------------------------------------------
   changes the price of the specfied section of given
   type of provider from given origin to destination
   --------------------------------------------------*/
   public void changeSeatClassPrice(String ori, String des, SeatClass s, int price, String lineName, String type){
      
      try{
         List<Port> portList = getPort(type);
         this.checkOriginDestination(ori, des, portList);
         boolean foundLine = false;
         List<Provider> lineList = getLine(type);
         for(Provider provider: lineList){
            if(provider.getName().equals(lineName)){
               foundLine = true;
               provider.changeSeatClassPrice(ori, des, s, price);
               
            }
         }
         if(!(foundLine)){
            throw new InvalidParameterException("!!\n~Airline was not found.\n" +
                                                "Violation: " + lineName);
         }
      }
      catch(InvalidParameterException e){
         System.out.println(e.getMessage());
      }
   }
   
   /*--------------------------------------------------
   returns port name of specifed type
   --------------------------------------------------*/
   private String getPortType(String type){
      if(type.equals("Flight"))
         return "Airports";
      else
         return "Shipports";
   }
   /*--------------------------------------------------
   returns provider name of specified type
   --------------------------------------------------*/
   private String getLineType(String type){
      if(type.equals("Flight"))
         return "Airline";
      else
         return "Cruiseline";
   }
   
   /*--------------------------------------------------
   creates airport system from a file
   --------------------------------------------------*/
   public void createAirportSystemfromFile(File file) throws IllegalStateException{
      Scanner reader = null;
      String fullFile = "";
      
      try{
         reader = new Scanner(file);
         while(reader.hasNextLine()){
            fullFile += reader.nextLine();
         }
      }
      catch(Exception e){
         throw new IllegalStateException("File is bad");
      }
      
      String allPorts = "";
      char cur = '\u0000';
      int x = 0;
      for(; x < fullFile.length() && fullFile.charAt(x) != '{'; x++){
         cur = fullFile.charAt(x);
         if(cur <= 'Z' && cur >= 'A'){
            allPorts += cur;
         }
      }
      for(int i = 0; i < allPorts.length(); i += 3){
         String airport = ("" + allPorts.charAt(i) + allPorts.charAt(i+1) + allPorts.charAt(i+2));
         this.createPort(airport, "Flight");//create ports
      }
      
      //now we will convert to arraylist
      String otherInfo = fullFile.substring(x+1);

      ArrayList<String> components = new ArrayList<String>();
      components.add("");
      char c = '\u0000';
      int current = 0;
      for(int index = 0; index < otherInfo.length(); index++){
         c = otherInfo.charAt(index);
         if((c <= '9' && c >= '0') || (c <= 'Z' && c >= 'A')){
            components.set(current, components.get(current) + c);
         }
         else if(c != ' '){
            components.add("");    
            current++;
         }
      }
      
      //now we will deal with the data
      boolean twoWords = false;
      String currentAirline = "";
      String curr = "";
      String next = "";
      String flightID = "";
      String ori = "";
      String des = "";
      int[] dates = new int[5];
      for(int index = 0; index < components.size() && (!components.get(index).equals("")); index++){
         twoWords = false;
         curr = components.get(index);
         next = components.get(++index);
         for(char ch: next.toCharArray()){
            if(ch <= 'Z' && ch >= 'A'){
               twoWords = true;
               break;
            }
         }
         if(twoWords){
            currentAirline = curr;
            this.createLine(currentAirline, "Flight");
            flightID = next;
            index++;
         }
         else{
            flightID = curr;
         }
         //get dates info
         for(int y = 0; y < 5; y++){
            dates[y] = Integer.parseInt(components.get(index++));
         }
         //get ports
         ori = components.get(index++);
         des = components.get(index++);
         
         //create the flight
         this.createEvent(currentAirline, ori, des, dates[0], dates[1], dates[2], dates[3], dates[4], flightID, "Flight");
         
         //deal with section stuff
         for(; index < components.size() && (!components.get(index).equals(""));){   
               SeatClass s = SeatClass.getTheSeatType(components.get(index++).charAt(0));
               int price = Integer.parseInt(components.get(index++));
               char layout = components.get(index++).charAt(0);
               int rows = Integer.parseInt(components.get(index++));
               this.createSection(currentAirline, flightID, rows, s, layout, price, "Flight");
         }
      }//for-loop of the arraylist of rest of the file
   }//end writetofile   
   
   
   
//----------------------------------------------------------------------------------------------------------------------------------
//------------------------------------------------Methods to check validity---------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------------------------

   private void checkPortDuplication(String name, List<Port> list) throws InvalidParameterException{
      for(Port port: list){
            if(port.getName().equals(name)){
               throw new InvalidParameterException("!!\n~Name already taken.\n"+
                                                   "Violation: " + name);
            }
      }
   }
   
   private void checkLineDuplication(String name, List<Provider> list) throws InvalidParameterException{
      for(Provider provider: list){
            if(provider.getName().equals(name)){
               throw new InvalidParameterException("!!\n~Name already taken.\n" +
                                                   "Violation: " + name);
            }
      }
   }
   
   private Port[] checkOriginDestination(String ori, String des, List<Port> list) throws InvalidParameterException{
      boolean validOri = false;
      boolean validDes = false;
      Port[] ports = new Port[2];
      if(ori.equals(des)){
         throw new InvalidParameterException("!!\n~Cannot have the origin and destination airports the same.\n" +
                                             "Violation: " + ori);
      }
      for(Port port: list){
         if(port.getName().equals(ori))
            ports[0] = port;
            validOri = true;
         if(port.getName().equals(des))
            ports[1] = port;
            validDes = true;
      }
      if(!(validOri)){
         throw new InvalidParameterException("!!\n~Origin Airport not found.\n" +
                                             "Violation: " + ori);
      }
      if(!(validDes)){
         throw new InvalidParameterException("!!\n~Destination Airport not found.\n" +
                                             "Violation: " + des);
      }
      return ports;
   }




}//end class