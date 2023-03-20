package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    CustomerService customerService;
    @Autowired
    EmployeeService employeeService;

    private CustomerDTO convertCustomerToCustomerDTO(Customer c){
        List<Long> pIds = c.getPets().stream().map(Pet::getId).collect(Collectors.toList());
        return new CustomerDTO(c.getId(), c.getName(), c.getPhoneNumber(), c.getNotes(), pIds);
    }

    private EmployeeDTO convertEmployeeToEmployeeDTO(Employee employee) {
        return new EmployeeDTO(employee.getId(), employee.getName(), employee.getSkills(), employee.getDaysAvailable());
    }

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer c = new Customer(customerDTO.getId(), customerDTO.getName(), customerDTO.getPhoneNumber(), customerDTO.getNotes());
        return convertCustomerToCustomerDTO(customerService.save(c, customerDTO.getPetIds()));
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<CustomerDTO> customerDTOs= new ArrayList<>();
        List<Customer> customers= customerService.getAllCustomers();
        for (Customer c: customers){ customerDTOs.add(convertCustomerToCustomerDTO(c)); }
        return customerDTOs;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        try {
            return convertCustomerToCustomerDTO(customerService.getCustomerByPetId(petId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not found");
        }
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee emp = new Employee(employeeDTO.getId(), employeeDTO.getName(), employeeDTO.getSkills(), employeeDTO.getDaysAvailable());
        return convertEmployeeToEmployeeDTO(employeeService.save(emp));
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        try {
            return convertEmployeeToEmployeeDTO(employeeService.getEmployeeById(employeeId));
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee not found");
        }
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        try {
            employeeService.setAvailability(daysAvailable, employeeId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee not found");
        }
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<EmployeeDTO> employeeDTOs= new ArrayList<>();
        List<Employee> employees = employeeService.getEmployeesBySkills(employeeDTO.getDate(), employeeDTO.getSkills());
        for (Employee emp: employees){ employeeDTOs.add(convertEmployeeToEmployeeDTO(emp)); }
        return employeeDTOs;
    }

}
