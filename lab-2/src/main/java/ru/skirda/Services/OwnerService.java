package ru.skirda.Services;

import ru.skirda.Dao.Abstraction.IDao;
import ru.skirda.Dao.Models.OwnerDao;
import ru.skirda.Models.Owner;
import ru.skirda.Models.Pet;

import java.util.List;

public class OwnerService {
    private final IDao<Owner> ownerDao;

    public OwnerService(OwnerDao ownerDao) {
        this.ownerDao = ownerDao;
    }

    public Owner save(Owner owner) {
        return ownerDao.save(owner);
    }

    public Owner getById(Long id) {
        return ownerDao.getById(id);
    }

    public List<Owner> getAllOwners() {
        return ownerDao.getAll();
    }

    public void updateOwner(Owner owner) {
        ownerDao.update(owner);
    }

    public void deleteById(Long id) {
        Owner owner = ownerDao.getById(id);
        if (owner != null) {
            ownerDao.deleteById(id);
        }
    }

    public void addPetToOwner(Long ownerId, Pet pet) {
        Owner owner = ownerDao.getById(ownerId);
        if (owner != null) {
            owner.getPets().add(pet);
            pet.setOwner(owner);
            ownerDao.update(owner);
        }
    }
}
