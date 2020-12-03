package com.parkit.parkingsystem.integration.config;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DataBaseConstants;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jacoco.core.tools.ExecDumpClient;
import org.junit.jupiter.api.Test;


public class DataBaseConfigTest {

  private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
  private static DataBaseConfig dataBaseConfig = new DataBaseConfig();

  @Test
  public void closeNullMainConnectionTest() {
    Connection testCon = null;
    dataBaseConfig.closeConnection(testCon);
  
    assertThatExceptionOfType(SQLException.class);
  }

  @Test
  public void closeNullTestConnectionTest() {
    Connection testCon = null;
    dataBaseTestConfig.closeConnection(testCon);

    assertThatExceptionOfType(SQLException.class);
  }

  @Test 
  public void closeNullMainPreparedStatementTest() throws Exception {
    Connection testCon = dataBaseConfig.getConnection();
    PreparedStatement ps = null;
    dataBaseConfig.closePreparedStatement(ps);
    dataBaseConfig.closeConnection(testCon);

    assertThatExceptionOfType(SQLException.class);
  }

  @Test 
  public void closeNullTestPreparedStatementTest() throws Exception {
    Connection testCon = dataBaseTestConfig.getConnection();
    PreparedStatement ps = null;
    dataBaseTestConfig.closePreparedStatement(ps);
    dataBaseTestConfig.closeConnection(testCon);

    assertThatExceptionOfType(SQLException.class);
  }

  @Test 
  public void closeMainNullResultSetTest() throws Exception {
    Connection testCon = dataBaseConfig.getConnection();
    PreparedStatement ps = testCon.prepareStatement(DataBaseConstants.NULL_RESULTS);
    ResultSet rs = null;
    dataBaseConfig.closeResultSet(rs);
    dataBaseConfig.closePreparedStatement(ps);
    dataBaseConfig.closeConnection(testCon);

    assertThatExceptionOfType(SQLException.class);
  }

  @Test 
  public void closeTestNullResultSetTest() throws Exception {
    Connection testCon = dataBaseTestConfig.getConnection();
    PreparedStatement ps = testCon.prepareStatement(DataBaseConstants.NULL_RESULTS);
    ResultSet rs = null;
    dataBaseTestConfig.closeResultSet(rs);
    dataBaseTestConfig.closePreparedStatement(ps);
    dataBaseTestConfig.closeConnection(testCon);

    assertThatExceptionOfType(SQLException.class);
  }
  
  @Test 
  public void closeMainResultSetNullResultsTest() throws Exception {
    Connection testCon = dataBaseConfig.getConnection();
    PreparedStatement ps = testCon.prepareStatement(DataBaseConstants.NULL_RESULTS);
    ResultSet rs = ps.executeQuery();
    dataBaseConfig.closeResultSet(rs);
    dataBaseConfig.closePreparedStatement(ps);
    dataBaseConfig.closeConnection(testCon);

    assertThatExceptionOfType(SQLException.class);
  }

  @Test 
  public void closeTestResultSetNullResultsTest() throws Exception {
    Connection testCon = dataBaseTestConfig.getConnection();
    PreparedStatement ps = testCon.prepareStatement(DataBaseConstants.NULL_RESULTS);
    ResultSet rs = ps.executeQuery();
    dataBaseTestConfig.closeResultSet(rs);
    dataBaseTestConfig.closePreparedStatement(ps);
    dataBaseTestConfig.closeConnection(testCon);

    assertThatExceptionOfType(SQLException.class);
  }

}
