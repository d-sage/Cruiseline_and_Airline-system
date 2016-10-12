package travelPackage;

import java.security.*;

public class Price{
   private int price;
   private String origin;
   private String destination;
   private SeatClass Sclass;
   
   Price(int price, String ori, String des, SeatClass s) throws InvalidParameterException{
      checkParams(price);
   
      this.price = price;
      this.origin = ori;
      this.destination = des;
      this.Sclass = s;
   }
   
   void setPrice(int price) throws InvalidParameterException{
      checkParams(price);
      this.price = price;
   }
   
   int getPrice(){
      return this.price;
   }
   
   boolean matches(String ori, String des, SeatClass s){
      if(this.origin.equals(ori) && this.destination.equals(des) && (this.Sclass.getType() == s.getType()))
         return true;
      return false;
   }
   
   static void checkParams(int price) throws InvalidParameterException{
      if(price < 1)
         throw new InvalidParameterException("!!\n~Price cannot be below 1 dollar.\n" +
                                             "Violation: " + price);
   }
   
}