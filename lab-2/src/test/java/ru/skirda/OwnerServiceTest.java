package ru.skirda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.skirda.Dao.Models.OwnerDao;
import ru.skirda.Dao.Models.PetDao;
import ru.skirda.Models.Owner;
import ru.skirda.Models.Pet;
import ru.skirda.Services.OwnerService;
import ru.skirda.Services.PetService;

import java.time.LocalDate;

import static org.junit.Assert.*;

class OwnerServiceTest extends TestContainersConfig {

    private OwnerService ownerService;
    private PetService petService;

    @BeforeEach
    void init() {
        OwnerDao ownerDao = new OwnerDao(entityManager);
        ownerService = new OwnerService(ownerDao);

        PetDao petDao = new PetDao(entityManager);
        petService = new PetService(petDao);
    }

    @Test
    void testCreateOwner() {
        Owner owner = new Owner()
                .setName("Brrr Brrr Patapim")
                .setBirthDate(LocalDate.of(2020, 10, 10));

        ownerService.save(owner);

        Owner foundOwner = ownerService.getById(owner.getId());
        assertNotNull(foundOwner);
        assertEquals("Brrr Brrr Patapim", foundOwner.getName());
    }

    @Test
    void testAddPetToOwner() {
        Owner owner = ownerService.save(new Owner()
                .setName("Tung Tung Tung Sahur")
                .setBirthDate(LocalDate.of(2020, 10, 10)));

        Pet pet = petService.save(new Pet()
                .setName("Tripi Tropa")
                .setBirthDate(LocalDate.of(2020, 10, 10)));

        ownerService.addPetToOwner(owner.getId(), pet);

        assertEquals("Tung Tung Tung Sahur", pet.getOwner().getName());
    }

    @Test
    void testDeleteOwner() {
        Owner owner = ownerService.save(new Owner()
                .setName("Shimpansini Bananini")
                .setBirthDate(LocalDate.of(2020, 10, 10)));

        ownerService.deleteById(owner.getId());

        assertNull(ownerService.getById(owner.getId()));
    }
}