package fr.eseo.tauri.service;

import fr.eseo.tauri.exception.GlobalExceptionHandler;
import fr.eseo.tauri.model.Comment;
import fr.eseo.tauri.exception.ResourceNotFoundException;
import fr.eseo.tauri.repository.CommentRepository;
import fr.eseo.tauri.repository.SprintRepository;
import fr.eseo.tauri.repository.TeamRepository;
import fr.eseo.tauri.repository.UserRepository;
import fr.eseo.tauri.validator.comment.CreateCommentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final AuthService authService;
    private final CommentRepository commentRepository;
    private final TeamRepository teamRepository;
    private final SprintRepository sprintRepository;
    private final UserRepository userRepository;

    public Comment getCommentById(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readComment"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("comment", id));
    }

    public List<Comment> getAllComments(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "readComments"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        return commentRepository.findAllByProject(projectId);
    }

    public void addComment(String token, CreateCommentValidator commentDetails) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "addComment"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        int commentsNumber = commentRepository.findAll().size();
            //commentRepository.save(commentDetails);
            if(commentRepository.findAll().size() == commentsNumber){
                //throw new DataAccessException("Error : Could not add comment written by " + comment.author().name()) {};
        }
    }

    public void updateComment(String token, Integer id, Map<String, Object> commentDetails) {
        if (Boolean.TRUE.equals(authService.checkAuth(token, "updateComment"))) {
            Comment comment = getCommentById(token, id);

            for (Map.Entry<String, Object> entry : commentDetails.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value == null) {
                    continue;
                }

                switch (key) {
                    case "content":
                        comment.content((String) value);
                        break;
                    case "feedback":
                        comment.feedback((Boolean) value);
                        break;
                    case "team":
                        Map<String, Object> teamMap = (Map<String, Object>) value;
                        comment.team(teamRepository.findById((Integer) teamMap.get("id")).orElseThrow(() -> new ResourceNotFoundException("team", (Integer) teamMap.get("id"))));
                        break;
                    case "sprint": //à changer si on change le schéma des clés étrangères dans le front pour n'envoyer que l'id de l'objet et pas l'objet tout entier
                        Map<String, Object> sprintMap = (Map<String, Object>) value;
                        comment.sprint(sprintRepository.findById((Integer) sprintMap.get("id")).orElseThrow(() -> new ResourceNotFoundException("sprint", (Integer) sprintMap.get("id"))));
                        break;
                    case "author":
                        Map<String, Object> userMap = (Map<String, Object>) value;
                        comment.author(userRepository.findById((Integer) userMap.get("id")).orElseThrow(() -> new ResourceNotFoundException("user", (Integer) userMap.get("id"))));
                        break;
                    // Add more cases here if you have more fields in the Comment class that can be updated
                    default:
                        throw new IllegalArgumentException("Invalid key: " + key);
                }
            }
            commentRepository.save(comment);

        } else {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
    }

    public void deleteComment(String token, Integer id) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteComment"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        getCommentById(token, id);
        int commentsNumber = commentRepository.findAll().size();
        commentRepository.deleteById(id);
        if(commentRepository.findAll().size() == commentsNumber){
            throw new DataAccessException("Error : Could not delete comment with id : " + id) {};
        }
    }

    public void deleteAllComments(String token, Integer projectId) {
        if (!Boolean.TRUE.equals(authService.checkAuth(token, "deleteComment"))) {
            throw new SecurityException(GlobalExceptionHandler.UNAUTHORIZED_ACTION);
        }
        commentRepository.deleteAllByProject(projectId);
    }
}
