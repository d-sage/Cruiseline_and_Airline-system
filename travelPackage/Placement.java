package travelPackage;

public abstract class Placement{

   /*--------------------------------------
   fields
   --------------------------------------*/
   protected int row;
   protected char col;
   protected boolean taken;
   protected Price price;
   
   
   /*------------------------------
   returns if the place is taken
   or empty
   ------------------------------*/
   boolean isTaken(){
      return taken;
   }
   
   /*------------------------------
   marks the placement taken
   ------------------------------*/
   void taken(){
      this.taken = true;
   }
   
   /*--------------------------------------
   returns the placement
   --------------------------------------*/
   String getPlace(){
      return "" + (row+1) + col;
   }
   
   /*--------------------------------------
   returns price of the place
   --------------------------------------*/
   int getPrice(){
      return this.price.getPrice();
   }


}//end class