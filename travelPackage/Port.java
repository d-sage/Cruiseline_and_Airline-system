package travelPackage;

import java.security.*;

public abstract class Port{

   protected String name;
   
   /*--------------------------------------
   returns the name of the port
   --------------------------------------*/
   String getName(){
      return this.name;
   }
   
   /*--------------------------------------
   this method checks to see if the name
   for the port is valid
   --------------------------------------*/
   protected static void checkParams(String n) throws InvalidParameterException{
      if(n.length() != 3){
         throw new InvalidParameterException("!!\n~Airport names are exactly three characters long.\n" +
                                             "Violation: " + n);
      }
      for(int x = 0; x < 3; x++){
         if(n.charAt(x) < 'A' || n.charAt(x) > 'Z'){
            throw new InvalidParameterException("!!\n~Airport name can only contain alphabetical characters.\n"+
                                                "Violation: " + n);
         }
      }
   }

}//end port