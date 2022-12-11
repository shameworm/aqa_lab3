package com.github.shameworm.aqa_lab3.api;

import com.github.shameworm.aqa_lab3.entity.DoctorSchedule;
import com.github.shameworm.aqa_lab3.exception.SessionWithDoctorScheduleException;
import com.github.shameworm.aqa_lab3.exception.UnknownScheduleException;
import com.github.shameworm.aqa_lab3.model.DoctorScheduleRequest;
import com.github.shameworm.aqa_lab3.repository.DoctorScheduleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/doctor-schedules")
public class DoctorScheduleController {

  private final DoctorScheduleRepository doctorScheduleRepository;

  @GetMapping("/{id}")
  public List<DoctorSchedule> fetchSchedulesByDoctorId(@PathVariable Long id) {
    return doctorScheduleRepository.findAllByDoctorId(id);
  }

  @PostMapping("/")
  public List<DoctorSchedule> fetchSchedulesByRequest(
      @RequestBody DoctorScheduleRequest scheduleRequest) {
    if (scheduleRequest.getDoctorId() == null) {
      throw new SessionWithDoctorScheduleException();
    }
    return doctorScheduleRepository.findAllByDoctorIdAndTimeBetween(scheduleRequest.getDoctorId(),
        scheduleRequest.getFrom(), scheduleRequest.getTo());
  }

  @PostMapping("/schedule")
  public DoctorSchedule scheduleSessionWithDoctor(
      @RequestBody DoctorScheduleRequest scheduleRequest) {
    DoctorSchedule schedule = doctorScheduleRepository.findFirstByDoctorIdAndTime(
        scheduleRequest.getDoctorId(),
        scheduleRequest.getSelectedTime());

    if (schedule != null && schedule.isFree()) {
      schedule.setPatientId(scheduleRequest.getPatientId());
      schedule.setFree(false);
      return doctorScheduleRepository.save(schedule);
    }

    throw new SessionWithDoctorScheduleException();
  }

  @PostMapping("/decline")
  public DoctorSchedule declineSessionWithDoctor(
      @RequestBody DoctorScheduleRequest scheduleRequest) {

    DoctorSchedule schedule = doctorScheduleRepository.findById(scheduleRequest.getScheduleId())
        .orElseThrow(UnknownScheduleException::new);

    schedule.setFree(true);
    schedule.setPatientId(0);

    return doctorScheduleRepository.save(schedule);
  }

}
