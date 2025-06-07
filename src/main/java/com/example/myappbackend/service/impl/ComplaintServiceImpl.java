package com.example.myappbackend.service.impl;


import com.example.myappbackend.dto.DTO.ComplaintRequestDTO;
import com.example.myappbackend.dto.DTO.ComplaintResponseDTO;
import com.example.myappbackend.exception.ResourceNotFoundException;
import com.example.myappbackend.model.Complaints;
import com.example.myappbackend.model.User;
import com.example.myappbackend.repository.ComplaintRepository;
import com.example.myappbackend.repository.UserRepository;
import com.example.myappbackend.service.interfaceservice.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;

    @Override
    public ComplaintResponseDTO createComplaint(ComplaintRequestDTO request) {
        User customer = userRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Complaints complaint = new Complaints();
        complaint.setCustomer(customer);
        complaint.setComplaintText(request.getComplaintText());
        complaint.setAdminResponse(request.getAdminResponse());
        complaint.setCreatedAt(LocalDateTime.now());
        complaint.setUpdatedAt(LocalDateTime.now());

        Complaints saved = complaintRepository.save(complaint);
        return mapToDTO(saved);
    }

    @Override
    public ComplaintResponseDTO getComplaintById(Integer id) {
        Complaints complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with ID " + id));
        return mapToDTO(complaint);
    }

    @Override
    public List<ComplaintResponseDTO> getAllComplaints() {
        return complaintRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ComplaintResponseDTO updateComplaint(Integer id, ComplaintRequestDTO request) {
        Complaints complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with ID " + id));

        complaint.setComplaintText(request.getComplaintText());
        complaint.setAdminResponse(request.getAdminResponse());
        complaint.setUpdatedAt(LocalDateTime.now());

        return mapToDTO(complaintRepository.save(complaint));
    }

    @Override
    public void deleteComplaint(Integer id) {
        Complaints complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with ID " + id));
        complaintRepository.delete(complaint);
    }

    private ComplaintResponseDTO mapToDTO(Complaints complaint) {
        ComplaintResponseDTO dto = new ComplaintResponseDTO();
        dto.setId(complaint.getId());
        dto.setCustomerUsername(complaint.getCustomer().getUsername());
        dto.setComplaintText(complaint.getComplaintText());
        dto.setAdminResponse(complaint.getAdminResponse());
        dto.setCreatedAt(complaint.getCreatedAt());
        dto.setUpdatedAt(complaint.getUpdatedAt());
        return dto;
    }
}
