package travelPackage;

import java.security.*;
import java.io.*;

public abstract class Section{

   protected Placement[][] section;
   protected SeatClass seatClass;
   protected Price price;
   
   /*--------------------------------------
   checks to see if rows is valid
   --------------------------------------*/
   protected static void checkParams(int rows) throws InvalidParameterException{
      if(rows > 100 || rows < 1){
         throw new InvalidParameterException("!!\n~Number of rows for section does not fall within the boundary: 1 <= rows <= 100.\n" +
                                             "Violation: " + rows);
      }
   }
   
   /*--------------------------------------
   marks the place to be taken if it has
   not yet been booked
   --------------------------------------*/
   Placement bookPlacement(int row, char col) throws InvalidParameterException, IllegalStateException{
      if(row > section.length || (col-'A') > (section[0].length)){
         throw new InvalidParameterException("!!\n~Desired Placement is not in the bounds of this section: Rows: " + section.length + ", Columns: " + 'A' + " through " + ((char)(section[0].length-1 + 'A')) + "\n" +
                                             "Violation: Seat: " + row + "" + (char)(col));
      }
      if(section[row-1][col-'A'].isTaken())
         throw new IllegalStateException("!!\n~Placement has already been booked.\n" +
                                             "Violation: Row: " + row + ", Column: " + (char)(col));
      section[row-1][col-'A'].taken();
      return section[row-1][col-'A'];
   }
   
   /*--------------------------------------
   looks to book place with prefernce but
   if it fails, trys to find any place
   --------------------------------------*/
   Placement bookPlacementWithPref(char seatPref){
      Placement place = null;
      if(seatPref == 'W'){
         for(int x = 0; x < this.section.length && place ==null; x++){//goes to rows
            for(int y = 0; y == 0 || y == (section[0].length-1) && place ==null; y+=(section[0].length-1)){//goes to cols
               if(!section[x][y].isTaken()){
                  place = this.bookPlacement(x+1, (char)(y + 'A'));
               }
            }
         }  
      }
      else{
         for(int x = 0; x < this.section.length; x++){//goes to rows
            for(int y = 1; y < this.section[0].length-1; y++){//goes to cols
               if(!section[x][y].isTaken()){
                  place = this.bookPlacement(x+1, (char)(y + 'A'));
               }
            }
         }
      }
      return place;
   }
   
   /*--------------------------------------
   checks if this section has availability
   --------------------------------------*/
   boolean hasAvailablePlacement(){   
      for(int row = 0; row < this.section.length; row++){
         for(int col = 0; col < this.section[row].length; col++){
            if(!(section[row][col].isTaken())){
               return true;
            }
         }
      }
      return false;
   }
   
   /*--------------------------------------
   returns all available and booked places
   --------------------------------------*/
   String showAll(){
      String placesAvail = "\tPrice: $" + this.price.getPrice() + "\nAvailable: \n";
      String placesBooked = "\nBooked: \n";
      
      for(int row = 0; row < this.section.length; row++){
         for(int col = 0; col < this.section[row].length; col++){
            if(!(section[row][col].isTaken())){
               placesAvail += ("\nRow: " + (row+1) + ", Seat: " + (char)(col + 'A'));
            }
            else{
               placesBooked += ("\nRow: " + (row+1) + ", Seat: " + (char)(col + 'A'));
            }
         }
      }
      if(placesBooked.length() == 10)
         placesBooked += "\nNone";
      if(placesAvail.length() == 13)
         placesAvail += "\nNone";
      return (placesAvail + "\n" + placesBooked + "\n\n"); 
   
   }
   
   /*--------------------------------------
   returns a string of all available places
   --------------------------------------*/
   String showAvailability(){
      String places = "";
      for(int row = 0; row < this.section.length; row++){
         for(int col = 0; col < this.section[row].length; col++){
            if(!(section[row][col].isTaken())){
               places += ("\nRow: " + (row+1) + ", Seat: " + (char)(col + 'A'));
            }
         }
      }
      return (places + "\n");
   }
   
   /*--------------------------------------
   writes info to file
   --------------------------------------*/
   void infoToFile(PrintStream writer, char layout){
   
      writer.print(SeatClass.getClassChar(getSeatClassCode()));
      writer.print(":");
      writer.print(this.price.getPrice());
      writer.print(":");
      writer.print(layout);
      writer.print(":");
      writer.print(this.section.length);
   
   }
   
   /*--------------------------------------
   returns true if specified section is
   same as current section
   --------------------------------------*/
   boolean isSection(SeatClass s){
      return (this.seatClass.getType() == s.getType());
   }
   
   /*--------------------------------------
   returns int value of the SeatClass of
   this section
   --------------------------------------*/
   int getSeatClassCode(){
      return this.seatClass.getType();
   }
   
   /*-------------------------------------
   returns price of this section
   -------------------------------------*/
   int getPrice(){
      return this.price.getPrice();
   }
   
   protected abstract void markSection();

}//end class