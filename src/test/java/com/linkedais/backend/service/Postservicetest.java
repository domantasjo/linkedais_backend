package com.linkedais.backend.service;

import com.linkedais.backend.dto.CreatePostRequest;
import com.linkedais.backend.dto.PostResponse;
import com.linkedais.backend.model.Post;
import com.linkedais.backend.model.User;
import com.linkedais.backend.repository.CommentRepository;
import com.linkedais.backend.repository.LikeRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock private PostRepository postRepository;
    @Mock private UserRepository userRepository;
    @Mock private LikeRepository likeRepository;
    @Mock private CommentRepository commentRepository;

    @InjectMocks
    private PostService postService;

    private User testUser;
    private Post testPost;

    // Pagalbinis metodas – nustato private lauką per reflection
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
        testPost.setContent("Testas post turinys");
        testPost.setAuthor(testUser);
    }

    @Test
    void createPost_validData_returnsPostResponse() {
        // Arrange
        CreatePostRequest request = new CreatePostRequest();
        request.setContent("Naujas įrašas");

        when(userRepository.findByEmail("jonas@test.lt")).thenReturn(Optional.of(testUser));
        when(postRepository.save(any(Post.class))).thenReturn(testPost);
        when(likeRepository.countByPostId(10L)).thenReturn(5);

        // Act
        PostResponse result = postService.createPost(request, "jonas@test.lt");

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Testas post turinys", result.getContent());
        assertEquals(1L, result.getAuthorId());
        assertEquals("Jonas Jonaitis", result.getAuthorName());
        assertEquals(5, result.getLikeCount());
        assertEquals(0, result.getCommentCount());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void createPost_userNotFound_throwsException() {
        // Arrange
        when(userRepository.findByEmail("nera@test.lt")).thenReturn(Optional.empty());

        CreatePostRequest request = new CreatePostRequest();
        request.setContent("Tekstas");

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> postService.createPost(request, "nera@test.lt"));

        assertEquals("User no found", ex.getMessage());
        verify(postRepository, never()).save(any());
    }

    // Parametrizuotas testas – skirtingi post turinio tekstai
    @ParameterizedTest
    @ValueSource(strings = {"Trumpas", "Vidutinis įrašas", "Labai ilgas įrašas su daug teksto ir informacijos!!!"})
    void createPost_differentContentLengths_allSucceed(String content) {
        CreatePostRequest request = new CreatePostRequest();
        request.setContent(content);

        Post savedPost = new Post();
        try { setField(savedPost, "id", 1L); } catch (Exception e) { fail(e.getMessage()); }
        savedPost.setContent(content);
        savedPost.setAuthor(testUser);

        when(userRepository.findByEmail("jonas@test.lt")).thenReturn(Optional.of(testUser));
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);
        when(likeRepository.countByPostId(any())).thenReturn(0);

        PostResponse result = postService.createPost(request, "jonas@test.lt");

        assertNotNull(result);
        assertEquals(content, result.getContent());
    }


    @Test
    void getAllPosts_returnsPostList() {
        // Arrange
        Page<Post> page = new PageImpl<>(List.of(testPost));
        when(postRepository.findAllByOrderByCreatedAtDesc(any(Pageable.class))).thenReturn(page);
        when(likeRepository.countByPostId(10L)).thenReturn(3);
        when(commentRepository.countByPostId(10L)).thenReturn(2);

        // Act
        List<PostResponse> result = postService.getAllPosts(0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Testas post turinys", result.get(0).getContent());
        assertEquals(3, result.get(0).getLikeCount());
        assertEquals(2, result.get(0).getCommentCount());
    }

    @Test
    void getAllPosts_noPosts_returnsEmptyList() {
        // Arrange
        Page<Post> emptyPage = new PageImpl<>(List.of());
        when(postRepository.findAllByOrderByCreatedAtDesc(any(Pageable.class))).thenReturn(emptyPage);

        // Act
        List<PostResponse> result = postService.getAllPosts(0, 10);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllPosts_multiplePostsReturned() {
        // Arrange – 3 postai
        Post post2 = new Post();
        try { setField(post2, "id", 20L); } catch (Exception e) { fail(e.getMessage()); }
        post2.setContent("Antras įrašas");
        post2.setAuthor(testUser);

        Post post3 = new Post();
        try { setField(post3, "id", 30L); } catch (Exception e) { fail(e.getMessage()); }
        post3.setContent("Trečias įrašas");
        post3.setAuthor(testUser);

        Page<Post> page = new PageImpl<>(List.of(testPost, post2, post3));
        when(postRepository.findAllByOrderByCreatedAtDesc(any(Pageable.class))).thenReturn(page);
        when(likeRepository.countByPostId(any())).thenReturn(0);
        when(commentRepository.countByPostId(any())).thenReturn(0);

        // Act
        List<PostResponse> result = postService.getAllPosts(0, 10);

        // Assert
        assertEquals(3, result.size());
    }


    @Test
    void deletePostById_authorDeletes_success() {
        // Arrange
        when(postRepository.findById(10L)).thenReturn(Optional.of(testPost));

        // Act – neturi mesti išimties
        assertDoesNotThrow(() ->
                postService.deletePostById(10L, "jonas@test.lt"));

        // Patikriname kad deleteById() buvo kviečiamas
        verify(postRepository, times(1)).deleteById(10L);
    }

    @Test
    void deletePostById_postNotFound_throwsException() {
        // Arrange
        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> postService.deletePostById(999L, "jonas@test.lt"));

        assertEquals("Post not found", ex.getMessage());
        verify(postRepository, never()).deleteById(any());
    }

    @Test
    void deletePostById_notAuthor_throwsException() {
        // Arrange – kitas vartotojas bando ištrinti
        when(postRepository.findById(10L)).thenReturn(Optional.of(testPost));

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> postService.deletePostById(10L, "kitas@test.lt"));

        assertEquals("You can only delete your own posts", ex.getMessage());
        verify(postRepository, never()).deleteById(any());
    }
}