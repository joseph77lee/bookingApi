package com.joseph.app.rest.Service;

import com.joseph.app.rest.Models.Booking;
import com.joseph.app.rest.Repo.BookingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public class BookingService {
    @Autowired
    private BookingRepo bookingRepo;

    public boolean isConflictingBooking(Date startDate, Date endDate) {
        List<Booking> existingBookings = bookingRepo.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(endDate, startDate);
        return !existingBookings.isEmpty();
    }

    public boolean isDateValid(Date startDate, Date endDate, String timeZoneId) {
        ZoneId zoneId;
        try {
            zoneId = ZoneId.of(timeZoneId);
        } catch (DateTimeException e) {
            return false; // Invalid time zone ID
        }

        LocalDate now = LocalDate.now(zoneId);
        LocalDate start = convertToLocalDateViaInstant(startDate, zoneId);
        LocalDate end = convertToLocalDateViaInstant(endDate, zoneId);

        return !start.isBefore(now) && !end.isBefore(start) && ChronoUnit.DAYS.between(start, end) >= 1;
    }

    private LocalDate convertToLocalDateViaInstant(Date dateToConvert, ZoneId zoneId) {
        return dateToConvert.toInstant()
                .atZone(zoneId)
                .toLocalDate();
    }
}
