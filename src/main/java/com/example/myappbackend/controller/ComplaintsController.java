package com.example.myappbackend.controller;


import com.example.myappbackend.dto.DTO.ComplaintRequestDTO;
import com.example.myappbackend.dto.DTO.ComplaintResponseDTO;
import com.example.myappbackend.service.interfaceservice.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
@RequiredArgsConstructor
public class ComplaintsController {

    private final ComplaintService complaintService;

    @PostMapping
    public ResponseEntity<ComplaintResponseDTO> create(@RequestBody ComplaintRequestDTO request) {
        return ResponseEntity.ok(complaintService.createComplaint(request));
    }

    @GetMapping
    public ResponseEntity<List<ComplaintResponseDTO>> getAll() {
        return ResponseEntity.ok(complaintService.getAllComplaints());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComplaintResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(complaintService.getComplaintById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComplaintResponseDTO> update(@PathVariable Integer id, @RequestBody ComplaintRequestDTO request) {
        return ResponseEntity.ok(complaintService.updateComplaint(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        complaintService.deleteComplaint(id);
        return ResponseEntity.ok().build();
    }
}
