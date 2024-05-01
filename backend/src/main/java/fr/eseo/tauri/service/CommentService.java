package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.model.Comment;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final AuthService authService;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final TeamService teamService;
    private final SprintService sprintService;

    public Comment getCommentById(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readComment"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("comment", id));
    }

    public List<Comment> getAllCommentsByProject(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readComments"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return commentRepository.findAllByProject(projectId);
    }

    public void createComment(String token, Comment comment) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addComment"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        comment.author(userService.getUserById(token, comment.authorId()));
        comment.sprint(sprintService.getSprintById(token, comment.sprintId()));
        comment.team(teamService.getTeamById(token, comment.teamId()));

        commentRepository.save(comment);
    }

    public void updateComment(String token, Integer id, Comment updatedComment) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "updateComment"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }

        Comment comment = getCommentById(token, id);

        if (updatedComment.content() != null) comment.content(updatedComment.content());
        if (updatedComment.feedback() != null) comment.feedback(updatedComment.feedback());
        if (updatedComment.sprintId() != null) comment.sprint(sprintService.getSprintById(token, updatedComment.sprintId()));
        if (updatedComment.authorId() != null) comment.author(userService.getUserById(token, updatedComment.authorId()));
        if (updatedComment.teamId() != null) comment.team(teamService.getTeamById(token, updatedComment.teamId()));

        commentRepository.save(comment);
    }

    public void deleteComment(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteComment"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        getCommentById(token, id);
        commentRepository.deleteById(id);
    }

    public void deleteAllCommentsByProject(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteComment"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        commentRepository.deleteAllByProject(projectId);
    }
}