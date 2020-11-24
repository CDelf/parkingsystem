package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

/**
 * Objects related to ParkingSpot class.
*/
public class ParkingSpot {
  /**
  * Parking Number.
  */
  private int number;
  /**
  * Parking type.
  */
  private ParkingType parkingType;
  /**
  * availability of the slot.
  */
  private boolean isAvailable;

  /**
  * Constructor of ParkingSpot.
  * @param number
  *      parking number
  * @param parkingType
  *      parking type
  * @param isAvailable
  *      availability of the slot
  */
  public ParkingSpot(final int number,
      final ParkingType parkingType,
      final boolean isAvailable) {
    this.number = number;
    this.parkingType = parkingType;
    this.isAvailable = isAvailable;
  }

  /**
  * gets parking number.
  * @return number
  */
  public int getId() {
    return number;
  }

  /**
  * sets parking number.
  * @param number
  *     parking number
  */
  public void setId(final int number) {
    this.number = number;
  }

  /**
  * gets vehicle type.
  * @return parkingType
  */
  public ParkingType getParkingType() {
    return parkingType;
  }

  /**
  * sets parking type depending on vehicle type.
  * @param parkingType
  *      related to vehicle type
  */
  public void setParkingType(final ParkingType parkingType) {
    this.parkingType = parkingType;
  }

  /**
  * Boolean that determines if slot is available.
  * @return isAvailable
  */
  public boolean isAvailable() {
    return isAvailable;
  }

  /**
  * sets that slot's available.
  * @param available
  *     Sets availability of the slot
  */
  public void setAvailable(final boolean available) {
    isAvailable = available;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ParkingSpot that = (ParkingSpot) o;
    return number == that.number;
  }

  @Override
    public int hashCode() {
    return number;
  }
}
