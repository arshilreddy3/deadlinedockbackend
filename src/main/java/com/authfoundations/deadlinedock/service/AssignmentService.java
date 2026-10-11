package com.authfoundations.deadlinedock.service;

import com.authfoundations.deadlinedock.entity.Assignment;
import com.authfoundations.deadlinedock.repository.AssignmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;

    public AssignmentService(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    public Assignment addAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public Optional<Assignment> getAssignmentById(Long id) {
        return assignmentRepository.findById(id);
    }

    public Assignment updateAssignment(Long id, Assignment updatedAssignment) {

        Optional<Assignment> existingAssignment =
                assignmentRepository.findById(id);

        if (existingAssignment.isPresent()) {

            Assignment assignment = existingAssignment.get();

            assignment.setTitle(updatedAssignment.getTitle());
            assignment.setSubject(updatedAssignment.getSubject());
            assignment.setDueDate(updatedAssignment.getDueDate());
            assignment.setPriority(updatedAssignment.getPriority());
            assignment.setStatus(updatedAssignment.getStatus());

            return assignmentRepository.save(assignment);
        }

        return null;
    }

    public void deleteAssignment(Long id) {
        assignmentRepository.deleteById(id);
    }
}
