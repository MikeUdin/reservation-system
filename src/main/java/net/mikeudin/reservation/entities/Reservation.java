package net.mikeudin.reservation.entities;

import net.mikeudin.reservation.enums.ReservationStatus;

import java.time.LocalDate;

public record Reservation(

        Long id,
        String userId,
        Long roomId,
        LocalDate startDate,
        LocalDate endDate,
        ReservationStatus status

) {

}
