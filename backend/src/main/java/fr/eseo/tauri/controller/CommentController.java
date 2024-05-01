package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Comment;
import fr.eseo.tauri.service.CommentService;
import fr.eseo.tauri.util.CustomLogger;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
@Tag(name = "comments")
public class CommentController {

    private final CommentService commentService;


    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        Comment comment = commentService.getCommentById(token, id);
        return ResponseEntity.ok(comment);
    }

    @GetMapping
    public ResponseEntity<List<Comment>> getAllComments(@RequestHeader("Authorization") String token, @RequestParam("projectId") Integer projectId) {
        List<Comment> comments = commentService.getAllComments(token, projectId);
        return ResponseEntity.ok(comments);
    }

    /*@PostMapping
    public ResponseEntity<String> addComment(@RequestHeader("Authorization") String token, @RequestBody CreateCommentValidator commentDetails) {
        commentService.addComment(token, commentDetails);
        CustomLogger.info("The comment has been created");
        return ResponseEntity.ok("The comment has been created");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateComment(@RequestHeader("Authorization") String token, @PathVariable Integer id, @RequestBody UpdateCommentValidator commentDetails) {
        //commentService.updateComment(token, id, commentDetails);
        CustomLogger.info("The comment has been updated");
        return ResponseEntity.ok("The comment has been updated");
    }*/

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        commentService.deleteComment(token, id);
        CustomLogger.info("The comment has been deleted");
        return ResponseEntity.ok("The comment has been deleted");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllComments(@RequestHeader("Authorization") String token, @RequestParam("projectId") Integer projectId) {
        commentService.deleteAllComments(token, projectId);
        CustomLogger.info("All the comments have been deleted");
        return ResponseEntity.ok("All the comments have been deleted");
    }
}
