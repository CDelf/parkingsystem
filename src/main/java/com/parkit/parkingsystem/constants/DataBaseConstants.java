package com.parkit.parkingsystem.constants;

public class DataBaseConstants {

  public static final String GET_NEXT_PARKING_SPOT = "select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?";
  public static final String UPDATE_PARKING_SPOT = "update parking set available = ? where PARKING_NUMBER = ?";
  
  public static final String SAVE_TICKET = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";
  public static final String UPDATE_TICKET = "update ticket set PRICE=?, OUT_TIME=? where ID=?";
  public static final String GET_TICKET = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME  limit 1";

  // Constants used in ParkingDataBaseIT
  public static final String IN_TEST_TICKET = "select t.PARKING_NUMBER, p.TYPE, p.AVAILABLE, t.ID, p.available from ticket t, parking p";
  public static final String OUT_TEST_TICKET = "select t.PRICE, t.OUT_TIME from ticket t";

  // Constant used for User Story 2
  public static final String GET_VEHICLE = "select * from ticket t where t.VEHICLE_REG_NUMBER=? ";

}
