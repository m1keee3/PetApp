import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.skirda.Application;
import ru.skirda.controller.OwnerController;
import ru.skirda.dto.OwnerDto;
import ru.skirda.entities.Owner;
import ru.skirda.service.OwnerService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OwnerController.class)
@ContextConfiguration(classes = {Application.class, OwnerControllerTest.TestConfig.class})
@Import(OwnerController.class)
public class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private ObjectMapper objectMapper;

    private OwnerDto testDto;

    @BeforeEach
    void setup() {
        testDto = new OwnerDto(1L, "BebrikOwner", LocalDate.of(1980, 1, 1));
    }

    @Test
    void createOwner_returnsCreated() throws Exception {
        Owner createdOwner = new Owner(1L, "BebrikOwner", LocalDate.of(1980, 1, 1));
        Mockito.when(ownerService.createOwner(any())).thenReturn(createdOwner);

        mockMvc.perform(post("/api/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdOwner)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("BebrikOwner"))
                .andExpect(jsonPath("$.birthDate").value("1980-01-01"));
    }

    @Test
    void getOwner_returnsOk() throws Exception {
        Mockito.when(ownerService.getOwner(1L)).thenReturn(testDto);

        mockMvc.perform(get("/api/owners/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("BebrikOwner"))
                .andExpect(jsonPath("$.birthDate").value("1980-01-01"));
    }

    @Test
    void updateOwner_returnsUpdated() throws Exception {
        Owner updatedOwner = new Owner(1L, "BoberOwner", LocalDate.of(1980, 1, 1));
        Mockito.when(ownerService.updateOwner(eq(1L), any())).thenReturn(updatedOwner);

        mockMvc.perform(put("/api/owners/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedOwner)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("BoberOwner"));
    }

    @Test
    void getAllOwners_returnsList() throws Exception {
        List<OwnerDto> owners = List.of(
                new OwnerDto(1L, "Bebrik", LocalDate.of(1980, 1, 1)),
                new OwnerDto(2L, "Bober", LocalDate.of(1985, 5, 5))
        );
        Mockito.when(ownerService.getAllOwners(any())).thenReturn(new PageImpl<>(owners, PageRequest.of(0, 10), owners.size()));

        mockMvc.perform(get("/api/owners"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Bebrik"))
                .andExpect(jsonPath("$.content[1].name").value("Bober"))
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    void deleteOwner_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/owners/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(ownerService).deleteOwner(1L);
    }

    @Configuration
    static class TestConfig {
        @Bean
        public OwnerService ownerService() {
            return Mockito.mock(OwnerService.class);
        }
    }
}
