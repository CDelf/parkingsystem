package com.parkit.parkingsystem.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){

    }

    @Test
	public void testParkingACar() throws Exception {
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
		// GIVEN
		Ticket ticket = null;
		ParkingSpot parkingSpotTest = new ParkingSpot(1, ParkingType.CAR, false);
		// WHEN
		Connection testCon = dataBaseTestConfig.getConnection();
		PreparedStatement ps = testCon.prepareStatement(DBConstants.IN_TEST_TICKET);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			ticket = new Ticket();
			ticket.setParkingSpot(parkingSpotTest);
		}
		// THEN
		assertThat(rs.getInt(4)).isNotEqualTo(0); // check ticket's ID is actually saved in DB
		assertThat(parkingSpotTest.isAvailable()).isEqualTo(rs.getBoolean(3)); // check if availability of the parking
																				// spot it updated
		dataBaseTestConfig.closeResultSet(rs);
		dataBaseTestConfig.closePreparedStatement(ps);
	}

    @Test
	public void testParkingLotExit() throws Exception {
        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();
		// GIVEN
		Ticket ticket = null;
		// WHEN
		Connection testCon = dataBaseTestConfig.getConnection();
		PreparedStatement ps = testCon.prepareStatement(DBConstants.OUT_TEST_TICKET);
		ResultSet rs = ps.executeQuery();

		 if(rs.next()){
        	 ticket = new Ticket();
        	 ticket.setPrice(rs.getDouble(1));
			 ticket.setOutTime(rs.getTimestamp(2));
        }

		// THEN
		assertThat(ticket.getPrice()).isEqualTo(rs.getDouble(1));
		assertThat(ticket.getOutTime()).isEqualTo(rs.getTimestamp(2));
		dataBaseTestConfig.closeResultSet(rs);
		dataBaseTestConfig.closePreparedStatement(ps);
    }
}

