package travelPackage;

import java.security.*;

public class CabinSection extends Section{

   /*--------------------------------------
   constructor
   --------------------------------------*/
   CabinSection(int rows, int columns, Price price, SeatClass s) throws InvalidParameterException{
      checkParams(rows);
   
      this.price = price;
      this.section = new Cabin[rows][columns];
      this.seatClass = s;
      this.markSection();
   }
   
   /*--------------------------------------
   goes to each slot in the section and
   adds a cabin to it
   --------------------------------------*/
   protected void markSection(){
      for(int row = 0; row < this.section.length; row++){
         for(int col = 0; col < this.section[row].length; col++){
            section[row][col] = new Cabin(row,(char)(col + 'A'), this.price);
         }
      }
   }

}//end class