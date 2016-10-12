package travelPackage;

import java.security.*;

public class ShipPort extends Port{

   /*--------------------------------------
   constructor
   --------------------------------------*/
   ShipPort(String n) throws InvalidParameterException{
      checkParams(n);
      
      this.name = n;
   }

}//end class