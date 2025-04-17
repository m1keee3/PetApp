import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import ru.skirda.Application;
import ru.skirda.controller.PetController;
import ru.skirda.dto.PetDto;
import ru.skirda.entities.Owner;
import ru.skirda.entities.Pet;
import ru.skirda.entities.PetColor;
import ru.skirda.service.PetService;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PetController.class)
@ContextConfiguration(classes = {Application.class, PetControllerTest.TestConfig.class})
@Import(PetController.class)
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetService petService;

    @Autowired
    private ObjectMapper objectMapper;

    private Owner owner;
    private Pet pet;
    private PetDto petDto;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        owner = new Owner(1L, "BebrikOwner", LocalDate.of(1980, 1, 1));
        pet = new Pet(1L, "Bebrik", LocalDate.of(2020, 1, 1), "Таксалёт", PetColor.BLACK, owner);
        petDto = new PetDto(1L, "Bebrik", "Таксалёт", PetColor.BLACK.toString(), LocalDate.of(2020, 1, 1), owner.getId());
    }

    @Test
    void createPet_returnsCreated() throws Exception {
        Mockito.when(petService.createPet(any())).thenReturn(pet);

        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pet)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Bebrik"))
                .andExpect(jsonPath("$.birthDate").value("2020-01-01"))
                .andExpect(jsonPath("$.breed").value("Таксалёт"))
                .andExpect(jsonPath("$.color").value("BLACK"));
    }

    @Test
    void getPet_returnsOk() throws Exception {
        Mockito.when(petService.getPet(1L)).thenReturn(petDto);

        mockMvc.perform(get("/api/pets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bebrik"))
                .andExpect(jsonPath("$.birthDate").value("2020-01-01"))
                .andExpect(jsonPath("$.breed").value("Таксалёт"))
                .andExpect(jsonPath("$.color").value("BLACK"));
    }

    @Test
    void updatePet_returnsUpdated() throws Exception {
        Pet updated = new Pet(1L, "Bober", LocalDate.of(2020, 1, 1), "brrr brr Patapim", PetColor.BLACK, owner);
        Mockito.when(petService.updatePet(eq(1L), any())).thenReturn(updated);

        mockMvc.perform(put("/api/pets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bober"))
                .andExpect(jsonPath("$.breed").value("brrr brr Patapim"))
                .andExpect(jsonPath("$.color").value("BLACK"));
    }

    @Test
    void deletePet_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/pets/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(petService).deletePet(1L);
    }

    @Configuration
    static class TestConfig {
        @Bean
        public PetService petService() {
            return Mockito.mock(PetService.class);
        }
    }
}
