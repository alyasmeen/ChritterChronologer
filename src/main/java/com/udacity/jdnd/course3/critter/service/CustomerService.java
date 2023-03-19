package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class CustomerService {
    @Autowired
    PetRepository petRepo;
    @Autowired
    CustomerRepository customerRepo;

    public Customer getCustomerByPetId(Long pId){
        return petRepo.getOne(pId).getCustomer();
    }
    public List<Customer> getAllCustomers(){
        return customerRepo.findAll();
    }
    public Customer save(Customer c,List<Long> pIds){
        List<Pet> pets = new ArrayList<>();
        if (pIds!=null && !pIds.isEmpty()){
            pets= pIds.stream().map((petId)-> petRepo.getOne(petId)).collect(Collectors.toList());
        }

        c.setPets(pets);
        return customerRepo.save(c);
    }
}
