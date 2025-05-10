import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import ru.skirda.Application;
import ru.skirda.config.SecurityConfig;
import ru.skirda.controller.OwnerController;
import ru.skirda.dto.OwnerDto;
import ru.skirda.entities.Owner;
import ru.skirda.service.OwnerService;
import ru.skirda.service.UserService;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OwnerController.class)
@ContextConfiguration(classes = {Application.class, SecurityConfig.class,OwnerControllerTest.TestConfig.class})
@Import(OwnerController.class)
class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public OwnerService ownerService() {
            return Mockito.mock(OwnerService.class);
        }

        @Bean
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }

    @Nested
    @DisplayName("PUT /api/owners/{id}")
    class UpdateOwnerTests {

        @Test
        @WithMockUser(username = "user1", roles = {"USER"})
        void userCanUpdateOwnOwner() throws Exception {
            OwnerDto dto = new OwnerDto(1L, "Croco", LocalDate.of(2000, 1, 1));

            Owner updated = new Owner().setId(1L).setName("Croco").setBirthDate(LocalDate.of(2000, 1, 1));

            Mockito.when(ownerService.updateOwner(eq(1L), any(), eq("user1")))
                    .thenReturn(updated);

            mockMvc.perform(put("/api/owners/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.name").value("Croco"));
        }

        @Test
        @WithMockUser(username = "user2", roles = {"USER"})
        void userForbiddenToUpdateOtherOwner() throws Exception {
            OwnerDto dto = new OwnerDto(1L, "Shimpansini Bananini", LocalDate.of(2000, 1, 1));

            Mockito.when(ownerService.updateOwner(eq(1L), any(), eq("user2")))
                    .thenThrow(new org.springframework.web.server.ResponseStatusException(HttpStatusCode.valueOf(403), "Forbidden"));

            mockMvc.perform(put("/api/owners/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("DELETE /api/owners/{id}")
    class DeleteOwnerTests {

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void adminCanDeleteAnyOwner() throws Exception {
            Mockito.doNothing().when(ownerService).deleteOwner(1L, "admin");

            mockMvc.perform(delete("/api/owners/1"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @WithMockUser(username = "user1", roles = {"USER"})
        void userCanDeleteOwnOwner() throws Exception {
            Mockito.doNothing().when(ownerService).deleteOwner(2L, "user1");

            mockMvc.perform(delete("/api/owners/2"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @WithMockUser(username = "userX", roles = {"USER"})
        void userForbiddenToDeleteOtherOwner() throws Exception {
            Mockito.doThrow(new org.springframework.web.server.ResponseStatusException(HttpStatusCode.valueOf(403), "Forbidden"))
                    .when(ownerService).deleteOwner(3L, "userX");

            mockMvc.perform(delete("/api/owners/3"))
                    .andExpect(status().isForbidden());
        }
    }
}
