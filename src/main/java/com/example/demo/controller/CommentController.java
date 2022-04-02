package com.example.demo.controller;

import com.example.demo.dto.commentdto.CommentIdDto;
import com.example.demo.dto.commentdto.CommentRequestDto;
import com.example.demo.dto.commentdto.CommentResponseDto;
import com.example.demo.model.Comment;
import com.example.demo.model.Response;
import com.example.demo.security.UserDetailsImpl;
import com.example.demo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    // 댓글 조회
    @GetMapping("/api/comments/{postId}")
    public List<CommentResponseDto> getComment(@PathVariable Long postId)
    {
        return commentService.getComment(postId);
    }

    // 댓글 작성.
    @PostMapping("/api/comments/{postId}")
    public CommentIdDto createComment(
            @PathVariable Long postId,
            @Validated @RequestBody CommentRequestDto requestDto,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return commentService.createComment(postId,requestDto,userDetails,bindingResult);
    }

    // 댓글 삭제
    @DeleteMapping("/api/comments/{postId}/{commentId}")
    public Response deleteComment(@PathVariable Long commentId,
                                  @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        commentService.deleteComment(commentId, userDetails);
        Response response = new Response();
        response.setResult(true);
        return response;
    }
}