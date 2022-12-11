package com.github.shameworm.aqa_lab3.utils;

import com.github.shameworm.aqa_lab3.entity.Doctor;
import com.github.shameworm.aqa_lab3.entity.DoctorSchedule;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import org.assertj.core.api.Condition;

public class TestConditions {

  public static final Condition<Doctor> DOCTOR_NAME_IS_DENYS = new Condition<>(
      d -> d.getFirstName().equals("Denys"), "The name of Doctor is Denys"
  );

  public static final Condition<? super List<? extends Doctor>> DOCTORS_NAMES_ARE_JOSHUA
      = new Condition<>(doctors -> doctors.stream()
      .allMatch(d -> d.getFirstName().equals("Joshua")),
      "The names of Doctors are Joshua");

  public static final Condition<? super List<? extends DoctorSchedule>> DOCTOR_SCHEDULES_BY_DOCTOR_ID
      = new Condition<>(schedules -> schedules.stream()
      .allMatch(s -> s.getDoctorId() == 1),
      "Doctor schedules with doctor id = 1");

  public static final Condition<DoctorSchedule> SESSION_IS_SCHEDULED = new Condition<>(
      s -> !s.isFree()
          && s.getDoctorId() == 1L
          && s.getPatientId() == 1L
          && s.getTime().equals(LocalDateTime.of(2020, Month.AUGUST, 14, 10, 30)),
      "Session is scheduled for patient id = 1");

  public static final Condition<? super List<? extends DoctorSchedule>> DOCTOR_SCHEDULES_BY_DOCTOR_ID_AND_TIME_RANGE
      = new Condition<>(schedules -> schedules.stream()
      .allMatch(s -> s.getDoctorId() == 1
          && (s.getTime().isEqual(LocalDateTime.of(2020, Month.AUGUST, 14, 10, 30))
          || s.getTime().isAfter(LocalDateTime.of(2020, Month.AUGUST, 14, 10, 30)))
          && s.getTime().isBefore(LocalDateTime.of(2020, Month.AUGUST, 15, 10, 30))),
      "Doctor schedules with doctor id = 1 are in time range");

  public static final Condition<DoctorSchedule> SESSION_IS_DECLINED = new Condition<>(
      s -> s.isFree()
          && s.getScheduleId() == 7L
          && s.getPatientId() == 0L,
      "Session is scheduled for patient id = 1");

  public static final Condition<? super List<? extends DoctorSchedule>> SCHEDULES_FOR_PATIENT =
      new Condition<>(schedules -> schedules.stream().allMatch(s -> s.getPatientId() == 1L),
          "Schedules for patient id = 1");
}
