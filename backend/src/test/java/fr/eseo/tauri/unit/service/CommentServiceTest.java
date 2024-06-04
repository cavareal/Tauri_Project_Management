package fr.eseo.tauri.unit.service;

import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.model.Comment;
import fr.eseo.tauri.model.Sprint;
import fr.eseo.tauri.model.Team;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.repository.CommentRepository;
import fr.eseo.tauri.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    @Mock
    AuthService authService;

    @Mock
    CommentRepository commentRepository;

    @Mock
    UserService userService;

    @Mock
    TeamService teamService;

    @Mock
    SprintService sprintService;



    @InjectMocks
    CommentService commentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCommentByIdShouldReturnCommentWhenPermissionExistsAndCommentExists() {
        String token = "validToken";
        Integer id = 1;
        Comment comment = new Comment();

        when(authService.checkAuth(token, "readComment")).thenReturn(true);
        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));

        Comment result = commentService.getCommentById(token, id);

        assertEquals(comment, result);
    }

    @Test
    void getCommentByIdShouldThrowSecurityExceptionWhenPermissionDoesNotExist() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "readComment")).thenReturn(false);

        assertThrows(SecurityException.class, () -> commentService.getCommentById(token, id));
    }

    @Test
    void getCommentByIdShouldThrowResourceNotFoundExceptionWhenCommentDoesNotExist() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "readComment")).thenReturn(true);
        when(commentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> commentService.getCommentById(token, id));
    }

    @Test
    void getAllCommentsByProjectShouldReturnCommentsWhenPermissionExists() {
        String token = "validToken";
        Integer projectId = 1;
        List<Comment> comments = Arrays.asList(new Comment(), new Comment());

        when(authService.checkAuth(token, "readComments")).thenReturn(true);
        when(commentRepository.findAllByProject(projectId)).thenReturn(comments);

        List<Comment> result = commentService.getAllCommentsByProject(token, projectId);

        assertEquals(comments, result);
    }

    @Test
    void getAllCommentsByProjectShouldThrowSecurityExceptionWhenPermissionDoesNotExist() {
        String token = "validToken";
        Integer projectId = 1;

        when(authService.checkAuth(token, "readComments")).thenReturn(false);

        assertThrows(SecurityException.class, () -> commentService.getAllCommentsByProject(token, projectId));
    }

    @Test
    void getAllCommentsByProjectShouldReturnEmptyListWhenNoCommentsExist() {
        String token = "validToken";
        Integer projectId = 1;

        when(authService.checkAuth(token, "readComments")).thenReturn(true);
        when(commentRepository.findAllByProject(projectId)).thenReturn(Collections.emptyList());

        List<Comment> result = commentService.getAllCommentsByProject(token, projectId);

        assertTrue(result.isEmpty());
    }

    @Test
    void createCommentShouldSaveCommentWhenPermissionExistsAndCommentIsValid() {
        String token = "validToken";
        Comment comment = new Comment();
        comment.authorId(1);
        comment.sprintId(1);
        comment.teamId(1);

        when(authService.checkAuth(token, "addComment")).thenReturn(true);
        when(userService.getUserById(token, comment.authorId())).thenReturn(new User());
        when(sprintService.getSprintById(token, comment.sprintId())).thenReturn(new Sprint());
        when(teamService.getTeamById(token, comment.teamId())).thenReturn(new Team());

        commentService.createComment(token, comment);

        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void createCommentShouldThrowSecurityExceptionWhenPermissionDoesNotExist() {
        String token = "validToken";
        Comment comment = new Comment();

        when(authService.checkAuth(token, "addComment")).thenReturn(false);

        assertThrows(SecurityException.class, () -> commentService.createComment(token, comment));
    }

    @Test
    void deleteCommentShouldDeleteCommentWhenPermissionExistsAndCommentExists() {
        String token = "validToken";
        Integer id = 1;
        Comment comment = new Comment();

        when(authService.checkAuth(token, "deleteComment")).thenReturn(true);
        when(authService.checkAuth(token, "readComment")).thenReturn(true);
        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));

        commentService.deleteComment(token, id);

        verify(commentRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteCommentShouldThrowSecurityExceptionWhenPermissionDoesNotExist() {
        String token = "validToken";
        Integer id = 1;

        when(authService.checkAuth(token, "deleteComment")).thenReturn(false);

        assertThrows(SecurityException.class, () -> commentService.deleteComment(token, id));
    }

    @Test
    void deleteAllCommentsByProjectShouldDeleteCommentsWhenPermissionExists() {
        String token = "validToken";
        Integer projectId = 1;

        when(authService.checkAuth(token, "deleteComment")).thenReturn(true);

        commentService.deleteAllCommentsByProject(token, projectId);

        verify(commentRepository, times(1)).deleteAllByProject(projectId);
    }

    @Test
    void deleteAllCommentsByProjectShouldThrowSecurityExceptionWhenPermissionDoesNotExist() {
        String token = "validToken";
        Integer projectId = 1;

        when(authService.checkAuth(token, "deleteComment")).thenReturn(false);

        assertThrows(SecurityException.class, () -> commentService.deleteAllCommentsByProject(token, projectId));
    }

    @Test
    void updateCommentShouldThrowSecurityExceptionWhenPermissionDoesNotExist() {
        String token = "validToken";
        Integer id = 1;
        Comment updatedComment = new Comment();

        when(authService.checkAuth(token, "updateComment")).thenReturn(false);

        assertThrows(SecurityException.class, () -> commentService.updateComment(token, id, updatedComment));
    }

    @Test
    void updateCommentShouldNotUpdateCommentWhenUpdatedCommentFieldsAreNull() {
        String token = "validToken";
        Integer id = 1;
        Comment updatedComment = new Comment();
        Comment existingComment = new Comment();
        existingComment.content("Existing Content");
        existingComment.feedback(false);

        when(authService.checkAuth(token, "updateComment")).thenReturn(true);
        when(authService.checkAuth(token, "readComment")).thenReturn(true);
        when(commentRepository.findById(id)).thenReturn(Optional.of(existingComment));
        commentService.updateComment(token, id, updatedComment);

        assertEquals("Existing Content", existingComment.content());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

}


