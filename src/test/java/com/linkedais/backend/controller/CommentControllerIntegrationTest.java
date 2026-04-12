package com.linkedais.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkedais.backend.dto.CommentResponse;
import com.linkedais.backend.dto.CreateCommentRequest;
import com.linkedais.backend.dto.UpdateCommentRequest;
import com.linkedais.backend.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // MockBean – CommentService pakeičiamas mock'u (nereikia DB)
    @MockitoBean
    private CommentService commentService;

    // Testinis CommentResponse objektas
    private CommentResponse sampleResponse() {
        return new CommentResponse(100L, 10L, 1L, "Jonas Jonaitis",
                "Labas, tai įdomu!", LocalDateTime.now());
    }


    @Test
    @WithMockUser(username = "jonas@test.lt") // simuliuoja prisijungusį vartotoją
    void getComments_returnsOkWithList() throws Exception {
        when(commentService.getCommentsByPostId(10L))
                .thenReturn(List.of(sampleResponse()));

        mockMvc.perform(get("/api/posts/10/comments"))
                .andExpect(status().isOk())                          // HTTP 200
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].content").value("Labas, tai įdomu!"))
                .andExpect(jsonPath("$[0].authorName").value("Jonas Jonaitis"));
    }

    @Test
    @WithMockUser(username = "jonas@test.lt")
    void getComments_postNotFound_returns500() throws Exception {
        when(commentService.getCommentsByPostId(99L))
                .thenThrow(new RuntimeException("Post not found"));

        mockMvc.perform(get("/api/posts/99/comments"))
                .andExpect(status().is4xxClientError());
    }


    @Test
    @WithMockUser(username = "jonas@test.lt")
    void createComment_validRequest_returns201() throws Exception {
        CreateCommentRequest request = new CreateCommentRequest();
        request.setContent("Naujas komentaras");

        when(commentService.createComment(eq(10L), any(), eq("jonas@test.lt")))
                .thenReturn(sampleResponse());

        mockMvc.perform(post("/api/posts/10/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())                     // HTTP 201
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.content").value("Labas, tai įdomu!"));
    }

    @Test
    void createComment_notAuthenticated_returns401or403() throws Exception {
        CreateCommentRequest request = new CreateCommentRequest();
        request.setContent("Tekstas");

        // Neprisijungęs vartotojas negali kurti komentarų
        mockMvc.perform(post("/api/posts/10/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }


    @Test
    @WithMockUser(username = "jonas@test.lt")
    void updateComment_validRequest_returnsOk() throws Exception {
        UpdateCommentRequest request = new UpdateCommentRequest();
        request.setContent("Atnaujintas tekstas");

        CommentResponse updated = new CommentResponse(100L, 10L, 1L,
                "Jonas Jonaitis", "Atnaujintas tekstas", LocalDateTime.now());

        when(commentService.updateComment(eq(10L), eq(100L), any(), eq("jonas@test.lt")))
                .thenReturn(updated);

        mockMvc.perform(put("/api/posts/10/comments/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())                          // HTTP 200
                .andExpect(jsonPath("$.content").value("Atnaujintas tekstas"));
    }

    @Test
    @WithMockUser(username = "kitas@test.lt")
    void updateComment_notAuthor_returns500() throws Exception {
        UpdateCommentRequest request = new UpdateCommentRequest();
        request.setContent("Tekstas");

        when(commentService.updateComment(eq(10L), eq(100L), any(), eq("kitas@test.lt")))
                .thenThrow(new RuntimeException("Forbidden"));

        mockMvc.perform(put("/api/posts/10/comments/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "jonas@test.lt")
    void deleteComment_authorDeletes_returns204() throws Exception {
        doNothing().when(commentService)
                .deleteComment(10L, 100L, "jonas@test.lt");

        mockMvc.perform(delete("/api/posts/10/comments/100"))
                .andExpect(status().isNoContent());                  // HTTP 204

        verify(commentService, times(1))
                .deleteComment(10L, 100L, "jonas@test.lt");
    }

    @Test
    @WithMockUser(username = "kitas@test.lt")
    void deleteComment_notAuthor_returns500() throws Exception {
        doThrow(new RuntimeException("Forbidden"))
                .when(commentService).deleteComment(10L, 100L, "kitas@test.lt");

        mockMvc.perform(delete("/api/posts/10/comments/100"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void deleteComment_notAuthenticated_returns401or403() throws Exception {
        mockMvc.perform(delete("/api/posts/10/comments/100"))
                .andExpect(status().is4xxClientError());
    }
}