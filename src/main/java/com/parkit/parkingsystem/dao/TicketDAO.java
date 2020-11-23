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

  private static final Logger logger = LogManager.getLogger("TicketDAO");

  public DataBaseConfig dataBaseConfig = new DataBaseConfig();

  /**
   * Saves ticket's informations in database.
   */ 
  public boolean saveTicket(Ticket ticket) {
    Connection con = null;
    try {
      con = dataBaseConfig.getConnection();
      PreparedStatement ps = con.prepareStatement(DataBaseConstants.SAVE_TICKET);
      ps.setInt(1, ticket.getParkingSpot().getId());
      ps.setString(2, ticket.getVehicleRegNumber());
      ps.setDouble(3, ticket.getPrice());
      ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
      ps.setTimestamp(5, (ticket.getOutTime() == null) ? null : (new Timestamp(ticket.getOutTime().getTime())));
      ps.execute();
      dataBaseConfig.closePreparedStatement(ps);
      return true;
    } catch (Exception ex) {
      logger.error("Error fetching next available slot", ex);
    } finally {
      dataBaseConfig.closeConnection(con);
      return false;
    }
  }
  
  /**
   * Retrieves ticket's informations when vehicle exits.
   */
  public Ticket getTicket(String vehicleRegNumber) {
    Connection con = null;
    Ticket ticket = null;
    try {
      con = dataBaseConfig.getConnection();
      PreparedStatement ps = con.prepareStatement(DataBaseConstants.GET_TICKET);
      ps.setString(1, vehicleRegNumber);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        ticket = new Ticket();
        ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)), false);
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
      logger.error("Error fetching next available slot", ex);
      } finally {
      dataBaseConfig.closeConnection(con);
      return ticket;
    }
  }

  /**
   * Updates ticket's informations. 
   */
  public boolean updateTicket(Ticket ticket) {
    Connection con = null;
    try {
      con = dataBaseConfig.getConnection();
      PreparedStatement ps = con.prepareStatement(DataBaseConstants.UPDATE_TICKET);
      ps.setDouble(1, ticket.getPrice());
      ps.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
      ps.setInt(3, ticket.getId());
      ps.execute();
      dataBaseConfig.closePreparedStatement(ps);
      return true;
    } catch (Exception ex) {
      logger.error("Error saving ticket info", ex);
    } finally {
      dataBaseConfig.closeConnection(con);
    }
    return false;
  }

  /**
   * Sets if user is recurring thanks to visitNumber counter. 
   */  
  public boolean isRecurringUser(String vehicleRegNumber) {
    Connection con = null;
    try {
      con = dataBaseConfig.getConnection();
      PreparedStatement ps = con.prepareStatement(DataBaseConstants.GET_VEHICLE);
      ps.setString(1, vehicleRegNumber);
      ResultSet rs = ps.executeQuery();
      int visitNumber = this.visitNumber(rs);
      if (visitNumber > 1) {
        return true;
      }
      dataBaseConfig.closeResultSet(rs);
      dataBaseConfig.closePreparedStatement(ps);
    } catch (Exception ex) {
      logger.error("Error checking if recurring user", ex);
      return false;
    } finally {
      dataBaseConfig.closeConnection(con);
    }
    return false;
  }

  /**
   * Counter of visits. 
   */ 
  private int visitNumber(ResultSet resultSet) throws SQLException {
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
