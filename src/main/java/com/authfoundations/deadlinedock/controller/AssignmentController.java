package com.authfoundations.deadlinedock.controller;

import com.authfoundations.deadlinedock.entity.Assignment;
import com.authfoundations.deadlinedock.service.AssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@CrossOrigin(origins = "*")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    // Add a new assignment
    @PostMapping
    public Assignment addAssignment(@RequestBody Assignment assignment) {
        return assignmentService.addAssignment(assignment);
    }

    // Get all assignments
    @GetMapping
    public List<Assignment> getAllAssignments() {
        return assignmentService.getAllAssignments();
    }

    // Get assignment by ID
    @GetMapping("/{id}")
    public ResponseEntity<Assignment> getAssignmentById(@PathVariable Long id) {

        return assignmentService.getAssignmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update an assignment
    @PutMapping("/{id}")
    public ResponseEntity<Assignment> updateAssignment(
            @PathVariable Long id,
            @RequestBody Assignment assignment) {

        Assignment updatedAssignment =
                assignmentService.updateAssignment(id, assignment);

        if (updatedAssignment != null) {
            return ResponseEntity.ok(updatedAssignment);
        }

        return ResponseEntity.notFound().build();
    }

    // Delete an assignment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {

        assignmentService.deleteAssignment(id);

        return ResponseEntity.noContent().build();
    }
}
