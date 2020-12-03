package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
class InputReaderUtilTest {

	InputReaderUtil inputReaderUtil = new InputReaderUtil();

	@Test
	 public void readSelectionTest() {
    final String inputString = "1";		
		Scanner scan = new Scanner(inputString);
		inputReaderUtil.getInput(scan);
		
    assertThat(inputReaderUtil.readSelection()).isEqualTo(1);
   }
	
	@Test
	public void readSelection_incorrectInputTest() {
	  final String inputString = "a";
		Scanner scan = new Scanner(inputString);
		inputReaderUtil.getInput(scan);
		
    assertThat(inputReaderUtil.readSelection()).isEqualTo(-1);
}
	
	@Test
	public void readVehicleRegistrationNumberTest() throws Exception {
	  final String vehicleRegNumber = "12345";
			Scanner scan = new Scanner(vehicleRegNumber);
			inputReaderUtil.getInput(scan);
		
		assertThat(inputReaderUtil.readVehicleRegistrationNumber()).isEqualTo(vehicleRegNumber);
}
	
	@Test
	public void readEmptyVehicleRegistrationNumberTest() throws Exception {
	  final String vehicleRegNumber = " ";
			Scanner scan = new Scanner(vehicleRegNumber);
			inputReaderUtil.getInput(scan);
		
			assertThrows(IllegalArgumentException.class, () -> inputReaderUtil.readVehicleRegistrationNumber());
}
	
	}

