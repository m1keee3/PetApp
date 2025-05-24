package ru.skirda.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pets")
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate birthDate;
    private String breed;

    @Enumerated(EnumType.STRING)
    private PetColor color;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @ManyToMany
    @JoinTable(
            name = "pet_friends",
            joinColumns = @JoinColumn(name = "pet_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private Set<Pet> friends = new HashSet<>();

    public Pet(Long id, String name, LocalDate localDate, String breed, PetColor petColor, Owner owner) {
        this.id = id;
        this.name = name;
        this.birthDate = localDate;
        this.breed = breed;
        this.color = petColor;
        this.owner = owner;
    }

    public Pet addFriend(Pet friend) {
        this.friends.add(friend);
        friend.getFriends().add(this);
        return this;
    }
}
