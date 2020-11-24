package com.parkit.parkingsystem.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataBaseConfig {

  /**
  * Records and displays informations or errors during connection
  * and preparedStatement opening/closing.
  */
  private static final Logger LOGGER = LogManager.getLogger("DataBaseConfig");

  /**
  * Configures database connection.
  * @return DriveManager.getConnection(url,user,password)
  * @throws ClassNotFoundException, SQLException,
  * FileNotFoundException, IOException
  */
  public Connection getConnection()
      throws ClassNotFoundException, SQLException,
      FileNotFoundException, IOException {
    LOGGER.info("Create DB connection");
    Class.forName("com.mysql.cj.jdbc.Driver");
    Properties properties = new Properties();
    properties.load(new FileInputStream(
        new File(".\\.settings\\credentials.properties")));
    String url = properties.getProperty("urlProd");
    String user = properties.getProperty("username");
    String password = properties.getProperty("password");
    return DriverManager.getConnection(url, user, password);
  }

  /**
  * Closing of the connection.
  * @param con
  *      Connection to the database
  */
  public void closeConnection(final Connection con) {
    if (con != null) {
      try {
        con.close();
        LOGGER.info("Closing DB connection");
      } catch (SQLException e) {
        LOGGER.error("Error while closing connection", e);
      }
    }
  }

  /**
  * Closing of the prepared statement.
  * @param ps
  *      Precompiled sql request
  */
  public void closePreparedStatement(final PreparedStatement ps) {
    if (ps != null) {
      try {
        ps.close();
        LOGGER.info("Closing Prepared Statement");
      } catch (SQLException e) {
        LOGGER.error("Error while closing prepared statement", e);
      }
    }
  }

  /**
  * Closing of the request's results.
  * @param rs
  *     results of preparedStatement
  */
  public void closeResultSet(final ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
        LOGGER.info("Closing Result Set");
      } catch (SQLException e) {
        LOGGER.error("Error while closing result set", e);
      }
    }
  }
}
