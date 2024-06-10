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
        Integer id = 1;
        Comment comment = new Comment();

        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));

        Comment result = commentService.getCommentById(id);

        assertEquals(comment, result);
    }

    @Test
    void getCommentByIdShouldThrowResourceNotFoundExceptionWhenCommentDoesNotExist() {
        Integer id = 1;

        when(commentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> commentService.getCommentById(id));
    }

    @Test
    void getAllCommentsByProjectShouldReturnCommentsWhenPermissionExists() {
        Integer projectId = 1;
        List<Comment> comments = Arrays.asList(new Comment(), new Comment());

        when(commentRepository.findAllByProject(projectId)).thenReturn(comments);

        List<Comment> result = commentService.getAllCommentsByProject(projectId);

        assertEquals(comments, result);
    }

    @Test
    void getAllCommentsByProjectShouldReturnEmptyListWhenNoCommentsExist() {
        Integer projectId = 1;

        when(commentRepository.findAllByProject(projectId)).thenReturn(Collections.emptyList());

        List<Comment> result = commentService.getAllCommentsByProject(projectId);

        assertTrue(result.isEmpty());
    }

    @Test
    void createCommentShouldSaveCommentWhenPermissionExistsAndCommentIsValid() {
        Comment comment = new Comment();
        comment.authorId(1);
        comment.sprintId(1);
        comment.teamId(1);

        when(userService.getUserById(comment.authorId())).thenReturn(new User());
        when(sprintService.getSprintById(comment.sprintId())).thenReturn(new Sprint());
        when(teamService.getTeamById(comment.teamId())).thenReturn(new Team());

        commentService.createComment(comment);

        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void deleteCommentShouldDeleteCommentWhenPermissionExistsAndCommentExists() {
        Integer id = 1;
        Comment comment = new Comment();

        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));

        commentService.deleteComment(id);

        verify(commentRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteAllCommentsByProjectShouldDeleteCommentsWhenPermissionExists() {
        Integer projectId = 1;

        commentService.deleteAllCommentsByProject(projectId);

        verify(commentRepository, times(1)).deleteAllByProject(projectId);
    }

    @Test
    void updateCommentShouldNotUpdateCommentWhenUpdatedCommentFieldsAreNull() {
        Integer id = 1;
        Comment updatedComment = new Comment();
        Comment existingComment = new Comment();
        existingComment.content("Existing Content");
        existingComment.feedback(false);

        when(commentRepository.findById(id)).thenReturn(Optional.of(existingComment));
        commentService.updateComment(id, updatedComment);

        assertEquals("Existing Content", existingComment.content());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

}


