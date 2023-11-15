package com.joseph.app.rest.Controller;

import com.joseph.app.rest.Models.Booking;
import com.joseph.app.rest.Repo.BookingRepo;
import com.joseph.app.rest.Service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingRepo.findAll();
    }

    @PostMapping
    public ResponseEntity<?> addBooking(@RequestBody Booking booking) {
        if (!bookingService.isDateValid(booking.getStartDate(), booking.getEndDate(), booking.getTimeZone())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid booking dates or time zone.");
        }

        if (bookingService.isConflictingBooking(booking.getStartDate(), booking.getEndDate())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Booking dates conflict with an existing booking.");
        }
        Booking createdBooking = bookingRepo.save(booking);
        return ResponseEntity.ok(createdBooking);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking bookingDetails) {
        Optional<Booking> bookingData = bookingRepo.findById(id);

        if (bookingData.isPresent()) {
            Booking booking = bookingData.get();
            booking.setStartDate(bookingDetails.getStartDate());
            booking.setEndDate(bookingDetails.getEndDate());
            booking.setCustomerName(bookingDetails.getCustomerName());
            booking.setCustomerAddress(bookingDetails.getCustomerAddress());
            booking.setCost(bookingDetails.getCost());
            booking.setPaymentType(bookingDetails.getPaymentType());
            booking.setCreationDate(bookingDetails.getCreationDate());
            final Booking updatedBooking = bookingRepo.save(booking);
            return ResponseEntity.ok(updatedBooking);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable Long id) {
        bookingRepo.deleteById(id);
    }
}
