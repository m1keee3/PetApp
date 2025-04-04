package ru.skirda.Services;

import ru.skirda.Dao.Abstraction.IDao;
import ru.skirda.Dao.Models.PetDao;
import ru.skirda.Models.Pet;

import java.util.List;

public class PetService {
    private final IDao<Pet> petDao;

    public PetService(PetDao petDao) {
        this.petDao = petDao;
    }

    public Pet save(Pet pet) {
        petDao.save(pet);
        return pet;
    }

    public Pet getById(Long id) {
        return petDao.getById(id);
    }

    public List<Pet> getAllPets() {
        return petDao.getAll();
    }

    public void updatePet(Pet pet) {
        petDao.update(pet);
    }

    public void deleteById(Long id) {
        Pet pet = petDao.getById(id);
        if (pet != null) {
            petDao.deleteById(id);
        }
    }

    public void addFriend(Long petId, Long friendId) {
        Pet pet = petDao.getById(petId);
        Pet friend = petDao.getById(friendId);

        if (pet != null && friend != null && !pet.getFriends().contains(friend)) {
            pet.getFriends().add(friend);
            friend.getFriends().add(pet);
            petDao.update(pet);
            petDao.update(friend);
        }
    }
}

