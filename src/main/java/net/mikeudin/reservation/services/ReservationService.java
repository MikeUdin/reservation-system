package net.mikeudin.reservation.services;

import net.mikeudin.reservation.entities.Reservation;
import net.mikeudin.reservation.enums.ReservationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReservationService {

    private static final Logger log = LoggerFactory.getLogger(ReservationService.class);
    private final Map<Long, Reservation> reservations;
    private AtomicLong idCounter;

    public ReservationService() {
        this.reservations = new HashMap<>();
        this.idCounter = new AtomicLong();
    }


    public Reservation getReservationById( Long id) {
        if (reservations.containsKey(id)) {
            return reservations.get(id);
        } else {
            throw new NoSuchElementException("Reservation with id " + id + " not found");
        }
    }

    public List<Reservation> getAllReservations() {
        return reservations.values().stream().toList();
    }

    public Reservation createReservation(Reservation reservationToCreate) {

        if (reservationToCreate.userId() == null || reservationToCreate.roomId() == null
                        || reservationToCreate.startDate() == null || reservationToCreate.endDate() == null) {
                    throw new IllegalArgumentException("All fields except id and status must be present");
                }

        Reservation newReservation = new Reservation(
                idCounter.incrementAndGet(),
                reservationToCreate.userId(),
                reservationToCreate.roomId(),
                reservationToCreate.startDate(),
                reservationToCreate.endDate(),
                ReservationStatus.PENDING
        );
        reservations.put(newReservation.id(), newReservation);
        log.info("Created new reservation: {}", newReservation);
        return newReservation;

    }

    public Reservation updateReservation(Long id, Reservation reservationToUpdate) {
        Reservation oldReservation = getReservationById(id);
        Reservation updatedReservation = new Reservation(
                oldReservation.id(),
                reservationToUpdate.userId() != null ? reservationToUpdate.userId() : oldReservation.userId(),
                reservationToUpdate.roomId() != null ? reservationToUpdate.roomId() : oldReservation.roomId(),
                reservationToUpdate.startDate() != null ? reservationToUpdate.startDate() : oldReservation.startDate(),
                reservationToUpdate.endDate() != null ? reservationToUpdate.endDate() : oldReservation.endDate(),
                reservationToUpdate.status() != null ? reservationToUpdate.status() : oldReservation.status()
        );
        reservations.put(id, updatedReservation);
        return updatedReservation;
    }

    public void deleteReservation(Long id) {
        reservations.remove(id);
    }
}
