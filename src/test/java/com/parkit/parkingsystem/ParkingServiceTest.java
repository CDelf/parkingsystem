package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;



@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

  private static ParkingService parkingService;
  
  private static final Logger LOGGER = LogManager.getLogger(ParkingService.class);

  @Mock
  private static InputReaderUtil inputReaderUtil;
  @Mock
  private static ParkingSpotDAO parkingSpotDAO;
  @Mock
  private static TicketDAO ticketDAO;

  @BeforeEach
  private void setUpPerTest() {
    try {
      parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Failed to set up test mock object");
    }
  }

  @Test
  public void processIncomingCarTest() throws Exception {
    Ticket ticket = new Ticket();
    ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
    ticket.setVehicleRegNumber("ABCDEF");
    ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));

    when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
    when(inputReaderUtil.readSelection()).thenReturn(1);
    when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);
    when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
    when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
    
    parkingService.processIncomingVehicle();
    verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
    verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
  }

  @Test
  public void processIncomingBikeTest() throws Exception {
    Ticket ticket = new Ticket();
    ticket.setParkingSpot(new ParkingSpot(1, ParkingType.BIKE, false));
    ticket.setVehicleRegNumber("ABCDEF");
    ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));

    when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
    when(inputReaderUtil.readSelection()).thenReturn(2);
    when(parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE)).thenReturn(1);
    when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
    when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);

    parkingService.processIncomingVehicle();
    verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
    verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
  }
  
  @Test
  public void processIncomingUnknownVehicleTest() throws Exception {
    Ticket ticket = new Ticket();
    ticket.setParkingSpot(new ParkingSpot(1, ParkingType.TEST, false));

    when(inputReaderUtil.readSelection()).thenReturn(1);
    when(inputReaderUtil.readSelection()).thenReturn(3);

    parkingService.processIncomingVehicle();
    assertThrows(IllegalArgumentException.class, () 
        -> parkingService.getVehichleType());
  }

  @Test
  public void processIncomingReccuringUserTest() throws Exception {
    Ticket ticket = new Ticket();
    ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
    ticket.setVehicleRegNumber("ABCDEF");

    when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
    when(inputReaderUtil.readSelection()).thenReturn(1);
    when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);
    when(ticketDAO.isRecurringUser(anyString())).thenReturn(true);

    parkingService.processIncomingVehicle();
    verify(ticketDAO, Mockito.times(1)).isRecurringUser(anyString());
  }

  @Test
  public void processIncomingCar_NegativeParkingNumberTest() throws Exception {
    Ticket ticket = new Ticket();
    ticket.setParkingSpot(new ParkingSpot(-1, ParkingType.CAR, false));
    when(inputReaderUtil.readSelection()).thenReturn(1);
    
    parkingService.processIncomingVehicle();
    assertThatIllegalArgumentException();
  }

  @Test
  public void processExitingCarTest() throws Exception {
    when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
    Ticket ticket = new Ticket();
    ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
    ticket.setParkingSpot(parkingSpot);
    when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
    when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
    when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

    parkingService.processExitingVehicle();
    verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    verify(ticketDAO, Mockito.times(1)).updateTicket(any(Ticket.class));
    verify(ticketDAO, Mockito.times(1)).getTicket(anyString());
  }

  @Test
  public void processExitingBikeTest() throws Exception {
    when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
    Ticket ticket = new Ticket();
    ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
    ticket.setParkingSpot(parkingSpot);
    when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
    when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
    when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

    parkingService.processExitingVehicle();
    verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    verify(ticketDAO, Mockito.times(1)).updateTicket(any(Ticket.class));
    verify(ticketDAO, Mockito.times(1)).getTicket(anyString());
  }

  @Test
  public void processExitingVehicleTestWithoutDAO() {
    parkingService.processExitingVehicle();
    verify(parkingSpotDAO, Mockito.times(0)).updateParking(any(ParkingSpot.class));
    verify(ticketDAO, Mockito.times(0)).updateTicket(any(Ticket.class));
    verify(ticketDAO, Mockito.times(0)).getTicket(anyString());
    assertThatNullPointerException();
  }
}