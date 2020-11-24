package com.parkit.parkingsystem.model;

import java.util.Date;

/**
 * Objects related to Ticket class.
*/
public class Ticket {
  /**
  * Ticket unique id number.
  */
  private int id;
  /**
  * parking spot registered in the ticket.
  */
  private ParkingSpot parkingSpot;
  /**
  * vehicle registration number registered in the ticket.
  */
  private String vehicleRegNumber;
  /**
  * price registered in the ticket.
  */
  private double price;
  /**
  * entry time registered in the ticket.
  */
  private Date inTime;
  /**
  * exit time registered in the ticket.
  */
  private Date outTime;

  /**
  * Gets ticket's id number.
  * @return id
  */
  public int getId() {
    return id;
  }

  /**
  * Sets id of the ticket.
  * @param id
  *      unique id number
  */
  public void setId(final int id) {
    this.id = id;
  }

  /**
  * Gets parking spot registered in the ticket.
  * @return parkingSpot
  */
  public ParkingSpot getParkingSpot() {
    return parkingSpot;
  }

  /**
  * Sets parking spot to register in the ticket.
  * @param parkingSpot
  *      parking spot number
  */
  public void setParkingSpot(final ParkingSpot parkingSpot) {
    this.parkingSpot = parkingSpot;
  }

  /**
  * Gets vehicle registration number registered in the ticket.
  * @return vehicleRegNumber
  */
  public String getVehicleRegNumber() {
    return vehicleRegNumber;
  }

  /**
  * Sets vehicle registration number to register in the ticket.
  * @param vehicleRegNumber
  *       vehicle registration number
  */
  public void setVehicleRegNumber(final String vehicleRegNumber) {
    this.vehicleRegNumber = vehicleRegNumber;
  }

  /**
  * Gets price registered in the ticket.
  * @return price
  */
  public double getPrice() {
    return price;
  }

  /**
  * Sets price to register in the ticket.
  * @param price
  *      price of parking
  */
  public void setPrice(final double price) {
    this.price = price;
  }

  /**
  * Gets entry time registered in the ticket.
  * @return inTime
  */
  public Date getInTime() {
    return inTime;
  }

  /**
  * Sets entry time to register in the ticket.
  * @param inTime
  *      Entry time
  */
  public void setInTime(final Date inTime) {
    this.inTime = inTime;
  }

  /**
  * Gets exit time registered in the ticket.
  * @return outTime
  */
  public Date getOutTime() {
    return outTime;
  }

  /**
  * Sets exit time to register in the ticket.
  * @param outTime
  *      Exit time
  */
  public void setOutTime(final Date outTime) {
    this.outTime = outTime;
  }
}
