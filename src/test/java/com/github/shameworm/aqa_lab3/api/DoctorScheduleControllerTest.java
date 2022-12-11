package com.github.shameworm.aqa_lab3.api;

import static com.github.shameworm.aqa_lab3.utils.TestConditions.DOCTOR_SCHEDULES_BY_DOCTOR_ID;
import static com.github.shameworm.aqa_lab3.utils.TestConditions.DOCTOR_SCHEDULES_BY_DOCTOR_ID_AND_TIME_RANGE;
import static com.github.shameworm.aqa_lab3.utils.TestConditions.SESSION_IS_DECLINED;
import static com.github.shameworm.aqa_lab3.utils.TestConditions.SESSION_IS_SCHEDULED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.shameworm.aqa_lab3.config.ReplaceCamelCase;
import com.github.shameworm.aqa_lab3.entity.DoctorSchedule;
import com.github.shameworm.aqa_lab3.exception.SessionWithDoctorScheduleException;
import com.github.shameworm.aqa_lab3.exception.UnknownScheduleException;
import com.github.shameworm.aqa_lab3.model.DoctorScheduleRequest;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(ReplaceCamelCase.class)
public class DoctorScheduleControllerTest {

  private static final String DOCTOR_SCHEDULE_PATH = "/doctor-schedules/";
  private static final String SCHEDULE_SESSION_WITH_DOCTOR_PATH = DOCTOR_SCHEDULE_PATH + "schedule";
  private static final String DECLINE_SESSION_WITH_DOCTOR_PATH = DOCTOR_SCHEDULE_PATH + "decline";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper mapper;

  @Test
  void shouldFetchDoctorSchedulesByDoctorId() throws Exception {
    // given
    Long doctorId = 1L;

    // when
    MvcResult mvcResult = performRequest(DOCTOR_SCHEDULE_PATH + doctorId);
    List<DoctorSchedule> doctorSchedules = mapper.readValue(
        mvcResult.getResponse().getContentAsString(),
        new TypeReference<>() {
        });

    // then
    assertThat(doctorSchedules)
        .isNotEmpty()
        .has(DOCTOR_SCHEDULES_BY_DOCTOR_ID);
  }

  @Test
  void shouldFetchEmptyDoctorSchedulesByUnknownDoctorId() throws Exception {
    // given
    Long unknownDoctorId = 999999L;

    // when
    MvcResult mvcResult = performRequest(DOCTOR_SCHEDULE_PATH + unknownDoctorId);
    List<DoctorSchedule> doctorSchedules = mapper.readValue(
        mvcResult.getResponse().getContentAsString(),
        new TypeReference<>() {
        });

    // then
    assertThat(doctorSchedules)
        .isEmpty();
  }

  @Test
  void shouldScheduleSessionForSpecificTimeWithDoctor() throws Exception {
    // given
    DoctorScheduleRequest scheduleRequest = DoctorScheduleRequest.builder()
        .doctorId(1L)
        .patientId(1L)
        .selectedTime(LocalDateTime.of(2020, Month.AUGUST, 14, 10, 30))
        .build();

    // when
    MvcResult mvcResult = performRequest(SCHEDULE_SESSION_WITH_DOCTOR_PATH, scheduleRequest);
    DoctorSchedule doctorSchedule = mapper.readValue(mvcResult.getResponse().getContentAsString(),
        new TypeReference<>() {
        });

    // then
    assertThat(doctorSchedule)
        .isNotNull()
        .hasNoNullFieldsOrProperties()
        .has(SESSION_IS_SCHEDULED);
  }

  @Test
  void shouldNotScheduleSessionForUnknownDoctor() throws Exception {
    // given
    DoctorScheduleRequest scheduleRequest = DoctorScheduleRequest.builder()
        .doctorId(99999L)
        .patientId(1L)
        .selectedTime(LocalDateTime.of(2020, Month.AUGUST, 14, 10, 30))
        .build();

    // when
    MvcResult mvcResult = performRequest(SCHEDULE_SESSION_WITH_DOCTOR_PATH, scheduleRequest);

    assertThat(mvcResult.getResolvedException())
        .isInstanceOf(SessionWithDoctorScheduleException.class);
  }

  @Test
  void shouldNotScheduleSessionWhenSelectedTimeIsNotFree() throws Exception {
    // given
    DoctorScheduleRequest scheduleRequest = DoctorScheduleRequest.builder()
        .doctorId(2L)
        .patientId(2L)
        .selectedTime(LocalDateTime.of(2020, Month.AUGUST, 14, 10, 30))
        .build();

    // when
    MvcResult mvcResult = performRequest(SCHEDULE_SESSION_WITH_DOCTOR_PATH, scheduleRequest);

    // then
    assertThat(mvcResult.getResolvedException())
        .isInstanceOf(SessionWithDoctorScheduleException.class);
  }

  @Test
  void shouldFetchDoctorSchedulesByDoctorIdForTimeRange() throws Exception {
    // given
    DoctorScheduleRequest scheduleRequest = DoctorScheduleRequest.builder()
        .doctorId(1L)
        .from(LocalDateTime.of(2020, Month.AUGUST, 14, 10, 30))
        .to(LocalDateTime.of(2020, Month.AUGUST, 15, 10, 30))
        .build();

    // when
    MvcResult mvcResult = performRequest(DOCTOR_SCHEDULE_PATH, scheduleRequest);
    List<DoctorSchedule> doctorSchedules = mapper.readValue(
        mvcResult.getResponse().getContentAsString(),
        new TypeReference<>() {
        });

    // then
    assertThat(doctorSchedules)
        .isNotEmpty()
        .hasSizeGreaterThan(1)
        .has(DOCTOR_SCHEDULES_BY_DOCTOR_ID_AND_TIME_RANGE);
  }

  @Test
  void shouldDeclineSessionWithDoctor() throws Exception {
    // given
    DoctorScheduleRequest scheduleRequest = DoctorScheduleRequest.builder()
        .scheduleId(7L)
        .build();

    // when
    MvcResult mvcResult = performRequest(DECLINE_SESSION_WITH_DOCTOR_PATH, scheduleRequest);
    DoctorSchedule doctorSchedule = mapper.readValue(
        mvcResult.getResponse().getContentAsString(),
        new TypeReference<>() {
        });

    // then
    assertThat(doctorSchedule)
        .isNotNull()
        .hasNoNullFieldsOrProperties()
        .has(SESSION_IS_DECLINED);
  }

  @Test
  void shouldNotDeclineSessionByUnknownScheduleId() throws Exception {
    // given
    DoctorScheduleRequest scheduleRequest = DoctorScheduleRequest.builder()
        .scheduleId(9999L)
        .build();

    // when
    MvcResult mvcResult = performRequest(DECLINE_SESSION_WITH_DOCTOR_PATH, scheduleRequest);

    // then
    assertThat(mvcResult.getResolvedException())
        .isInstanceOf(UnknownScheduleException.class);
  }

  private MvcResult performRequest(String path) throws Exception {
    return mockMvc.perform(get(path))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(200))
        .andReturn();
  }

  private MvcResult performRequest(String path, DoctorScheduleRequest request)
      throws Exception {
    return mockMvc.perform(post(path)
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andReturn();
  }
}
