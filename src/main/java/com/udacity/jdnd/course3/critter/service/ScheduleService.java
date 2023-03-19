package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ScheduleService {
    @Autowired
    PetRepository petRepo;
    @Autowired
    CustomerRepository customerRepo;
    @Autowired
    EmployeeRepository empRepo;
    @Autowired
    ScheduleRepository scheduleRepo;

    public List<Schedule> getSchedulesByPet(Long pId) {
        return scheduleRepo.findByPets(petRepo.getOne(pId));
    }

    public List<Schedule> getSchedulesByCustomer(Long cId) {
        return scheduleRepo.findByPetsIn(customerRepo.getOne(cId).getPets());
    }

    public List<Schedule> getSchedulesByEmployee(Long empId) {
        return scheduleRepo.findByEmployees(empRepo.getOne(empId));
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepo.findAll();
    }

    public Schedule save(Schedule s, List<Long> empIds, List<Long> pIds){
        s.setPets(petRepo.findAllById(pIds));
        s.setEmployees(empRepo.findAllById(empIds));
        return scheduleRepo.save(s);
    }
}
