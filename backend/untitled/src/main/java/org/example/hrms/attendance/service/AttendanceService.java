package org.example.hrms.attendance.service;

import lombok.RequiredArgsConstructor;
import org.example.hrms.attendance.dto.AttendanceResponseDTO;
import org.example.hrms.attendance.entity.Attendance;
import org.example.hrms.attendance.enums.AttendanceStatus;
import org.example.hrms.attendance.repository.AttendanceRepository;
import org.example.hrms.exception.ConflictException;
import org.example.hrms.exception.ResourceNotFoundException;
import org.example.hrms.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    // ✅ CHECK IN
    @Transactional
    public AttendanceResponseDTO checkIn(User user) {

        LocalDate today = LocalDate.now();

        if (attendanceRepository.findByEmployeeAndDate(user, today).isPresent()) {
            throw new ConflictException("Already checked in today");
        }

        Attendance attendance = Attendance.builder()
                .employee(user)
                .date(today)
                .checkIn(LocalTime.now())
                .status(AttendanceStatus.PRESENT)
                .build();

        return toResponseDTO(attendanceRepository.save(attendance));
    }

    // ✅ CHECK OUT
    @Transactional
    public AttendanceResponseDTO checkOut(User user) {

        LocalDate today = LocalDate.now();

        Attendance attendance = attendanceRepository
                .findByEmployeeAndDate(user, today)
                .orElseThrow(() -> new ResourceNotFoundException("Check-in not found"));

        if (attendance.getCheckOut() != null) {
            throw new ConflictException("Already checked out today");
        }

        attendance.setCheckOut(LocalTime.now());

        return toResponseDTO(attendanceRepository.save(attendance));
    }

    // ✅ GET MY ATTENDANCE
    public List<AttendanceResponseDTO> getMyAttendance(User user) {
        return attendanceRepository.findByEmployee(user).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private AttendanceResponseDTO toResponseDTO(Attendance attendance) {
        return AttendanceResponseDTO.builder()
                .id(attendance.getId())
                .date(attendance.getDate())
                .checkIn(attendance.getCheckIn())
                .checkOut(attendance.getCheckOut())
                .status(attendance.getStatus() != null ? attendance.getStatus().name() : null)
                .build();
    }
}
