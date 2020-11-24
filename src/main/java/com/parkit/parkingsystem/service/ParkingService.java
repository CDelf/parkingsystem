package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ParkingService {

  /**
  * Records and displays errors during incoming or
  * exiting the parking.
  */
  private static final Logger LOGGER = LogManager.getLogger("ParkingService");

  /**
   * Initializes fareCalculatorService.
   */
  private static FareCalculatorService fareCalculatorService =
      new FareCalculatorService();

  /**
   * Reads user's inputs.
   */
  private InputReaderUtil inputReaderUtil;
  /**
   * Access to parking's table data.
   */
  private ParkingSpotDAO parkingSpotDAO;
  /**
   * Access to ticket's table data.
   */
  private  TicketDAO ticketDAO;


  /**
   * ParkingService's constructor.
   * @param inputReaderUtil
   *      Reads user's input when he enters or exit the parking
   * @param parkingSpotDAO
   *      Access to parking's table data
   * @param ticketDAO
   *      Access to ticket's table data
   */
  public ParkingService(final InputReaderUtil inputReaderUtil,
        final ParkingSpotDAO parkingSpotDAO, final TicketDAO ticketDAO) {
    this.inputReaderUtil = inputReaderUtil;
    this.parkingSpotDAO = parkingSpotDAO;
    this.ticketDAO = ticketDAO;
  }

  /**
   * Method carrying out steps,
   * updating parkingSpot,
   * and saving ticket's info
   * when the vehicle enters into the parking.
   */
  public void processIncomingVehicle() {
    try {
      ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();
      if (parkingSpot != null && parkingSpot.getId() > 0) {
        String vehicleRegNumber = getVehichleRegNumber();

        // Check if a recurring user is entering,
        // if so displays a welcome message
        if (this.ticketDAO.isRecurringUser(vehicleRegNumber)) {
          System.out.println(
              "Welcome Back! As a recurring user of our parking lot, "
              + "you'll benefit from a 5% discount.");
        }

        //allot this parking space and mark it's availability as false
        parkingSpot.setAvailable(false);
        parkingSpotDAO.updateParking(parkingSpot);
        Date inTime = new Date();
        Ticket ticket = new Ticket();

        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(vehicleRegNumber);
        ticket.setPrice(0);
        ticket.setInTime(inTime);
        ticket.setOutTime(null);
        ticketDAO.saveTicket(ticket);
        System.out.println("Generated Ticket and saved in DB");
        System.out.println(
            "Please park your vehicle in spot number:" + parkingSpot.getId());
        System.out.println(
            "Recorded in-time for vehicle number:"
                + "" + vehicleRegNumber + " is:" + inTime);
      }
    } catch (Exception e) {
      LOGGER.error("Unable to process incoming vehicle", e);
    }
  }

  private String getVehichleRegNumber() throws Exception {
    System.out.println(
        "Please type the vehicle registration number and press enter key");
    return inputReaderUtil.readVehicleRegistrationNumber();
  }

  /**
   * Verify if a slot is available
   * depending to parkingNumber and vehicleType.
   * @return parkingSpot
   */
  public ParkingSpot getNextParkingNumberIfAvailable() {
    int parkingNumber = 0;
    ParkingSpot parkingSpot = null;
    try {
      ParkingType parkingType = getVehichleType();
      parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
      if (parkingNumber > 0) {
        parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);
      } else {
        throw new Exception(
            "Error fetching parking number from DB. "
            + "Parking slots might be full");
      }
    } catch (IllegalArgumentException ie) {
      LOGGER.error("Error parsing user input for type of vehicle", ie);
    } catch (Exception e) {
      LOGGER.error("Error fetching next available parking slot", e);
    }
    return parkingSpot;
  }

  /**
   * Receives and treats user's input when choosing vehicleType.
   * @return parkingType
   */
  public ParkingType getVehichleType() {
    System.out.println("Please select vehicle type from menu");
    System.out.println("1 CAR");
    System.out.println("2 BIKE");
    int input = inputReaderUtil.readSelection();
    switch (input) {
      case 1: {
        return ParkingType.CAR;
      }
      case 2: {
        return ParkingType.BIKE;
      }
      default: {
        System.out.println("Incorrect input provided");
        throw new IllegalArgumentException("Entered input is invalid");
      }
    }
  }

  /**
   * Method carrying out steps,
   * updating parkingSpot,
   * and getting ticket's info
   * when the vehicle exits the parking.
   */
  public void processExitingVehicle() {
    try {
      String vehicleRegNumber = getVehichleRegNumber();
      Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
      Date outTime = new Date();
      ticket.setOutTime(outTime);

      // Applies discount if recurring user
      boolean isDiscount = this.ticketDAO.isRecurringUser(vehicleRegNumber);
      fareCalculatorService.calculateFare(ticket, isDiscount);

      if (ticketDAO.updateTicket(ticket)) {
        ParkingSpot parkingSpot = ticket.getParkingSpot();
        parkingSpot.setAvailable(true);
        parkingSpotDAO.updateParking(parkingSpot);
        System.out.println("Please pay the parking fare:" + ticket.getPrice());
        System.out.println(
            "Recorded out-time for vehicle number:"
            + ticket.getVehicleRegNumber() + " is:" + outTime);
      } else {
        System.out.println(
            "Unable to update ticket information. Error occurred");
      }
    } catch (Exception e) {
      LOGGER.error("Unable to process exiting vehicle", e);
    }
  }
}
