package travelPackage;

import java.security.*;

public class Airport extends Port{

   /*--------------------------------------
   constructor
   --------------------------------------*/
   Airport(String n) throws InvalidParameterException{
      checkParams(n);
      
      this.name = n;
   }
   
}//end class