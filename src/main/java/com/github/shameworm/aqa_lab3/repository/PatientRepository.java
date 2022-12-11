package com.github.shameworm.aqa_lab3.repository;

import com.github.shameworm.aqa_lab3.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

}
