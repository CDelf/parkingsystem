package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket, boolean isDiscount) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		// DONE: Tests were failing because duration was calculated from integer hours,
		// so it didn't work for decimal duration or duration > 1 day
		double inHour = ticket.getInTime().getTime();
		double outHour = ticket.getOutTime().getTime();

		double duration = (outHour - inHour) / (60 * 60 * 1000);

		// US1 : if duration < 0.5 then parking is free
		double price = 0;

		if (duration > 0.5) {

			switch (ticket.getParkingSpot().getParkingType()) {
			case CAR: {
				price = duration * Fare.CAR_RATE_PER_HOUR;
				break;
			}
			case BIKE: {
				price = duration * Fare.BIKE_RATE_PER_HOUR;
				break;
			}
			default:
				throw new IllegalArgumentException("Unkown Parking Type");
			}
		}
		// US2: if isDiscount is true then user is recurring and benefits a 5% discount
		if (isDiscount) {
			price -= (price * 5) / 100;
		}
		ticket.setPrice(price);
	}
}