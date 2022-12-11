package com.github.shameworm.aqa_lab3.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "doctor_schedule")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorSchedule {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "schedule_id")
  private long scheduleId;

  @Column(name = "doctor_id")
  private long doctorId;

  @Column(name = "patient_id")
  private long patientId;

  @Column(name = "time")
  private LocalDateTime time;

  @Builder.Default
  @Column(name = "is_free")
  private boolean free = true;
}
