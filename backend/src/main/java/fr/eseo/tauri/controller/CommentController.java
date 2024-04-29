package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Comment;
import fr.eseo.tauri.repository.CommentRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
@Tag(name = "comments")
public class CommentController {

    private final CommentRepository commentRepository;

    @PostMapping("/")
    public Comment addComment(@RequestBody Comment comment) {
        return commentRepository.save(comment);
    }

    @GetMapping("/")
    public Iterable<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Comment getCommentById(@PathVariable Integer id) {
        return commentRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Comment updateComment(@PathVariable Integer id, @RequestBody Comment commentDetails) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment != null) {
            comment.content(commentDetails.content());
            comment.feedback(commentDetails.feedback());
            return commentRepository.save(comment);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public String deleteComment(@PathVariable Integer id) {
        commentRepository.deleteById(id);
        return "Comment deleted";
    }
}
