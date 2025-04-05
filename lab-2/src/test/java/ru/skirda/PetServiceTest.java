package ru.skirda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.skirda.Dao.Models.PetDao;
import ru.skirda.Models.Pet;
import ru.skirda.Models.PetColor;

import ru.skirda.Services.PetService;

import static org.junit.Assert.*;

public class PetServiceTest extends TestContainersConfig {

    private PetService petService;

    @BeforeEach
    void init() {
        PetDao petDao = new PetDao(entityManager);
        petService = new PetService(petDao);
    }

    @Test
    void testCreatePet() {
        Pet pet = new Pet()
                .setName("Tralalero Tralala")
                .setColor(PetColor.BLACK);

        petService.save(pet);

        Pet foundPet = petService.getById(pet.getId());

        assertNotNull(foundPet);
        assertEquals("Tralalero Tralala", foundPet.getName());
    }

    @Test
    void testAddPetFriend() {
        Pet cat1 = petService.save(new Pet()
                .setName("Bombardiro Crocodilo")
                .setColor(PetColor.BLACK));
        Pet cat2 = petService.save(new Pet()
                .setName("Lirili Larila")
                .setColor(PetColor.ORANGE));

        petService.addFriend(cat1.getId(), cat2.getId());

        Pet foundCat1 = petService.getById(cat1.getId());
        System.out.println(foundCat1.getFriends());
        assertTrue(foundCat1.getFriends().stream()
                .anyMatch(f -> f.getId().equals(cat2.getId())));
    }

    @Test
    void testDeletePet() {
        Pet pet = petService.save(new Pet()
                .setName("Shimpansini Bananini")
                .setColor(PetColor.BLACK));

        petService.deleteById(pet.getId());

        assertNull(petService.getById(pet.getId()));
    }
}