import travelPackage.*;

import java.security.*;
import java.util.*;
import java.io.*;

public class Client{
   public static void main(String[] args){
        
        
      SystemManager systemManager = new SystemManager();
      
      Scanner kb = new Scanner(System.in);
      
      File ams = askForFileCreation(kb);
      
      if(ams != null){
         try{
            systemManager.createAirportSystemfromFile(ams);
         }
         catch(IllegalStateException e){
            System.out.println(e.getMessage());
         }
      }
      
      int choice = 0;
         
      do{
         displayMenu();
      
         choice = choice(kb);
         
         doChosenTask(choice, systemManager, kb);
      
      }while(choice != 12);
         
      System.out.println("Exiting...");
      
      
      
      

         /*//TESTS
         SystemManager sM = new SystemManager();
         
         sM.createAirportSystemfromFile(new File("AMS.txt"));
         
         sM.createPort("DEN", "Flight");
         sM.createPort("LON", "Flight");
         sM.createPort("DEN", "Ship");
         sM.createPort("LON", "Ship");
         
         sM.createLine("DELTA", "Flight");
         sM.createLine("KING", "Ship");
         
         sM.createEvent("DELTA", "DEN", "LON", 2016, 6, 20, 13, 30, "F1", "Flight");
         sM.createEvent("DELTA", "DEN", "LON", 2016, 8, 27, 19, 45, "F2", "Flight");
         sM.createEvent("KING", "DEN", "LON", 2016, 11, 1, 21, 15, "S1", "Ship");
         sM.createEvent("KING", "DEN", "LON", 2016, 6, 20, 13, 30, "S2", "Ship");
         
         sM.createSection("DELTA", "F1", 1, SeatClass.first, 'S', 500, "Flight");
         sM.createSection("DELTA", "F1", 1, SeatClass.economy, 'S', 300, "Flight");
         sM.createSection("DELTA", "F2", 1, SeatClass.business, 'S', 450, "Flight");
         sM.createSection("KING", "S1", 1, SeatClass.first, 'S', 400, "Ship");
         
         sM.changeSeatClassPrice("DEN", "LON", SeatClass.first, 550, "DELTA", "Flight");
         
         
         sM.findAvailableEvents("DEN", "LON", "Flight");
         sM.findAvailableEvents("DEN", "LON", "Ship");
         
         sM.bookPlacement("DELTA", "F1", SeatClass.first, 1, 'A', "Flight");
         sM.bookPlacementWithPref("KING", "S1", SeatClass.first, 'A', "Ship");
         
         sM.findSpecificAvailableEvents(SeatClass.first, "DEN", "LON", 2016, 6, 20, 13, 30, "Flight");
         
         sM.displaySystemDetails("Flight");
         sM.displaySystemDetails("Trip");

         try{
         sM.infoToFile(new PrintStream(new File("Airport_subSyste.txt")));
         }
         catch(FileNotFoundException e){
            e.printStackTrace();
         }*/

      
   
   
   }//end main
   
//========================================================================================================================
//========================================================================================================================
//========================================================================================================================
//========================================================================================================================

   private static File askForFileCreation(Scanner kb){
      
      System.out.print("Would you like to create an Airport system from a file(Y or N): ");
      
      if(kb.next().toUpperCase().charAt(0) == 'Y'){
         System.out.print("Please enter the name for the AMS file: ");
         
         return new File(kb.next());
         
      }
      System.out.println("**Chose not to use a file");
      return null;      
   }
   
   
   
   public static void displayMenu(){
      System.out.println("----------------------------------\n  _____SystemManager_____\n\n" +
                         "Create Airport/Shipport...................................1\n" +
                         "Create Airline/Cruiseline.................................2\n" +
                         "Create Flight/Trip........................................3\n" +
                         "Create FlightSection/CabinSection.........................4\n" +
                         "Book flight/trip placement specifically...................5\n" +
                         "Book flight/trip with a prefernce.........................6\n" +
                         "Find available flights/trips with Origin & Destination....7\n" +
                         "Find available flights/trips with SeatClass, Date, Ports..8\n" +
                         "Change SeatClass prices...................................9\n" +
                         "Display Airport/Shipport sub-System details...............10\n" +
                         "Store Airport sub-System in to a specified file...........11\n" +
                         "Exit......................................................12\n");
   }
   
   public static int choice(Scanner kb){
      int choice = 0;
      do{
         System.out.print("Please enter choice 1-12: ");
         while(!kb.hasNextInt()){
            kb.next();
            System.out.println("Sorry looking for number 1-12");
            System.out.println("Please enter choice 1-12: ");
         }
         choice = kb.nextInt();
      
      }while(choice > 12 || choice < 1);
      return choice;
   }
   
   public static void doChosenTask(int choice, SystemManager sM, Scanner kb){
      switch(choice){
         case 1: //need port name
                 String type = getType(kb);
                 System.out.print("Please enter name for the Port(3 characters in length and only alphabetical symbols): ");
                 String portName = kb.next().toUpperCase();
                 sM.createPort(portName, type);
                 break;
         case 2: //need line name
                 type = getType(kb);
                 System.out.print("Please enter name for Line(less than 6 characters): ");
                 String lineName = kb.next().toUpperCase();
                 sM.createLine(lineName, type);
                 break;
         case 3: //need line name, ori, des, date, eventID
                 type = getType(kb);
                 System.out.print("Please enter Line name: ");
                 lineName = kb.next().toUpperCase();
                 System.out.print("Please enter origin: ");
                 String origin = kb.next().toUpperCase();
                 System.out.print("Please enter destination: ");
                 String destination = kb.next().toUpperCase();
                 int[] date = getDate(kb);
                 System.out.print("Please enter the event ID: ");
                 String eventID = kb.next();
                 sM.createEvent(lineName, origin, destination, date[0], date[1], date[2], date[3], date[4], eventID, type);
                 break;
         case 4: //need line name, eventID, rows, layout, seatclass
                 type = getType(kb);
                 System.out.print("Please enter line name: ");
                 lineName = kb.next().toUpperCase();
                 System.out.print("Please enter event ID: ");
                 eventID = kb.next();
                 int rows = getRows(kb);
                 char layout = getLayout(kb);
                 int price = getPrice(kb);
                 sM.createSection(lineName, eventID, rows, getClass(kb), layout, price, type);
                 break;
         case 5: //need lineName, eventID, s, row, col
                 type = getType(kb); 
                 System.out.print("Please enter line name: ");
                 lineName = kb.next().toUpperCase();
                 System.out.print("Please enter event ID: ");
                 eventID = kb.next();
                 rows = getRows(kb);
                 char col = getCol(kb);
                 layout = getLayout(kb);
                 sM.bookPlacement(lineName, eventID, getClass(kb), rows, col, type);
                 break;
         case 6: //need lineName, eventID, seatclass, preference
                 type = getType(kb);
                 System.out.print("Please enter line name: ");
                 lineName = kb.next().toUpperCase();
                 System.out.print("Please enter event ID: ");
                 eventID = kb.next();
                 sM.bookPlacementWithPref(lineName, eventID, getClass(kb), getLayout(kb), type);
                 break;
         case 7: //need ori, des
                 type = getType(kb);
                 System.out.print("Please enter origin: ");
                 origin = kb.next().toUpperCase();
                 System.out.print("Please enter destination: ");
                 destination = kb.next().toUpperCase();
                 sM.findAvailableEvents(origin, destination, type);
                 break;
         case 8: //need s, ori, des, date
                 type = getType(kb);
                 System.out.print("Please enter origin: ");
                 origin = kb.next().toUpperCase();
                 System.out.print("Please enter destination: ");
                 destination = kb.next().toUpperCase();
                 date = getDate(kb);
                 sM.findSpecificAvailableEvents(getClass(kb), origin, destination, date[0], date[1], date[2], date[3], date[4], type);//modify get row col
                 break;
         case 9: //need ori, des, s, price, lineName
                 type = getType(kb);
                 System.out.print("Please enter line name: ");
                 lineName = kb.next().toUpperCase();
                 System.out.print("Please enter origin: ");
                 origin = kb.next().toUpperCase();
                 System.out.print("Please enter destination: ");
                 destination = kb.next().toUpperCase();
                 sM.changeSeatClassPrice(origin, destination, getClass(kb), getPrice(kb), lineName, type);//modify get row col
                 break;        
         case 10: //nothing
                 type = getType(kb);
                 sM.displaySystemDetails(type);
                 break;
         case 11: //file name
                 sM.infoToFile(getPrinter(kb));
                 break;
      }
   }
   
   private static PrintStream getPrinter(Scanner kb){
      File file = null;
      PrintStream writer = null;
      
      System.out.print("Please enter filename for where the airport system will be stored: ");
      
      try{
         file = new File(kb.next());
         writer = new PrintStream(file);
      }
      catch(FileNotFoundException e){
         System.out.println("File not found");
      }
      
      return writer;
      
   }
   
   private static String getType(Scanner kb){
      System.out.print("Please enter 'A' for air type, or 'S' for ship type: ");
      
      char c = kb.next().toUpperCase().charAt(0);
      while(c != 'A' && c != 'S'){
         System.out.print("Please enter 'A' for air type, or 'S' for ship type: ");
         c = kb.next().toUpperCase().charAt(0);
      }
      
      if(c == 'A')
         return "Flight";
      return "Ship";
      
   }
   
   private static int getPrice(Scanner kb){
      int price = 0;
      
      do{
         System.out.print("Please enter price(less than $99,999): ");
         while(!kb.hasNextInt()){
            System.out.print("Please enter price(less than $99,999): ");
         }
      }while(price < 100000);
      
      return price;
      
   }
   
   private static char getLayout(Scanner kb){
      if(kb.hasNextLine())
         kb.nextLine();
         
      char layout = '\u0000';
      System.out.println("S >>> 1x2,3\n" +
                         "M >>> 1,2x3,4\n" +
                         "W >>> 1,2,3x4,5,6,7x8,9,10\n"); 
      
      while(!(layout == 'S' || layout == 'M' || layout == 'W')){
         System.out.println("Enter layout for the flight section(S,M,W): ");
         layout = (kb.next().charAt(0));
      }
      
      return layout;
   }
   
   private static int[] getDate(Scanner kb){
      if(kb.hasNextLine())
         kb.nextLine();
         
      int[] dates = new int[5];
      boolean numberSoFar = true;
      boolean keepGoing = true;
      
      do{
         numberSoFar = true;
         keepGoing = true;
         System.out.print("Please enter date using numeric values\nof the form 'year,month,day,hour,minute': ");
         String[] temp = kb.nextLine().split(",");
         if(temp.length == 5){
            keepGoing = false;
            for(int x = 0; x < 5; x++){
               for(int i = 0; i < temp[x].length() && numberSoFar; i++){
                  if((temp[x].charAt(i) < '0' || temp[x].charAt(i) > '9')){
                     numberSoFar = false;
                     keepGoing = true;
                  }
               }
               if(numberSoFar)
                  dates[x] = Integer.parseInt(temp[x]);
            }
         }
            
      }while(keepGoing);   
      
      return dates; 
   }
   
   private static int getRows(Scanner kb){
      if(kb.hasNextLine())
         kb.nextLine();
      
      int rows = 0;
      
      System.out.print("Please enter row: ");
      
      while(!kb.hasNextInt()){
         System.out.print("Please enter row: "); 
      }
            
      return kb.nextInt();
   }
   
   private static char getCol(Scanner kb){
      char col = '\u0000';
      
      System.out.print("Please enter col(A-J): ");
      
      col = kb.next().toUpperCase().charAt(0);
      
      while(col < 'A' || col > 'J'){
         System.out.print("Please enter col: ");
      }
      
      return col;      
   }
   
   private static SeatClass getClass(Scanner kb){
      
      classesMenu();
      System.out.print("Please enter choice for type of Seat Class: ");
           
      return getInputandProcess(kb);
   }
   
   private static SeatClass getInputandProcess(Scanner kb){
      
      int choice = 0;
      SeatClass sClass = null;
      do{
         while(!kb.hasNextInt()){
            kb.next();
            System.out.print("Please enter choice for type of Seat Class: ");
         }
         choice = kb.nextInt();
      }while(choice > 3 || choice < 1);
      
      switch(choice){
         case 1: sClass = SeatClass.first;
                 break;
         case 2: sClass = SeatClass.business;
                 break;
         case 3: sClass = SeatClass.economy;
                 break;
      }
      return sClass;
   }
   
   private static void classesMenu(){
      System.out.println("\nFirst.......1\n" +
                         "Business....2\n" +
                         "Economy.....3\n");
   }
   
}//end class