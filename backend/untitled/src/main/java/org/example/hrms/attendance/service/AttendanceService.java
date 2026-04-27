package org.example.hrms.attendance.service;

import lombok.RequiredArgsConstructor;
import org.example.hrms.attendance.entity.Attendance;
import org.example.hrms.attendance.repository.AttendanceRepository;
import org.example.hrms.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    // ✅ CHECK IN
    public Attendance checkIn(User user) {

        LocalDate today = LocalDate.now();

        if (attendanceRepository.findByEmployeeAndDate(user, today).isPresent()) {
            throw new RuntimeException("Already checked in today");
        }

        Attendance attendance = Attendance.builder()
                .employee(user)
                .date(today)
                .checkIn(LocalTime.now())
                .status("PRESENT")
                .build();

        return attendanceRepository.save(attendance);
    }

    // ✅ CHECK OUT
    public Attendance checkOut(User user) {

        LocalDate today = LocalDate.now();

        Attendance attendance = attendanceRepository
                .findByEmployeeAndDate(user, today)
                .orElseThrow(() -> new RuntimeException("Check-in not found"));

        attendance.setCheckOut(LocalTime.now());

        return attendanceRepository.save(attendance);
    }

    // ✅ GET MY ATTENDANCE
    public List<Attendance> getMyAttendance(User user) {
        return attendanceRepository.findByEmployee(user);
    }
}