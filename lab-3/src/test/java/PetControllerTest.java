import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import ru.skirda.Application;
import ru.skirda.config.SecurityConfig;
import ru.skirda.controller.PetController;
import ru.skirda.dto.PetDto;
import ru.skirda.entities.Owner;
import ru.skirda.entities.Pet;
import ru.skirda.entities.PetColor;
import ru.skirda.service.PetService;
import ru.skirda.service.UserService;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PetController.class)
@ContextConfiguration(classes = {Application.class, SecurityConfig.class, PetControllerTest.TestConfig.class})
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetService petService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PetService petService() {
            return Mockito.mock(PetService.class);
        }

        @Bean
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }

    @Nested
    @DisplayName("PUT /api/pets/{id}")
    class UpdatePetTests {

        @Test
        @WithMockUser(username = "user1", roles = {"USER"})
        void userCanUpdateOwnPet() throws Exception {
            PetDto dto = new PetDto(1L, "Bebrik", "ubiytsa", PetColor.ORANGE.toString(), LocalDate.of(2020, 1, 1), 1L);

            Pet updated = new Pet()
                    .setId(1L)
                    .setName("Bebrik")
                    .setBreed("ubiytsa")
                    .setColor(PetColor.ORANGE)
                    .setBirthDate(LocalDate.of(2020, 1, 1))
                    .setOwner(new Owner(1L, "BebrikOwner", LocalDate.of(2020, 1, 1)));

            Mockito.when(petService.updatePet(eq(1L), any(), eq("user1")))
                    .thenReturn(updated);

            mockMvc.perform(put("/api/pets/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.name").value("Bebrik"));
        }

        @Test
        @WithMockUser(username = "user2", roles = {"USER"})
        void userForbiddenToUpdateOthersPet() throws Exception {
            PetDto dto = new PetDto(1L, "Brr Brr Patapim", "catito", PetColor.GRAY.toString(), LocalDate.of(2019, 5, 10), 2L);

            Mockito.when(petService.updatePet(eq(1L), any(), eq("user2")))
                    .thenThrow(new org.springframework.web.server.ResponseStatusException(HttpStatusCode.valueOf(403), "Forbidden"));

            mockMvc.perform(put("/api/pets/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("DELETE /api/pets/{id}")
    class DeletePetTests {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void adminCanDeleteAnyPet() throws Exception {
            Mockito.doNothing().when(petService).deletePet(1L, "admin");

            mockMvc.perform(delete("/api/pets/1"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @WithMockUser(username = "user1", roles = {"USER"})
        void userCanDeleteOwnPet() throws Exception {
            Mockito.doNothing().when(petService).deletePet(2L, "user1");

            mockMvc.perform(delete("/api/pets/2"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @WithMockUser(username = "userX", roles = {"USER"})
        void userForbiddenToDeleteOtherPet() throws Exception {
            Mockito.doThrow(new org.springframework.web.server.ResponseStatusException(HttpStatusCode.valueOf(403), "Forbidden"))
                    .when(petService).deletePet(3L, "userX");

            mockMvc.perform(delete("/api/pets/3"))
                    .andExpect(status().isForbidden());
        }
    }
}
