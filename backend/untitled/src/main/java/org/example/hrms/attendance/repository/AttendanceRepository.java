package org.example.hrms.attendance.repository;

import org.example.hrms.attendance.entity.Attendance;
import org.example.hrms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByEmployeeAndDate(User employee, LocalDate date);

    List<Attendance> findByEmployee(User employee);
}