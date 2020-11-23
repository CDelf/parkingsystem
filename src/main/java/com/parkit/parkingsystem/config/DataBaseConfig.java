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

  private static final Logger logger = LogManager.getLogger("DataBaseConfig");
  
  /**
  * Configures database connection.
  */

  public Connection getConnection() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {
    logger.info("Create DB connection");
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
  */
  
  public void closeConnection(Connection con) {
    if (con != null) {
      try {
        con.close();
        logger.info("Closing DB connection");
      } catch (SQLException e) {
        logger.error("Error while closing connection", e);
      }
    }
  }

  /**
  * Closing of the prepared statement.
  */
  
  public void closePreparedStatement(PreparedStatement ps) {
    if (ps != null) {
      try {
        ps.close();
        logger.info("Closing Prepared Statement");
      } catch (SQLException e) {
        logger.error("Error while closing prepared statement", e);
      }
    }
  }

  /**
  * Closing of the request's results.
  */
  public void closeResultSet(ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
        logger.info("Closing Result Set");
      } catch (SQLException e) {
        logger.error("Error while closing result set", e);
      }
    }
  }
}
