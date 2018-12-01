import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

//class' purpose is to connect in database, make a query and export the result in console
//or to readFile and fill the lists of objects
public class ParserReader {
    //make a connection in database and make a query, the result-insurance status is exported in console
    public void connectorDatabase(String inputPlate, String inputExpireDate, int inputQuery) {
          String url;
          Connection conn = null;
          Statement stmt = null;
          ResultSet rs = null;

          try {
              //connect to a database with this url,username and password
              Class.forName("com.mysql.jdbc.Driver");
              url = "jdbc:mysql://localhost:3306/insurancedatabase?useSSL=false";
              conn = DriverManager.getConnection(url, "root", "root");
              stmt = conn.createStatement();
              List<Vehicle> vehicleOfUninsured = new ArrayList<>();
              int i = 0;
              //make a query chosen by functionality
              rs = stmt.executeQuery(makeQuery(inputQuery,inputPlate,inputExpireDate));
              while (rs.next()) {
                  if (inputQuery!=3){

                      String licenseId = rs.getString("licenseID");
                      String firstName = rs.getString("fname");
                      String surname= rs.getString("surname");
                      String plate = rs.getString("plate");
                      String model = rs.getString("model");
                      String insuranceID = rs.getString("insuranceID");
                      String startDate = rs.getString("startDate");
                      String expireDate = rs.getString("expireDate");
                      //diplay the insurance status in console
                      System.out.println(licenseId + " " + firstName + "\t " + surname
                              + "\t " + plate + "\t " + model
                              + "\t " + insuranceID + "\t " + startDate + "\t " + expireDate);

                  }
                  else if (inputQuery == 3){
                      Vehicle uninsuredVehicle= new Vehicle();
                      uninsuredVehicle.setPlate(rs.getString("plate"));
                      uninsuredVehicle.setModel(rs.getString("model"));
                      vehicleOfUninsured.add(uninsuredVehicle);
                      i++;
                  }
                  else if (inputQuery == 4){

                  }
              }
              Collections.sort(vehicleOfUninsured);
              for (Vehicle str:vehicleOfUninsured)
                  System.out.println(str.getPlate() +" "+ str.getModel());
          } catch (Exception e) {
              System.err.println("Got an exception! ");
              System.err.println(e.getMessage());
          } finally { //close connection ,stmt and resultset here
              //BAD PRACTISE
              try {
                  conn.close();
              } catch (SQLException se) { /*can't do anything */ }
              try {
                  stmt.close();
              } catch (SQLException se) { /*can't do anything */ }
              try {
                  rs.close();
              } catch (SQLException se) { /*can't do anything */ }
          }
      }
    //open a file and fill the lists of objects
    public void ReaderFile(List<Owner> ownerData, List<Vehicle> vehicleData, List<Insurance> insuranceData){

        //pathfile and filename to open
        String fileNameDefined = "inputFile.csv";
        File file = new File(fileNameDefined);

        try{
            //read from filePooped with Scanner class
            Scanner inputStream = new Scanner(file);
            //hashNext() loops line-by-line
            while(inputStream.hasNext()){
                String temp = null;
                String[] temp1 = null;

                Owner ownerTemp = new Owner();
                Vehicle vehicleTemp = new Vehicle();
                Insurance insuranceTemp = new Insurance();

                temp = inputStream.next();
                temp1 = temp.split(",");

                ownerTemp.setFirstName(temp1[0]);
                ownerTemp.setSurName(temp1[1]);
                try {
                    ownerTemp.setDriverLicense(Integer.parseInt(temp1[2]));
                } catch (NumberFormatException e) {
                    //Do something! Anything to handle the exception.
                }
                vehicleTemp.setPlate(temp1[3]);
                vehicleTemp.setModel(temp1[4]);
                try {
                   insuranceTemp.setInsuranceID(Integer.parseInt(temp1[5]));
                } catch (NumberFormatException e) {
                    //Do something! Anything to handle the exception.
                }
                insuranceTemp.setStartDateInsurance(temp1[6]);
                insuranceTemp.setExpiredDateInsurance(temp1[7]);

                ownerData.add(ownerTemp);
                vehicleData.add(vehicleTemp);
                insuranceData.add(insuranceTemp);
            }
            //after loop, close scanner
            inputStream.close();
        }catch (FileNotFoundException e){
            System.out.println("FILE NOT FOUND");
            e.printStackTrace();
        }
    }
    //make a query by functionality
    public String makeQuery(int inputQuery, String inputPlate, String inputExpireDate){
        if (inputQuery == 1){
              return "select * from owners,vehicle,insurance where licenseID = (select licenseID from hasvehicle where plate = "+inputPlate+") " +
                      "and plate = "+inputPlate+" " +
                      "and insuranceID = (select insuranceID from isinsured where plate = "+inputPlate+")";
        }
        else if (inputQuery == 2){
              return "select has.licenseID, fname, surname, v.plate, model, ins.insuranceID, startDate ,expireDate\n" +
                      "from owners as o inner join hasvehicle as has\n" +
                      "on o.licenseID = has.licenseID\n" +
                      "inner join vehicle v\n" +
                      "on has.plate = v.plate\n" +
                      "inner join isinsured isin\n" +
                      "on v.plate = isin.plate\n" +
                      "inner join insurance ins\n" +
                      "on isin.insuranceID = ins.insuranceID\n" +
                      "where expireDate<'"+inputExpireDate +"';";
        }
        else if (inputQuery == 3){
            return "select has.licenseID, fname, surname, v.plate, model, ins.insuranceID, startDate ,expireDate\n" +
                    "from owners as o inner join hasvehicle as has\n" +
                    "on o.licenseID = has.licenseID\n" +
                    "inner join vehicle v\n" +
                    "on has.plate = v.plate\n" +
                    "inner join isinsured isin\n" +
                    "on v.plate = isin.plate\n" +
                    "inner join insurance ins\n" +
                    "on isin.insuranceID = ins.insuranceID\n" +
                    "where expireDate<'"+inputExpireDate +"';";
        }
        return null;
    }
}