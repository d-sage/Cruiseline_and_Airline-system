package travelPackage;

public enum SeatClass{
   first(1), 
   business(2), 
   economy(3);
   
   private final int type;
   
   SeatClass(int type){
      this.type = type;
   }
   
   int getType(){
      return this.type;
   }
   
   static String getClassName(int type){
      if(type == 1){
         return "First";
      }
      else if(type == 2){
         return "Business";
      }
      else{
         return "Economy";
      }
   }
   
   static String getClassChar(int type){
      if(type == 1){
         return "F";
      }
      else if(type == 2){
         return "B";
      }
      else{
         return "E";
      }
   }
   
   public static SeatClass getTheSeatType(char c){
      SeatClass s = null;
      switch(c){
         case 'E': s = SeatClass.economy;
            break;
         case 'F': s = SeatClass.first;
            break;
         case 'B': s = SeatClass.business;
            break;
      }
      return s;
   }
   
}//end enum class