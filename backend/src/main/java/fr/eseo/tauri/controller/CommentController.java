package fr.eseo.tauri.controller;

import fr.eseo.tauri.model.Comment;
import fr.eseo.tauri.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @PostMapping("/add")
    public Comment addComment(@RequestBody Comment comment) {
        return commentRepository.save(comment);
    }

    @GetMapping("/all")
    public Iterable<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Comment getCommentById(@PathVariable Integer id) {
        return commentRepository.findById(id).orElse(null);
    }

    @PutMapping("/update/{id}")
    public Comment updateComment(@PathVariable Integer id, @RequestBody Comment commentDetails) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment != null) {
            comment.content(commentDetails.content());
            comment.feedback(commentDetails.feedback());
            return commentRepository.save(comment);
        }
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteComment(@PathVariable Integer id) {
        commentRepository.deleteById(id);
        return "Comment deleted";
    }
}
