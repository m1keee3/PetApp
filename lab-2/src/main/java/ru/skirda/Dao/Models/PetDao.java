package ru.skirda.Dao.Models;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import ru.skirda.Dao.Abstraction.IDao;
import ru.skirda.Models.Pet;
import ru.skirda.Models.PetColor;

import java.util.List;

public class PetDao implements IDao<Pet> {
    private final EntityManager entityManager;

    public PetDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Pet save(Pet pet) {
        entityManager.getTransaction().begin();
        entityManager.persist(pet);
        entityManager.flush();
        entityManager.getTransaction().commit();
        return pet;
    }

    @Override
    public void deleteById(long id) {
        entityManager.getTransaction().begin();
        Pet pet = entityManager.find(Pet.class, id);
        if (pet != null) {
            entityManager.remove(pet);
        }
        entityManager.getTransaction().commit();
    }

    @Override
    public void deleteByEntity(Pet pet) {
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.contains(pet) ? pet : entityManager.merge(pet));
        entityManager.getTransaction().commit();
    }

    @Override
    public void deleteAll() {
        entityManager.getTransaction().begin();
        entityManager.createQuery("DELETE FROM Pet").executeUpdate();
        entityManager.getTransaction().commit();
    }

    @Override
    public Pet update(Pet pet) {
        entityManager.getTransaction().begin();
        Pet merged = entityManager.merge(pet);
        entityManager.getTransaction().commit();
        return merged;
    }

    @Override
    public Pet getById(long id) {
        return entityManager.find(Pet.class, id);
    }

    @Override
    public List<Pet> getAll() {
        return entityManager.createQuery("SELECT p FROM Pet p", Pet.class)
                .getResultList();
    }
}
