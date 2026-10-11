package com.authfoundations.deadlinedock.repository;

import com.authfoundations.deadlinedock.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

}
