package ru.skirda.Dao.Models;

import jakarta.persistence.EntityManager;
import ru.skirda.Dao.Abstraction.IDao;
import ru.skirda.Models.Owner;

import java.util.List;

public class OwnerDao implements IDao<Owner> {
    private final EntityManager entityManager;

    public OwnerDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Owner save(Owner owner) {
        entityManager.getTransaction().begin();
        entityManager.persist(owner);
        entityManager.flush();
        entityManager.getTransaction().commit();
        return owner;
    }

    @Override
    public void deleteById(long id) {
        entityManager.getTransaction().begin();
        Owner owner = entityManager.find(Owner.class, id);
        if (owner != null) {
            entityManager.remove(owner);
        }
        entityManager.getTransaction().commit();
    }

    @Override
    public void deleteByEntity(Owner owner) {
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.contains(owner) ? owner : entityManager.merge(owner));
        entityManager.getTransaction().commit();
    }

    @Override
    public void deleteAll() {
        entityManager.getTransaction().begin();
        entityManager.createQuery("DELETE FROM Owner").executeUpdate();
        entityManager.getTransaction().commit();
    }

    @Override
    public Owner update(Owner owner) {
        entityManager.getTransaction().begin();
        Owner merged = entityManager.merge(owner);
        entityManager.getTransaction().commit();
        return merged;
    }

    @Override
    public Owner getById(long id) {
        return entityManager.find(Owner.class, id);
    }

    @Override
    public List<Owner> getAll() {
        return entityManager.createQuery("SELECT o FROM Owner o", Owner.class)
                .getResultList();
    }
}
