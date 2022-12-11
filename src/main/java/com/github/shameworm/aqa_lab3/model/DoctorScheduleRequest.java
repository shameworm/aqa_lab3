package com.github.shameworm.aqa_lab3.model;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Range;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorScheduleRequest {

  private Long scheduleId;
  private Long doctorId;
  private LocalDateTime selectedTime;
  private Long patientId;
  private LocalDateTime from;
  private LocalDateTime to;
}
