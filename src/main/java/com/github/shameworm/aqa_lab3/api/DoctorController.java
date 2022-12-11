package com.github.shameworm.aqa_lab3.api;

import com.github.shameworm.aqa_lab3.entity.Doctor;
import com.github.shameworm.aqa_lab3.model.DoctorRequest;
import com.github.shameworm.aqa_lab3.repository.DoctorRepository;
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
@RequestMapping("/doctors")
public class DoctorController {

  private final DoctorRepository doctorRepository;

  @GetMapping("/")
  public List<Doctor> fetchAllDoctors() {
    return doctorRepository.findAll();
  }

  @GetMapping("/{id}")
  public Doctor fetchDoctorById(@PathVariable Long id) {
    return doctorRepository.findById(id).orElse(null);
  }

  @PostMapping("/")
  public List<Doctor> fetchDoctorsByFirstName(@RequestBody DoctorRequest request) {
    if (Character.isLowerCase(request.getFirstName().codePointAt(0))) {
      throw new IllegalArgumentException("First Name cannot start with a lower case character!");
    }
    return doctorRepository.findAllByFirstName(request.getFirstName());
  }

}
