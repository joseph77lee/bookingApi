package com.joseph.app.rest.Repo;

import com.joseph.app.rest.Models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Long> {
    List<Booking> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(Date endDate, Date startDate);
}
