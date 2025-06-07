package com.example.myappbackend.service.interfaceservice;



import com.example.myappbackend.dto.DTO.ComplaintRequestDTO;
import com.example.myappbackend.dto.DTO.ComplaintResponseDTO;

import java.util.List;

public interface ComplaintService {
    ComplaintResponseDTO createComplaint(ComplaintRequestDTO request);
    ComplaintResponseDTO getComplaintById(Integer id);
    List<ComplaintResponseDTO> getAllComplaints();
    ComplaintResponseDTO updateComplaint(Integer id, ComplaintRequestDTO request);
    void deleteComplaint(Integer id);
}
