package ru.skirda;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import ru.skirda.Dao.Models.OwnerDao;
import ru.skirda.Dao.Models.PetDao;
import ru.skirda.Models.Owner;
import ru.skirda.Models.Pet;
import ru.skirda.Models.PetColor;
import ru.skirda.Services.OwnerService;
import ru.skirda.Services.PetService;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.setProperty("DB_URL", "jdbc:postgresql://localhost:5432/lab2");
        System.setProperty("DB_USER", "postgres");
        System.setProperty("DB_PASS", "postgres");

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("lab2-persistence-unit");
        EntityManager em = entityManagerFactory.createEntityManager();

        em.getTransaction().begin();
        em.createQuery("delete from Pet").executeUpdate();
        em.createQuery("delete from Owner").executeUpdate();
        em.getTransaction().commit();

        PetDao petDao = new PetDao(em);
        PetService petService = new PetService(petDao);

        OwnerDao ownerDao = new OwnerDao(em);
        OwnerService ownerService = new OwnerService(ownerDao);

        Owner owner = new Owner("Peter Parker", LocalDate.of(1990, 1, 1));
        ownerService.save(owner);

        Pet pet1 = petService.save(new Pet()
                .setName("Tralalero Tralala")
                .setColor(PetColor.BLACK));

        Pet pet2 = petService.save(new Pet()
                .setName("Bombardiro Crocodilo")
                .setColor(PetColor.BLACK));

        ownerService.addPetToOwner(owner.getId(), pet1);

        pet1 = petService.getById(pet1.getId());
        System.out.println(pet1.getOwner().getName());
    }
}