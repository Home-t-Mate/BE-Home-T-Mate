package com.example.demo.service;

import com.example.demo.dto.commentdto.CommentIdDto;
import com.example.demo.dto.commentdto.CommentRequestDto;
import com.example.demo.dto.commentdto.CommentResponseDto;
import com.example.demo.model.Comment;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    //댓글 작성
    @Transactional
    public CommentIdDto createComment(
            Long postId,
            CommentRequestDto requestDto,
            UserDetailsImpl userDetails,
            BindingResult bindingResult) {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당하는 게시글을 찾을 수 없습니다.")
        );
        User user = userDetails.getUser();

        if (bindingResult.hasErrors()){
            throw  new IllegalArgumentException(bindingResult.getFieldError().getDefaultMessage());
        } else {
            Comment comment = new Comment(requestDto,post,user);
            Long Id = commentRepository.save(comment).getId();
            return new CommentIdDto(Id);
        }
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, UserDetailsImpl userDetails) {
        commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );
        User user = commentRepository.findById(commentId).get().getUser();
        Long commentUserId = user.getId();
        Long loginUserId = userDetails.getUser().getId();
        if (!Objects.equals(commentUserId, loginUserId)){
            throw new IllegalArgumentException("작성자만 삭제 할 수 있습니다.");
        } else {
            commentRepository.deleteById(commentId);
        }
    }

    //댓글 조회
    @ResponseBody
    public List<CommentResponseDto> getComment(Long postId) {
        List<CommentResponseDto> responseDto = new ArrayList<>();
        List<Comment> test = commentRepository.findByPostIdOrderByModifiedAtDesc(postId);

        for (Comment co : test) {
            CommentResponseDto commentResponseDto = new CommentResponseDto(co);
            responseDto.add(commentResponseDto);
        }
        return responseDto;
    }
}