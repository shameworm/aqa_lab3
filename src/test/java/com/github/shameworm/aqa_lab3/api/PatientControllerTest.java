package com.github.shameworm.aqa_lab3.api;

import static com.github.shameworm.aqa_lab3.utils.TestConditions.SCHEDULES_FOR_PATIENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.shameworm.aqa_lab3.config.ReplaceCamelCase;
import com.github.shameworm.aqa_lab3.entity.DoctorSchedule;
import com.github.shameworm.aqa_lab3.entity.Patient;
import com.github.shameworm.aqa_lab3.model.DoctorRequest;
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
public class PatientControllerTest {

  private static final String PATIENTS_PATH = "/patients/";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper mapper;

  @Test
  void shouldFetchSchedulesForSpecificPatient() throws Exception {
    // given
    Long patientId = 1l;

    // when
    MvcResult mvcResult = performRequest(PATIENTS_PATH + patientId + "/schedules");
    List<DoctorSchedule> schedules = mapper.readValue(mvcResult.getResponse().getContentAsString(),
        new TypeReference<>() {
        });

    // then
    assertThat(schedules)
        .isNotEmpty()
        .has(SCHEDULES_FOR_PATIENT);
  }

  @Test
  void shouldFetchEmptySchedulesForUnknownPatient() throws Exception {
    // given
    Long unknownPatientId = 999999l;

    // when
    MvcResult mvcResult = performRequest(PATIENTS_PATH + unknownPatientId + "/schedules");
    List<DoctorSchedule> schedules = mapper.readValue(mvcResult.getResponse().getContentAsString(),
        new TypeReference<>() {
        });

    // then
    assertThat(schedules).isEmpty();
  }

  @Test
  void shouldFetchAllPatients() throws Exception {
    // when
    MvcResult mvcResult = performRequest(PATIENTS_PATH);
    List<Patient> patients = mapper.readValue(mvcResult.getResponse().getContentAsString(),
        new TypeReference<>() {
        });

    // then
    assertThat(patients)
        .isNotEmpty()
        .hasSizeGreaterThan(1);
  }

  private MvcResult performRequest(String path) throws Exception {
    return mockMvc.perform(get(path))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(200))
        .andReturn();
  }

  private MvcResult performRequest(String path, DoctorRequest request)
      throws Exception {
    return mockMvc.perform(post(path)
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andReturn();
  }
}
