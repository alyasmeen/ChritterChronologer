package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    ScheduleService scheduleService;

    private ScheduleDTO convertScheduleToScheduleDTO(Schedule schedule) {
        List<Long> empIds = schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList());
        List<Long> pIds = schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList());
        return new ScheduleDTO(schedule.getId(), empIds, pIds, schedule.getDate(), schedule.getActivities());
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule s=new Schedule(scheduleDTO.getDate(), scheduleDTO.getActivities());
        return convertScheduleToScheduleDTO(scheduleService.save(s, scheduleDTO.getEmployeeIds(), scheduleDTO.getPetIds()));
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        return scheduleService.getAllSchedules().stream().map(this::convertScheduleToScheduleDTO).collect(Collectors.toList());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        try {
            return scheduleService.getSchedulesByPet(petId).stream().map(this::convertScheduleToScheduleDTO).collect(Collectors.toList());
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not found");
        }
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        try {
            return scheduleService.getSchedulesByEmployee(employeeId).stream().map(this::convertScheduleToScheduleDTO).collect(Collectors.toList());
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not found");
        }
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        try {
            return scheduleService.getSchedulesByCustomer(customerId).stream().map(this::convertScheduleToScheduleDTO).collect(Collectors.toList());
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not found");
        }
    }
}
