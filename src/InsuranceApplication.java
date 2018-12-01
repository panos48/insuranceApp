import java.text.SimpleDateFormat;
import java.util.*;

public class InsuranceApplication {

    public static void main(String[] args) {
        //call the method which display the options to the user
        displayOptions();
    }
    //display the options about functionality and the type that you want to use your data to user
    public static void displayOptions(){
        //functionality option and type option
        int inputFunctionality;
        int inputUseType;

        System.out.println("---Select Functionality to perform:\n" + "*1-Insurance status of a vehicle\n" + "*2-Uninsured vehicles in Xdays\n"
                + "*3-Uninsured vehicles\n" + "*4-Calculator fine");
        //check for valid functionality option input with inputValidation
        inputFunctionality = (int) inputOptionValidation(4);
        System.out.println("---Enter export type:\n" + "*1-File\n" + "*2-Console");
        //check for valid type option input with inputValidation
        inputUseType = (int) inputOptionValidation(2);
        if (inputUseType == 1) {
            //call callFile to read data from a file and export the results to a File
            callFile(inputFunctionality);
        } else if (inputUseType == 2) {
            //call callDatabase to read data from database and export the results to console
            callDatabase(inputFunctionality);
        }
    }
    //import data from a file to fill lists of objects
    //export data to a file the results
    public static void callFile(int inputFunctionality){
        //create an object ParserReader to get data from a file or database
        ParserReader read = new ParserReader();
        //create an object ParserWriter to write the results to a file or to export them to a database
        ParserWriter write = new ParserWriter();
        //create a list of object owner that has all the owners data
        List<Owner> ownerTemp = new ArrayList<>();
        //create a list of object vehicle that has all the vehicles data
        List<Vehicle> vehicleTemp = new ArrayList<>();
        //create a list of object insurance that has all the insurances data
        List<Insurance> insuranceTemp = new ArrayList<>();
        //call the ReaderFile to fill the lists with data from the import file
        read.ReaderFile(ownerTemp, vehicleTemp, insuranceTemp);
        //use this 'result' variable to check if contidion
        int result = 0;
        //variable 'i' contains the position of an object
        int i = 0;
        String exportData = null;
        //if functionality option is '1' then ask user to import the plate of a vehicle in format (XXX-0000)
        //that want to know the insurance status and if there is in the import file
        //then call .writing to export insurance status to a file
        if (inputFunctionality == 1) {
            Scanner scan = new Scanner(System.in);
            System.out.println("Enter a valid plate: (XXX-0000) ");
            //take the plate that user wrote
            String inputPlate = scan.nextLine();
            //check for valid plate format with inputPlateValidation
            inputPlate = inputPlateValidation(inputPlate);
            for (Vehicle str : vehicleTemp) {
                //check if there is a vehicle that has a plate which user want the insurance status
                if (result == vehicleTemp.get(i).getPlate().compareTo(inputPlate)) {
                    exportData = ownerTemp.get(i).getFirstName() + " " + ownerTemp.get(i).getSurName() + " " + ownerTemp.get(i).getDriverLicense()
                            + " " + vehicleTemp.get(i).getPlate() + " " + vehicleTemp.get(i).getModel()
                            + " " + insuranceTemp.get(i).getInsuranceID() + " " + insuranceTemp.get(i).getStartDateInsurance() + " " + insuranceTemp.get(i).getExpiredDateInsurance();
                    //write in a file the insurance status of the vehicle
                    write.writing(exportData);
                    break;
                }
                i++;
            }
        }//if functionality is 2 then callCheckDate which ask you to insert Xdays and returns the day from today+Xdays
        //and export to a file the insurance status of every uninsured vehicle that has a expireDate lower than today+Xdays
        else  if (inputFunctionality == 2){
            String expireDate = callCheckDate();
            for (Insurance str : insuranceTemp) {
                //check every insurance that expires before today+Xdays
                if (insuranceTemp.get(i).getExpiredDateInsurance().compareTo(expireDate)<0){
                    exportData = ownerTemp.get(i).getFirstName() + " " + ownerTemp.get(i).getSurName() + " " + ownerTemp.get(i).getDriverLicense()
                            + " " + vehicleTemp.get(i).getPlate() + " " + vehicleTemp.get(i).getModel()
                            + " " + insuranceTemp.get(i).getInsuranceID() + " " + insuranceTemp.get(i).getStartDateInsurance() + " " + insuranceTemp.get(i).getExpiredDateInsurance();
                    //write in a file the insurance status of every uninsured vehicle
                    write.writing(exportData);
                }
                i++;
            }
        }//if functionality is 3 then check which insurnances are expired,
        //check if expireDate is lower than currentDate
        //then we sort the results by play and export them to a file
        else if (inputFunctionality == 3){
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            String currentDate = date.format(c.getTime());
            System.out.println(currentDate);
            List<Vehicle> vehicleOfUninsured = new ArrayList<>();
            for (Insurance str : insuranceTemp) {
                //check which vehicles are uninsured
                if (insuranceTemp.get(i).getExpiredDateInsurance().compareTo(currentDate)<0){
                    vehicleOfUninsured.add(vehicleTemp.get(i));
                }
                i++;
            }
            Collections.sort(vehicleOfUninsured);
            for (Vehicle str:vehicleOfUninsured){
                exportData = str.getPlate()+ " " + str.getModel();
                write.writing(exportData);
            }
        }
        else if (inputFunctionality == 4){
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            String currentDate = date.format(c.getTime());
            System.out.println(currentDate);
            List<Owner> ownerOfUninsured = new ArrayList<>();
            for (Insurance str : insuranceTemp) {
                //check which vehicles are uninsured
                if (insuranceTemp.get(i).getExpiredDateInsurance().compareTo(currentDate)<0){
                    ownerOfUninsured.add(ownerTemp.get(i));
                }
                i++;
            }
            Set<Owner> hset = new HashSet<Owner>(ownerOfUninsured);

            int j = 0;
            for (Owner owner : hset) {
                j++;
                exportData = owner.getFirstName() + " " + owner.getSurName()
                        + " has to pay " + calculateFine(5,Collections.frequency(ownerOfUninsured, owner)) + " € in fine";
                write.writing(exportData);
            }
        }
    }
    //calculate total fine that owner has to pay
    public static int calculateFine(int inputFine, int numberOfUninsuredVehicles){
        return inputFine * numberOfUninsuredVehicles;
    }
    //callDatabase read a plate from user ,then make a connection to a database and make a query
    public static void callDatabase(int inputFunctionality) {
        ParserReader read = new ParserReader();
        String inputPlate = null;
        String inputExpireDate = null;
        if (inputFunctionality == 1){
            Scanner scan = new Scanner(System.in);
            System.out.println("Enter a valid plate number: (XXX-0000) ");
            inputPlate = scan.nextLine();
            inputPlate = inputPlateValidation(inputPlate);
            inputPlate = "'"+inputPlate+"'";
        }
        else if (inputFunctionality == 2){
            inputExpireDate = callCheckDate();
        }
        else if (inputFunctionality == 3){
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            String expireDate = date.format(c.getTime());

            inputExpireDate = expireDate;
        }
        //make a connection to a database
        read.connectorDatabase(inputPlate, inputExpireDate, inputFunctionality);
    }
    //add Xdays in currentDate and return the result
    public static String callCheckDate() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Give X-Days to check which insurances will expire until then");
        int inputXDays = scan.nextInt();
        //date format
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        //Number of Days to add
        c.add(Calendar.DAY_OF_MONTH, inputXDays);
        //Date after adding the days to the given date
        String expireDate = date.format(c.getTime());

        return expireDate;
    }
    //check if option is valid until it is
    public static int inputOptionValidation(int validOption){
        Scanner input = new Scanner(System.in);
        int inputOption = input.nextInt();
        do {
            if (inputOption <= 0  || inputOption > validOption) {
                System.out.println("Wrong input, choose a right one");
                input = new Scanner(System.in);
                inputOption = input.nextInt();
            }
        }while ( inputOption <= 0 || inputOption > validOption);

        return inputOption;
    }
    //check if plate is in xxx-1111 format by checking the all the parametres
    public static String inputPlateValidation(String inputPlate) {
        inputPlate = inputPlate.toUpperCase();
        if (inputPlate.length() == 8 && inputPlate.contains("-")) {
                String[] sp = inputPlate.split("-");
                if (isAlpha(sp[0]) && sp[0].length() == 3) {
                    try {
                        Integer.parseInt(sp[1]);
                    } catch (Exception e) {
                        System.out.println(inputPlate + " is a an invalid plate number.");
                    }
                } else {
                    System.out.println(inputPlate + " is a an invalid plate number.");
                }
        } else{
            System.out.println(inputPlate + " is a an invalid plate number.");

        }

        return inputPlate;
    }
    //check if a char is a letter
    public static boolean isAlpha(String name) {
        char[] chars = name.toCharArray();
        for (char c : chars) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }
}