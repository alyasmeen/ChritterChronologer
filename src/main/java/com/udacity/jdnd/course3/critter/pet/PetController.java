package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    PetService petService;

    private PetDTO convertPetToPetDTO(Pet p) {
        return new PetDTO(p.getId(), p.getType(), p.getName(), p.getCustomer().getId(), p.getBirthDate(), p.getNotes());
    }
    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet p= new Pet(petDTO.getType(), petDTO.getName(), petDTO.getBirthDate(), petDTO.getNotes());
        return convertPetToPetDTO(petService.save(p, petDTO.getOwnerId()));
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        try {
            return convertPetToPetDTO(petService.getPetById(petId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pet not found");
        }
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<PetDTO> petDTOs= new ArrayList<>();
        List<Pet> pets= petService.getAllPets();
        for (Pet p: pets){ petDTOs.add(convertPetToPetDTO(p)); }
        return petDTOs;
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        try {
            List<PetDTO> petDTOs= new ArrayList<>();
            List<Pet> pets= petService.getPetsByCustomerId(ownerId);
            for (Pet p: pets){ petDTOs.add(convertPetToPetDTO(p)); }
            return petDTOs;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not found");
        }
    }
}
