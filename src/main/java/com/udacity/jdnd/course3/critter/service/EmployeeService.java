package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository empRepo;


    public Employee getEmployeeById(Long empId){
        return empRepo.getOne(empId);
    }


    public List<Employee> getEmployeesBySkills(LocalDate date, Set<EmployeeSkill> skills){
        return empRepo
                .findByDaysAvailable(date.getDayOfWeek()).stream()
                .filter(employee-> employee.getSkills().containsAll(skills))
                .collect(Collectors.toList());
    }


    public Employee save(Employee emp){
        return empRepo.save(emp);
    }


    public void setAvailability(Set<DayOfWeek> days, Long empId){
        Employee emp=empRepo.getOne(empId);
        emp.setDaysAvailable(days);
        empRepo.save(emp);
    }
}
