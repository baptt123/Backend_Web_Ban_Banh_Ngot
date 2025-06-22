package com.example.myappbackend.service.impl;

import com.example.myappbackend.model.Role;
import com.example.myappbackend.repository.RoleRepository;
import com.example.myappbackend.service.interfaceservice.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
