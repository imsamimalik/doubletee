package com.sda.doubleTee.service;

import com.sda.doubleTee.dto.AddAdminDto;
import com.sda.doubleTee.model.Admin;
import com.sda.doubleTee.model.Staff;
import com.sda.doubleTee.model.User;
import com.sda.doubleTee.repository.AdminRepository;
import com.sda.doubleTee.repository.StaffRepository;
import com.sda.doubleTee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Admin> fetchAll() {
        return adminRepository.findAll();
    }

    public Admin findById(Long id) {
        return adminRepository.findById(id).orElse(null);
    }

    public boolean saveAdmin(AddAdminDto addAdminDto) {
        Admin admin = new Admin();
        Staff staff = new Staff();
        Staff newStaff = staffRepository.save(staff);
        admin.setId(newStaff.getId());
        admin.setName(addAdminDto.getName());
        adminRepository.save(admin);
        return true;
    }

    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
        staffRepository.deleteById(id);
        User teacher = userRepository.findByEmployeeId(id);
        userRepository.deleteById(teacher.getId());
    }
}
