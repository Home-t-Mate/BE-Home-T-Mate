package com.example.demo.service;


import com.example.demo.config.S3Uploader;
import com.example.demo.dto.postsdto.PhotoResponseDto;
import com.example.demo.dto.postsdto.PostsDeleteRequestDto;
import com.example.demo.dto.commentdto.CommentUserDto;
import com.example.demo.dto.likedto.LikeUserDto;
import com.example.demo.dto.postsdto.PostCreateResponseDto;
import com.example.demo.dto.postsdto.PostResponseDto;
import com.example.demo.model.*;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.LikeRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.PhotoRepository;
import com.example.demo.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final S3Uploader s3Uploader;
    private final PhotoRepository photoRepository;


    // 게시글 전체 조회
    public List<PostResponseDto> getPost(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);

        List<PostResponseDto> postResponseDtos = new ArrayList<>();


        for (Post post : posts) {
            List<CommentUserDto> commentUserDtos = new ArrayList<>();
            List<LikeUserDto> likeUserDtos = new ArrayList<>();
            List<PhotoResponseDto> photoResponseDtos = new ArrayList<>();


            for (Like like : post.getLikes()) {
                LikeUserDto likeUserDto = new LikeUserDto(like);
                likeUserDtos.add(likeUserDto);
            }


            for (Comment comment : post.getComments()) {
                CommentUserDto commentUserDto = new CommentUserDto(comment);
                commentUserDtos.add(commentUserDto);
            }


            for (Photo photo : post.getPhotos()) {
                PhotoResponseDto photoResponseDto = new PhotoResponseDto(photo.getPostImg());
                photoResponseDtos.add(photoResponseDto);
            }


            PostResponseDto postResponseDto = new PostResponseDto(
                    post.getId(),
                    post.getUser().getId(),
                    post.getUser().getNickname(),
                    post.getContent(),
                    commentUserDtos,
                    likeUserDtos,
                    photoResponseDtos,
                    post.getUser().getProfileImg(),
                    post.getCreatedAt(),
                    post.getModifiedAt()
            );
            postResponseDtos.add(postResponseDto);
        }
        return postResponseDtos;
    }


    //내가 작성한 게시글 조회
    public List<PostResponseDto> getMyPosts(User user) {
        List<Post> posts = postRepository.findByUser(user);

        List<PostResponseDto> postResponseDtos = new ArrayList<>();


        for (Post post : posts) {
            List<CommentUserDto> commentUserDtos = new ArrayList<>();
            List<LikeUserDto> likeUserDtos = new ArrayList<>();
            List<PhotoResponseDto> photoResponseDtos = new ArrayList<>();

            for (Like like : post.getLikes()) {
                LikeUserDto likeUserDto = new LikeUserDto(like);
                likeUserDtos.add(likeUserDto);
            }

            for (Comment comment : post.getComments()) {
                CommentUserDto commentUserDto = new CommentUserDto(comment);
                commentUserDtos.add(commentUserDto);
            }


            for (Photo photo : post.getPhotos()) {
                PhotoResponseDto photoResponseDto = new PhotoResponseDto(photo.getPostImg());
                photoResponseDtos.add(photoResponseDto);
            }

            PostResponseDto postResponseDto = new PostResponseDto(
                    post.getId(),
                    post.getUser().getId(),
                    post.getUser().getNickname(),
                    post.getContent(),
                    commentUserDtos,
                    likeUserDtos,
                    photoResponseDtos,
                    post.getUser().getProfileImg(),
                    post.getCreatedAt(),
                    post.getModifiedAt()
            );
            postResponseDtos.add(postResponseDto);
        }
        return postResponseDtos;
    }

    // 게시글 작성
    @Transactional
    public PostCreateResponseDto createPost(String content, List<MultipartFile> multipartFile, User user) throws IOException {

        if (multipartFile == null) {
            throw new IllegalArgumentException("이미지를 넣어주세요.");
        }
        if (content == null) {
            throw new IllegalArgumentException("내용을 입력해주세요.");
        }
        if (content.length() > 600) {
            throw new IllegalArgumentException("600자 이하로 입력해주세요.");
        }
        Post post = postRepository.save(new Post(content, user));

        for (MultipartFile image : multipartFile) {
            String postImg = s3Uploader.upload(image, "static");
            Photo photo = new Photo(postImg, post);
            photoRepository.save(photo);
        }


        List<PhotoResponseDto> photoResponseDtos = new ArrayList<>();
        List<Photo> photos = photoRepository.findByPost(post);

        for (Photo photo : photos) {
            PhotoResponseDto photoResponseDto = new PhotoResponseDto(photo.getPostImg());
            photoResponseDtos.add(photoResponseDto);
        }

        PostCreateResponseDto postCreateResponseDto = new PostCreateResponseDto(
                post.getId(),
                user.getId(),
                user.getProfileImg(),
                user.getNickname(),
                content,
                photoResponseDtos,
                post.getCreatedAt(),
                post.getModifiedAt()
        );
        return postCreateResponseDto;
    }

    //게시글 수정
    @Transactional
    public void updatePost(List<MultipartFile> multipartFile, String content, Long postId, User user) throws IOException {

        Post post = postRepository.getById(postId);

        if (!post.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }
        if (post == null) {
            throw new IllegalArgumentException("해당 게시물이 존재하지 않습니다.");
        }
        photoRepository.deleteByPost(post);

        post.update(content, user);
        for (MultipartFile image : multipartFile) {
            String postImg = s3Uploader.upload(image, "static");
            Photo photo = new Photo(postImg, post);
            photoRepository.save(photo);
        }
    }


    //게시글 삭제
    @Transactional
    public Long deletePost(Long postId, UserDetailsImpl userDetails) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        User user = post.getUser();
        Long deleteId = user.getId();
        if (!Objects.equals(userDetails.getUser().getId(), deleteId)) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }
//        List<Comment> comments = commentRepository.findAllByPost(post);
//        for (Comment comment : comments) {
//            commentRepository.deleteById(comment.getId());
//        }

//        likeRepository.deleteByPost(post);
        postRepository.delete(post);
//        photoRepository.deleteByPost(post);
        return postId;
    }

    @Transactional
    public void deletePosts(List<PostsDeleteRequestDto> postsDeleteRequestDtos, UserDetailsImpl userDetails) {

        for (PostsDeleteRequestDto postsDeleteRequestDto : postsDeleteRequestDtos) {
            Long postId = postsDeleteRequestDto.getPostId();
            Post post = postRepository.findById(postId).orElseThrow(
                    () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
            );

            User user = post.getUser();
            Long deleteId = user.getId();
            if (!Objects.equals(userDetails.getUser().getId(), deleteId)) {
                throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
            }

            postRepository.delete(post);

        }
    }
}