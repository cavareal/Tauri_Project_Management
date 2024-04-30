package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Comment;
import fr.eseo.tauri.service.CommentService;
import fr.eseo.tauri.util.CustomLogger;
import fr.eseo.tauri.validator.comment.CreateCommentValidator;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
@Tag(name = "comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<Comment>> getAllComments(@RequestHeader("Authorization") String token) {
        List<Comment> comments = commentService.getAllComments(token);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        Comment comment = commentService.getCommentById(token, id);
        return ResponseEntity.ok(comment);
    }

    @PostMapping
    public ResponseEntity<String> addComment(@RequestHeader("Authorization") String token, @RequestBody CreateCommentValidator commentDetails) {
        commentService.addComment(token, commentDetails);
        CustomLogger.info("The comment has been created");
        return ResponseEntity.ok("The comment has been created");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateComment(@RequestHeader("Authorization") String token, @PathVariable Integer id, @RequestBody Map<String, Object> request) {
        commentService.updateComment(token, id, request);
        CustomLogger.info("The comment has been updated");
        return ResponseEntity.ok("The comment has been updated");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllComments(@RequestHeader("Authorization") String token) {
        commentService.deleteAllComments(token);
        CustomLogger.info("All the comments have been deleted");
        return ResponseEntity.ok("All the comments have been deleted");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        commentService.deleteComment(token, id);
        CustomLogger.info("The comment has been deleted");
        return ResponseEntity.ok("The comment has been deleted");
    }
}
