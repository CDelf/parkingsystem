package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DataBaseConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TicketDAO {
  /**
  * Records errors if a method fails here.
  */
  private static final Logger LOGGER = LogManager.getLogger("TicketDAO");
  /**
  * Database configuration for TicketDAO.
  */
  public DataBaseConfig dataBaseConfig = new DataBaseConfig();

  /**
  * Saves ticket's informations in database.
  * @param ticket
  *      a new ticket is created when vehicle enters,
  *      with incoming informations.
  * @return boolean
  *      true if success, false if failure
  */
  public boolean saveTicket(final Ticket ticket) {
    Connection con = null;
    try {
      con = dataBaseConfig.getConnection();
      PreparedStatement ps =
          con.prepareStatement(DataBaseConstants.SAVE_TICKET);
      ps.setInt(1, ticket.getParkingSpot().getId());
      ps.setString(2, ticket.getVehicleRegNumber());
      ps.setDouble(3, ticket.getPrice());
      ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
      ps.setTimestamp(5, (ticket.getOutTime() == null)
          ? null : (new Timestamp(ticket.getOutTime().getTime())));
      ps.execute();
      dataBaseConfig.closePreparedStatement(ps);
      return true;
    } catch (Exception ex) {
      LOGGER.error("Error fetching next available slot", ex);
    } finally {
      dataBaseConfig.closeConnection(con);
      return false;
    }
  }

  /**
   * Retrieves ticket's informations when vehicle exits.
   * @param vehicleRegNumber
   *     Recovers ticket thanks to the vehicle registration number.
   * @return ticket
   *      with updated informations.
   */
  public Ticket getTicket(final String vehicleRegNumber) {
    Connection con = null;
    Ticket ticket = null;
    try {
      con = dataBaseConfig.getConnection();
      PreparedStatement ps = con.prepareStatement(DataBaseConstants.GET_TICKET);
      ps.setString(1, vehicleRegNumber);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        ticket = new Ticket();
        ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1),
            ParkingType.valueOf(rs.getString(6)), false);
        ticket.setParkingSpot(parkingSpot);
        ticket.setId(rs.getInt(2));
        ticket.setVehicleRegNumber(vehicleRegNumber);
        ticket.setPrice(rs.getDouble(3));
        ticket.setInTime(rs.getTimestamp(4));
        ticket.setOutTime(rs.getTimestamp(5));
      }
      dataBaseConfig.closeResultSet(rs);
      dataBaseConfig.closePreparedStatement(ps);
    } catch (Exception ex) {
      LOGGER.error("Error fetching next available slot", ex);
    } finally {
      dataBaseConfig.closeConnection(con);
      return ticket;
    }
  }

  /**
  * Updates ticket's informations.
  * @param ticket
  *     Gets ticket and updates information at the end of parking
  * @return boolean
  *     true if success, false if failure
  */
  public boolean updateTicket(final Ticket ticket) {
    Connection con = null;
    try {
      con = dataBaseConfig.getConnection();
      PreparedStatement ps =
          con.prepareStatement(DataBaseConstants.UPDATE_TICKET);
      ps.setDouble(1, ticket.getPrice());
      ps.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
      ps.setInt(3, ticket.getId());
      ps.execute();
      dataBaseConfig.closePreparedStatement(ps);
      return true;
    } catch (Exception ex) {
      LOGGER.error("Error saving ticket info", ex);
    } finally {
      dataBaseConfig.closeConnection(con);
    }
    return false;
  }

  /**
  * Sets if user is recurring thanks to visitNumber counter.
  * @param vehicleRegNumber
  *     Looks for existing vehicle registration number in
  *     the database to set if user is recurring
  * @return boolean
  *      true if recurring, false if not
  */
  public boolean isRecurringUser(final String vehicleRegNumber) {
    Connection con = null;
    try {
      con = dataBaseConfig.getConnection();
      PreparedStatement ps =
          con.prepareStatement(DataBaseConstants.GET_VEHICLE);
      ps.setString(1, vehicleRegNumber);
      ResultSet rs = ps.executeQuery();
      int visitNumber = this.visitNumber(rs);
      if (visitNumber > 1) {
        return true;
      }
      dataBaseConfig.closeResultSet(rs);
      dataBaseConfig.closePreparedStatement(ps);
    } catch (Exception ex) {
      LOGGER.error("Error checking if recurring user", ex);
      return false;
    } finally {
      dataBaseConfig.closeConnection(con);
    }
    return false;
  }

  /**
  * Counter of visits.
  * @param resultSet
  *     Counts number of rows in results of sql request
  * @return i
  *      Number of rows = number of visits
  */
  private int visitNumber(final ResultSet resultSet) throws SQLException {
    try {
      int i = 1;
      while (resultSet.next()) {
        i++;
      }
      return i;
    } catch (Exception e) {
      System.out.println("Error getting row count");
      e.printStackTrace();
      return 0;
    }
  }
}
