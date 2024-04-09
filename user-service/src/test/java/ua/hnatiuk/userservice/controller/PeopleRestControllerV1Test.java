package ua.hnatiuk.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ua.hnatiuk.dto.PersonDTO;
import ua.hnatiuk.userservice.config.SecurityConfig;
import ua.hnatiuk.userservice.controller.rest.PeopleRestController;
import ua.hnatiuk.userservice.exception.EmailNotFoundException;
import ua.hnatiuk.userservice.model.entity.Person;
import ua.hnatiuk.userservice.service.PeopleService;
import ua.hnatiuk.userservice.utils.DataUtils;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * @author Hnatiuk Volodymyr on 07.04.2024.
 */
@WebMvcTest(PeopleRestController.class)
@Import(SecurityConfig.class)
public class PeopleRestControllerV1Test {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PeopleService peopleService;
    @Value("${carnotificator.inner-api-key}")
    private String innerApiKey;

    @Test
    @DisplayName("Test assign chat id to person functionality")
    @SneakyThrows
    public void givenPersonDTO_whenAssignChatId_thenChatIdIsAssigned() {
        // given
        PersonDTO personDTO = DataUtils.getPersonDTO();
        Person entity = DataUtils.getPersonPersisted();
        BDDMockito.given(peopleService.assignChatId(anyString(), anyLong()))
                .willReturn(entity);

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/people/assign-tg-chat-id")
                .param("inner_key", innerApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDTO))
        );

        // then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Test assign chat id to incorrect email functionality")
    @SneakyThrows
    public void givenPersonDtoWithIncorrectEmail_whenAssignChatId_thenReturnNotFound() {
        // given
        PersonDTO personDTO = DataUtils.getPersonDTO();
        BDDMockito.given(peopleService.assignChatId(anyString(), anyLong()))
                .willThrow(EmailNotFoundException.class);

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/people/assign-tg-chat-id")
                .param("inner_key", innerApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDTO))
        );

        // then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Test assign chat id with invalid inner key functionality")
    @SneakyThrows
    public void givenInvalidInnerKey_whenAssignChatId_thenReturnForbidden() {
        // given
        PersonDTO personDTO = DataUtils.getPersonDTO();
        String invalidInnerKey = "invalid-key52736";

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/people/assign-tg-chat-id")
                .param("inner_key", invalidInnerKey)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDTO))
        );

        // then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}
