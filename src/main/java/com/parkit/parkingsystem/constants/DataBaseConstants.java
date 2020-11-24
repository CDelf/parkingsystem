package com.parkit.parkingsystem.constants;

public class DataBaseConstants {
  /**
  * Constant used when vehicle enters
  * into the parking to check if available slot.
  */
  public static final String GET_NEXT_PARKING_SPOT =
      "select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?";
  /**
  * Updates parking spot :
  * false when vehicle enters, true when it exits.
  */
  public static final String UPDATE_PARKING_SPOT =
      "update parking set available = ? where PARKING_NUMBER = ?";
  /**
  * Saves ticket's informations
  * when vehicle enters into the parking.
  */
  public static final String SAVE_TICKET =
      "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";
  /**
  * Updates ticket's information
  * at the end of the parking.
  */
  public static final String UPDATE_TICKET =
      "update ticket set PRICE=?, OUT_TIME=? where ID=?";
  /**
  * Gets ticket's informations when the vehicle exits.
  */
  public static final String GET_TICKET =
      "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME  limit 1";

  // Constants used in ParkingDataBaseIT
  /**
  * Gets incoming informations
  * for integration database test.
  */
  public static final String IN_TEST_TICKET =
      "select t.PARKING_NUMBER, p.TYPE, p.AVAILABLE,t.ID, p.available from ticket t, parking p";
  /**
  * Gets exiting informations
  * for integration database test.
  */
  public static final String OUT_TEST_TICKET =
      "select t.PRICE, t.OUT_TIME from ticket t";

  // Constant used for User Story 2
  /**
  * Checks if the vehicle registration number is in the database.
  */
  public static final String GET_VEHICLE =
      "select * from ticket t where t.VEHICLE_REG_NUMBER=? ";
}
