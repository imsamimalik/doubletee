package com.sda.doubleTee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sda.doubleTee.model.Staff;
import com.sda.doubleTee.repository.StaffRepository;


@Service
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;

    public Staff findById(Long id) {
        return staffRepository.findById(id).orElse(null);
    }


}
