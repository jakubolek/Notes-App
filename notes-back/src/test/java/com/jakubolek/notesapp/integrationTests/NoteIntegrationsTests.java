package com.jakubolek.notesapp.integrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakubolek.notesapp.model.Note;
import com.jakubolek.notesapp.model.User;
import com.jakubolek.notesapp.repository.NoteRepository;
import com.jakubolek.notesapp.service.NoteService;
import com.jakubolek.notesapp.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.jakubolek.notesapp.utils.ApiUrlDefaults.*;
import static com.jakubolek.notesapp.utils.NoteDefaults.*;
import static com.jakubolek.notesapp.utils.UserDefaults.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
class NoteIntegrationsTests {

    private final String updatedContent = "new content";
    private String accessToken;
    private User defaultUser;

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private NoteService noteService;

    @BeforeAll
    void setUp() throws Exception {
        createNewUser(defaultUsername, defaultEmail, defaultPassword);
        accessToken = signIn(defaultUsername, defaultPassword);
        defaultUser = initDefaultUser();
    }

    @Test
    void shouldCreateNewNote() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(noteUrl)
                .contentType(APPLICATION_JSON)
                .header(authorizationKey, accessToken)
                .content(objectMapper.writeValueAsString(contentBody(noteKey, defaultContent))))
                .andReturn();

        Note noteResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Note.class);
        Optional<Note> note = noteRepository.findById(noteResponse.getId());

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(201);
        assertThat(note.get().getContent()).isEqualTo(defaultContent);
    }

    @Test
    void shouldNotCreateNewNoteWhenRequestBodyIsNotCorrect() throws Exception {
        long notesQuantityBeforeTest = noteRepository.count();

        MvcResult mvcResult = mockMvc.perform(post(noteUrl)
                .contentType(APPLICATION_JSON)
                .header(authorizationKey, accessToken)
                .content("incorrect Content"))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
        assertThat(noteRepository.count()).isEqualTo(notesQuantityBeforeTest);
    }

    @Test
    void shouldNotCreateNewNoteWhenUserIsNotAuthorized() throws Exception {
        long notesQuantityBeforeTest = noteRepository.count();

        MvcResult mvcResult = mockMvc.perform(post(noteUrl)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contentBody(noteKey, defaultContent))))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(403);
        assertThat(noteRepository.count()).isEqualTo(notesQuantityBeforeTest);
    }

    @Test
    void shouldShowAllNotes() throws Exception {
        createNewNote();
        createNewNote();

        MvcResult mvcResult = mockMvc.perform(get(noteUrl)
                .contentType(APPLICATION_JSON)
                .header(authorizationKey, accessToken))
                .andReturn();

        Object[] notesResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Object[].class);

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
        assertThat(noteRepository.findByUsername(defaultUser).size()).isEqualTo(notesResponse.length);
    }

    @Test
    void shouldUpdateNoteContent() throws Exception {
        Note note = createNewNote();

        MvcResult mvcResult = mockMvc.perform(put(notePutAndDeleteUrl + note.getId())
                .contentType(APPLICATION_JSON)
                .header(authorizationKey, accessToken)
                .content(objectMapper.writeValueAsString(contentBody(updateNoteKey, updatedContent))))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);

        Optional<Note> expectedNote = noteRepository.findById(note.getId());
        assertThat(expectedNote.get().getContent()).isEqualTo(updatedContent);
    }

    @Test
    void shouldNotUpdateNoteWhenRequestBodyIsNotCorrect() throws Exception {
        Note note = createNewNote();

        MvcResult mvcResult = mockMvc.perform(put(notePutAndDeleteUrl + note.getId())
                .contentType(APPLICATION_JSON)
                .header(authorizationKey, accessToken)
                .content("incorrect content"))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
        assertThat(note.getContent()).isNotEqualTo(updatedContent);
    }

    @Test
    void shouldNotUpdateNoteWhenUserIsNotAuthorized() throws Exception {
        Note note = createNewNote();

        MvcResult mvcResult = mockMvc.perform(put(notePutAndDeleteUrl + note.getId())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contentBody(updateNoteKey, updatedContent))))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(403);
        assertThat(note.getContent()).isNotEqualTo(updatedContent);
    }

    @Test
    void shouldNotUpdateOtherUserNote() throws Exception {
        Note note = createNewNote();
        createNewUser(defaultSecondUsername, defaultSecondEmail, defaultSecondPassword);
        String secondUserToken = signIn(defaultSecondUsername, defaultSecondPassword);

        MvcResult mvcResult = mockMvc.perform(put(notePutAndDeleteUrl + note.getId())
                .contentType(APPLICATION_JSON)
                .header(authorizationKey, secondUserToken)
                .content(objectMapper.writeValueAsString(contentBody(updateNoteKey, updatedContent))))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(403);
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(missingPermissionMessage);
        assertThat(note.getContent()).isNotEqualTo(updatedContent);
    }

    @Test
    void shouldDeleteNote() throws Exception {
        Note note = createNewNote();

        MvcResult mvcResult = mockMvc.perform(delete(notePutAndDeleteUrl + note.getId())
                .contentType(APPLICATION_JSON)
                .header(authorizationKey, accessToken))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(204);
        assertThat(noteRepository.findById(note.getId())).isEmpty();
    }

    private void createNewUser(String username, String email, String password) {
        userService.saveUser(username, email, password);
    }

    private String signIn(String username, String password) throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(loginUrl)
                .contentType(APPLICATION_FORM_URLENCODED_VALUE)
                .param(usernameKey, username)
                .param(passwordKey, password))
                .andReturn();

        Map resultString = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Map.class);
        return "Bearer " + resultString.get("access_token");
    }

    private User initDefaultUser() {
        return userService.getUser(defaultUsername);
    }

    private Note createNewNote() {
        return noteService.createNote(defaultContent, defaultUsername);
    }

    private Map<String, String> contentBody(String key, String value) {
        Map<String, String> payload = new HashMap<>();
        payload.put(key, value);
        return payload;
    }
}