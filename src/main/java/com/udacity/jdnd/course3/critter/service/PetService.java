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

@Transactional
@Service
public class PetService {
    @Autowired
    PetRepository petRepo;
    @Autowired
    CustomerRepository customerRepo;

    public Pet getPetById(Long pId) {
        return petRepo.getOne(pId);
    }
    public List<Pet> getAllPets() {
        return petRepo.findAll();
    }
    public List<Pet> getPetsByCustomerId(long cId) {
        return petRepo.findByCustomerId(cId);
    }

    public Pet save(Pet p, Long cId){
        Customer c=customerRepo.getOne(cId);
        p.setCustomer(c);
        p= petRepo.save(p);

        List<Pet> pets=new ArrayList<>();
        pets.add(p);
        c.setPets(pets);
        customerRepo.save(c);

        return p;
    }

}
