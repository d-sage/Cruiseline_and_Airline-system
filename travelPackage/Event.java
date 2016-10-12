package travelPackage;

import java.util.*;
import java.security.*;
import java.time.LocalDateTime;
import java.io.*;

public abstract class Event{

   /*--------------------------------------
   fields
   --------------------------------------*/
   protected String eventID;
   protected List<Section> sections;
   protected Port origin;
   protected Port destination;
   protected int[] date;
   protected char layout;
   
   /*--------------------------------------
   this method checks to see if the date
   and time is valid
   --------------------------------------*/
   protected static void checkDate(int givenYear, int givenMonth, int givenDay, int givenHour, int givenMin) throws InvalidParameterException{
      Calendar cal = Calendar.getInstance();
      int curYear = cal.get(cal.YEAR);
      int curMonth = cal.get(cal.MONTH)+1;
      int curDay = cal.get(cal.DATE);
      LocalDateTime time = LocalDateTime.now();
      int hour = time.getHour();
      int min = time.getMinute();
            
      boolean isLeapYear = (givenYear%4 == 0);
      int daysOfGivenMonth = Months.getDays(givenMonth, isLeapYear);
      int daysOfCurMonth = Months.getDays(curMonth, isLeapYear);
      
      if(givenYear == curYear){
         if(givenMonth < curMonth || givenMonth > 12){
            throw new InvalidParameterException("!!\n~Month not valid, month needs to be equal to or after the current month, " + curMonth + ", and doesn't exceed 12.\n" +
                                                "Violation: " + givenMonth);
         }
         else if(givenMonth > curMonth){
            if(givenDay < 1 || givenDay > daysOfGivenMonth){
               throw new InvalidParameterException("!!\n~Day not valid, day needs to be greater than 1 and less than the max days in the month, " + daysOfGivenMonth + ".\n" +
                                                   "Violation: " + givenDay);
            }
         }
         else{
            if(givenDay < curDay || givenDay > daysOfCurMonth){
               throw new InvalidParameterException("!!\n~Day not valid, day needs be the same as today, " + curDay + ", or less than the days in the month, " + daysOfCurMonth + ".\n" +
                                                   "Violation: " + givenDay);
            }
            else if(givenDay == curDay){
               if((givenHour < hour || givenHour > 23)){
                  throw new InvalidParameterException("!!\n~Given hour needs to be after current hour, " + hour + ", and before 24.\n" +
                                                   "Violation: " + givenHour);
               }
               else if(givenHour == hour){
                  if(givenMin < min || givenMin > 59){
                     throw new InvalidParameterException("!!\n~Given minute needs to be after current minute, " + min + ", and before 60.\n" +
                                                         "Violation: " + givenMin);
                  }
               }
            }
         }
      }
      else if(givenYear > curYear){
         if(givenMonth < 1 || givenMonth > 12){
            throw new InvalidParameterException("!!\n~Month not valid, month needs to be 1 through 12.\n" +
                                                 "Violation: " + givenMonth);
         }
         else{
            if(givenDay < 1 || givenDay > daysOfGivenMonth){
               throw new InvalidParameterException("!!\n~Day not valid, day needs to be greater than 1 and less than days in given month, " + daysOfGivenMonth + ".\n" +
                                                   "Violation: " + givenDay);
            }
         }
      }
      else{
         throw new InvalidParameterException("!!\n~Year not valid, year must be of the current year or after, year " + curYear + ".\n" +
                                             "Violation: " + givenYear);
      }
      
   }
   
   /*--------------------------------------
   returns the eventID
   --------------------------------------*/
   String getID(){
      return this.eventID;
   }
   
   /*--------------------------------------
   returns port name of origin
   --------------------------------------*/
   String getOrigin(){
      return this.origin.getName();
   }
 
   /*--------------------------------------
   returns port name of destination
   --------------------------------------*/
   String getDestination(){
      return this.destination.getName();
   }
   
   /*--------------------------------------
   adds a section to current event
   --------------------------------------*/
   void addSection(int rows, SeatClass s, Price price, char layout) throws InvalidParameterException{
      if(this.layout == '\u0000')
         this.layout = layout;
         
      else if(this.layout != layout)
         throw new InvalidParameterException("Layout of the section, " + this.layout + ", does not match the desired layout for the section.\n" +
                                             "Violation: " + layout);
      if(!(sections.isEmpty())){
         this.checkSection(s);
      }
         
      this.sections.add(UtilityFactory.getSectionType(rows, columnNum(this.layout), s, price, this.getTypeName()));
   }
   
   /*--------------------------------------
   returns int corresponding to layout
   type
   --------------------------------------*/
   private int columnNum(char layout){
      char c = '\u0000';
      switch(layout){
         case 'S': c = 3;
                   break;
         case 'M': c = 4;
                   break;
         case 'W': c = 10;
                   break;
      }
      return c;
   }
   
   /*--------------------------------------
   checks to see if a section of this
   class is already in this event
   --------------------------------------*/
   protected void checkSection(SeatClass s) throws InvalidParameterException{
      for(Section section: sections){
         if(section.isSection(s))
            throw new InvalidParameterException("!!\n~Can only have one type of class on each flight\n" +
                                                "Violation: " + SeatClass.getClassName(s.getType()));
      }
   }
   
   /*--------------------------------------
   books a placement with specifics
   --------------------------------------*/
   void bookPlacement(SeatClass s, int row, char col) throws InvalidParameterException, IllegalStateException{
      boolean foundClass = false;
      for(Section section: sections){
         if(section.isSection(s)){
            foundClass = true;
            Placement temp = section.bookPlacement(row, col);
            System.out.println("\nBooked place.\n" + temp.getPlace() + "\nThe price is: " + temp.getPrice() +"\n");
         }
      }  
      if(!(foundClass)){
         throw new InvalidParameterException("!!\n~SeatClass not found on this flight.\n" + 
                                             "Violation: " + SeatClass.getClassName(s.getType()));
      } 
   }
   
   /*--------------------------------------
   books a placement with a preference
   --------------------------------------*/
   void bookPlacementWithPref(SeatClass s, char seatPref){
      boolean foundClass = false;
      for(Section section: sections){
         if(section.isSection(s)){
            foundClass = true;
            Placement temp = section.bookPlacementWithPref(seatPref);
            boolean bookedPref = temp != null;
            if(bookedPref){
               System.out.println("Booked with preference.\n" + temp.getPlace() + "\nThe price is: " +temp.getPrice() +"\n");
            }
            else{
               temp = section.bookPlacementWithPref(oppositePref(seatPref));
               bookedPref = temp != null;
               if(bookedPref){
                  System.out.println("Booked without preference, sorry.\n" + temp.getPlace() + "\nThe price is: " +temp.getPrice() +"\n");
               }
               else
                  System.out.println("Sorry no places available.");
            }
         }
      }
      if(!foundClass){
         throw new InvalidParameterException("!!\n~Class not found.\n" +
                                             "Violation: " + SeatClass.getClassName(s.getType()));
      }
   }
   
   /*--------------------------------------
   returns opposite of prefernce
   W -> A and A -> W
   --------------------------------------*/
   private char oppositePref(char pref){
      if(pref == 'W')
         return 'A';
      return 'W';
   }
   
   /*--------------------------------------
   checks to see if any section has
   available placements
   --------------------------------------*/
   boolean hasAvailablePlacement(){
      for(Section section: sections){
         if(section.hasAvailablePlacement()){
            return true;
         }
      }
      return false;
   }
   
   /*--------------------------------------
   gets all placements that are not taken
   --------------------------------------*/
   String showAvailability(){
      String IDandPlaces = "";
      for(Section section: sections){
         IDandPlaces += ("\n" + SeatClass.getClassName(section.getSeatClassCode()) + " Class:");
         IDandPlaces += section.showAvailability();
      }
      return IDandPlaces;
   }
   
   /*--------------------------------------
   gets all placements taken or not
   --------------------------------------*/
   String showAll(String type){
      String eventInfo = ("\n\n" + type + " ID: " + this.eventID + "\nDate: " + this.getDate() + "\nFrom: " + this.origin.getName() + "\nTo: " + this.destination.getName() + "\n" + "Layout: " + this.layout + ": " +
                          this.displayLayout() + "\n");
      String placementInfo = "";
      for(Section section: sections){
         placementInfo += ("\n" + SeatClass.getClassName(section.getSeatClassCode()) + " Class:");
         placementInfo += section.showAll();
      }
      
      return (eventInfo + placementInfo);  
   }
   
   /*-------------------------------------
   returns date in string form
   -------------------------------------*/
   private String getDate(){
      return (this.date[0] + "-" + this.date[1] + "-" + this.date[2] + "_" + this.date[3] + ":" + this.date[4]);
   }
   
   /*-------------------------------------
   checks origin, destination, and date
   to see if matches
   -------------------------------------*/
   int findSpecificAvailableEvents(String ori, String des, int[] givenDate, SeatClass s){
   
      boolean dateSame = this.checkDate(givenDate);
      if(ori.equals(this.origin) && des.equals(this.destination) && dateSame){
         for(Section section: sections){
            if(section.isSection(s)){
               if(section.hasAvailablePlacement()){
                  return section.getPrice();
               }
            }
         }
      }
      return -1;  
   
   }
   
   /*--------------------------------------
   returns true if date is same
   --------------------------------------*/
   private boolean checkDate(int[] givenDate){
      for(int x = 0; x < 5; x++){
         if(this.date[x] != givenDate[x])
            return false;
      }
      return true;
   }
   
   /*--------------------------------------
   returns the look of the layout
   --------------------------------------*/
   private String displayLayout(){
      String s = "";
      switch(this.layout){
         case 'S': s = "A,x,B,C";
                   break;
         case 'M': s = "A,B,x,C,D";
                   break;
         case 'W': s = "A,B,C,x,D,E,F,G,x,H,I,J";
                   break;
         default: s = "None Available";
      }
      return s;
   }
   
   /*--------------------------------------
   writes info to a file
   --------------------------------------*/
   void infoToFile(PrintStream writer){
   
      writer.print(this.eventID);
      writer.print("|");
      writer.print(this.date[2] + ", " + this.date[0] + ", " + this.date[1] + ", " + this.date[3] + ", " + this.date[4]);
      writer.print("|");
      writer.print(this.origin.getName());
      writer.print("|");
      writer.print(this.destination.getName());
      writer.print("[");
      
      for(int cur = 0; cur < this.sections.size(); cur++){
         sections.get(cur).infoToFile(writer, this.layout);
         if(cur != this.sections.size()-1)
            writer.print(",");
      }
      
      writer.print("]");
   
   }
   
   /*--------------------------------------
   makes sure subclasses implement
   --------------------------------------*/
   protected abstract String getTypeName();
   
}//end event