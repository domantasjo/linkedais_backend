package com.linkedais.backend.service;

import com.linkedais.backend.dto.CommentResponse;
import com.linkedais.backend.dto.CreateCommentRequest;
import com.linkedais.backend.dto.UpdateCommentRequest;
import com.linkedais.backend.model.Comment;
import com.linkedais.backend.model.Post;
import com.linkedais.backend.model.User;
import com.linkedais.backend.repository.CommentRepository;
import com.linkedais.backend.repository.PostRepository;
import com.linkedais.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock private CommentRepository commentRepository;
    @Mock private PostRepository postRepository;
    @Mock private UserRepository userRepository;
    @Mock private NotificationService notificationService;

    @InjectMocks
    private CommentService commentService;

    private Post testPost;
    private User testUser;
    private Comment testComment;

    // Pagalbinis metodas – nustato private lauką per reflection (kai nėra setter'io)
    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @BeforeEach
    void setUp() throws Exception {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Jonas Jonaitis");
        testUser.setEmail("jonas@test.lt");

        testPost = new Post();
        setField(testPost, "id", 10L);
        testPost.setAuthor(testUser);

        testComment = new Comment();
        testComment.setId(100L);
        testComment.setPost(testPost);
        testComment.setAuthor(testUser);
        testComment.setContent("Labas, tai įdomu!");
    }

    @Test
    void getCommentsByPostId_postExists_returnsCommentList() {
        when(postRepository.findById(10L)).thenReturn(Optional.of(testPost));
        when(commentRepository.findByPostIdOrderByCreatedAtAsc(10L))
                .thenReturn(List.of(testComment));

        List<CommentResponse> result = commentService.getCommentsByPostId(10L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Labas, tai įdomu!", result.get(0).getContent());
    }

    @Test
    void getCommentsByPostId_postNotFound_throwsException() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> commentService.getCommentsByPostId(99L));

        assertEquals("Post not found", ex.getMessage());
    }

    @Test
    void getCommentsByPostId_noComments_returnsEmptyList() {
        when(postRepository.findById(10L)).thenReturn(Optional.of(testPost));
        when(commentRepository.findByPostIdOrderByCreatedAtAsc(10L))
                .thenReturn(List.of());

        List<CommentResponse> result = commentService.getCommentsByPostId(10L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    @Test
    void createComment_validData_returnsCommentResponse() {
        CreateCommentRequest request = new CreateCommentRequest();
        request.setContent("Naujas komentaras");

        when(postRepository.findById(10L)).thenReturn(Optional.of(testPost));
        when(userRepository.findByEmail("jonas@test.lt")).thenReturn(Optional.of(testUser));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        CommentResponse result = commentService.createComment(10L, request, "jonas@test.lt");

        assertNotNull(result);
        assertEquals(testComment.getId(), result.getId());
        verify(notificationService, times(1))
                .createCommentNotifications(any(), any(), any());
    }

    @Test
    void createComment_postNotFound_throwsException() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        CreateCommentRequest request = new CreateCommentRequest();
        request.setContent("Tekstas");

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> commentService.createComment(99L, request, "jonas@test.lt"));

        assertEquals("Post not found", ex.getMessage());
        verify(commentRepository, never()).save(any());
    }

    @Test
    void createComment_userNotFound_throwsException() {
        when(postRepository.findById(10L)).thenReturn(Optional.of(testPost));
        when(userRepository.findByEmail("nera@test.lt")).thenReturn(Optional.empty());

        CreateCommentRequest request = new CreateCommentRequest();
        request.setContent("Tekstas");

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> commentService.createComment(10L, request, "nera@test.lt"));

        assertEquals("User not found", ex.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Trumpas", "Vidutinis komentaras", "Labai ilgas komentaras su daug teksto!!!"})
    void createComment_differentContentLengths_allSucceed(String content) {
        CreateCommentRequest request = new CreateCommentRequest();
        request.setContent(content);

        Comment savedComment = new Comment();
        savedComment.setId(1L);
        savedComment.setPost(testPost);
        savedComment.setAuthor(testUser);
        savedComment.setContent(content);

        when(postRepository.findById(10L)).thenReturn(Optional.of(testPost));
        when(userRepository.findByEmail("jonas@test.lt")).thenReturn(Optional.of(testUser));
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        CommentResponse result = commentService.createComment(10L, request, "jonas@test.lt");

        assertNotNull(result);
        assertEquals(content, result.getContent());
    }


    @Test
    void updateComment_authorUpdates_returnsUpdatedComment() {
        UpdateCommentRequest request = new UpdateCommentRequest();
        request.setContent("Atnaujintas tekstas");

        Comment updatedComment = new Comment();
        updatedComment.setId(100L);
        updatedComment.setPost(testPost);
        updatedComment.setAuthor(testUser);
        updatedComment.setContent("Atnaujintas tekstas");

        when(commentRepository.findById(100L)).thenReturn(Optional.of(testComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(updatedComment);

        CommentResponse result = commentService.updateComment(10L, 100L, request, "jonas@test.lt");

        assertEquals("Atnaujintas tekstas", result.getContent());
    }

    @Test
    void updateComment_commentNotFound_throwsException() {
        when(commentRepository.findById(999L)).thenReturn(Optional.empty());

        UpdateCommentRequest request = new UpdateCommentRequest();
        request.setContent("Tekstas");

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> commentService.updateComment(10L, 999L, request, "jonas@test.lt"));

        assertEquals("Comment not found", ex.getMessage());
    }

    @Test
    void updateComment_wrongPost_throwsException() {
        when(commentRepository.findById(100L)).thenReturn(Optional.of(testComment));

        UpdateCommentRequest request = new UpdateCommentRequest();
        request.setContent("Tekstas");

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> commentService.updateComment(99L, 100L, request, "jonas@test.lt"));

        assertEquals("Comment not found", ex.getMessage());
    }

    @Test
    void updateComment_notAuthor_throwsForbidden() {
        when(commentRepository.findById(100L)).thenReturn(Optional.of(testComment));

        UpdateCommentRequest request = new UpdateCommentRequest();
        request.setContent("Tekstas");

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> commentService.updateComment(10L, 100L, request, "kitas@test.lt"));

        assertEquals("Forbidden", ex.getMessage());
    }

    @Test
    void deleteComment_authorDeletes_success() {
        when(commentRepository.findById(100L)).thenReturn(Optional.of(testComment));

        assertDoesNotThrow(() ->
                commentService.deleteComment(10L, 100L, "jonas@test.lt"));

        verify(commentRepository, times(1)).delete(testComment);
    }

    @Test
    void deleteComment_commentNotFound_throwsException() {
        when(commentRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> commentService.deleteComment(10L, 999L, "jonas@test.lt"));

        assertEquals("Comment not found", ex.getMessage());
        verify(commentRepository, never()).delete(any());
    }

    @Test
    void deleteComment_notAuthor_throwsForbidden() {
        when(commentRepository.findById(100L)).thenReturn(Optional.of(testComment));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> commentService.deleteComment(10L, 100L, "svetimas@test.lt"));

        assertEquals("Forbidden", ex.getMessage());
        verify(commentRepository, never()).delete(any());
    }

    @Test
    void deleteComment_wrongPost_throwsException() {
        when(commentRepository.findById(100L)).thenReturn(Optional.of(testComment));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> commentService.deleteComment(99L, 100L, "jonas@test.lt"));

        assertEquals("Comment not found", ex.getMessage());
    }
}