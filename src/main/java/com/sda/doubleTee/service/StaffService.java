package com.sda.doubleTee.service;

import com.sda.doubleTee.dto.AddAdminDto;
import com.sda.doubleTee.model.Admin;
import com.sda.doubleTee.model.Staff;
import com.sda.doubleTee.repository.AdminRepository;
import com.sda.doubleTee.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;

    public Staff findById(Long id) {
        return staffRepository.findById(id).orElse(null);
    }


}
