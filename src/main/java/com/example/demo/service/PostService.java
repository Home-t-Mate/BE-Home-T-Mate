package com.example.demo.service;


import com.example.demo.config.S3Uploader;
import com.example.demo.dto.PotoResponseDto;
import com.example.demo.dto.commentdto.CommentUserDto;
import com.example.demo.dto.likedto.LikeUserDto;
import com.example.demo.dto.postsdto.PostRequestDto;
import com.example.demo.dto.postsdto.PostResponseDto;
import com.example.demo.model.*;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.LikeRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.PotoRepository;
import com.example.demo.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
//import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final S3Uploader s3Uploader;
    private final PotoRepository potoRepository;

//    private final S3Uploader s3Uploader;
//    private final String imageDirName = "posts";
//    private final LikesRepository likesRepository;
//    private final CommentRepository commentRepository;
//    private final UserRepository userRepository;
//    private final PhotoRepository photoRepository;


    // 게시글 전체 조회
    public List<PostResponseDto> getPost() {

        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();

        List<PostResponseDto> postResponseDtos = new ArrayList<>();


        for (Post post : posts)
        {

            List<CommentUserDto> commentUserDtos = new ArrayList<>();
            List<LikeUserDto> likeUserDtos = new ArrayList<>();
            List<PotoResponseDto> potoResponseDtos = new ArrayList<>();


            Long commentCount = commentRepository.countByPost(post);
            Long likeCount = likeRepository.countByPost(post);

            List<Like> likes = likeRepository.findAllByPost(post);
            List<Comment> comments = commentRepository.findAllByPost(post);

            List<Poto> potos = potoRepository.findByPost(post);


            for (Like like : likes)
            {
                LikeUserDto likeUserDto = new LikeUserDto(like);
                likeUserDtos.add(likeUserDto);
            }

            for (Comment comment : comments) {
                CommentUserDto commentUserDto = new CommentUserDto(comment);
                commentUserDtos.add(commentUserDto);
            }

            for (Poto poto : potos) {
                PotoResponseDto potoResponseDto = new PotoResponseDto(poto.getPostImg());
                potoResponseDtos.add(potoResponseDto);
            }



            PostResponseDto postResponseDto = new PostResponseDto(
                    post.getId(),
                    post.getUser().getId(),
                    post.getUser().getNickname(),
                    post.getContent(),
                    post.getPostImg(),
                    commentCount,
                    likeCount,
                    commentUserDtos,
                    likeUserDtos,
                    potoResponseDtos,
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
    public Post createPost(String content, List<MultipartFile> multipartFile, User user) throws IOException {

//        if (postRequestDto.getPostImg() == null) {
//            throw new IllegalArgumentException("이미지를 넣어주세요.");
//        }
//
//        System.out.println("1차 break");
//        String content = postRequestDto.getContent();
//        if (postRequestDto.getContent() == null) {
//            throw new IllegalArgumentException("내용을 입력해주세요.");
//        }
//        if (content.length() > 600) {
//            throw new IllegalArgumentException("600자 이하로 입력해주세요.");
//        }
//        Post post = new Post(content, user);
        Post post = postRepository.save(new Post(content, user));

        for(MultipartFile image : multipartFile) {
            String postImg = s3Uploader.upload(image, "static");
            Poto poto = new Poto(postImg, post);
            potoRepository.save(poto);
        }
        return postRepository.save(post);
    }

//    // 게시글 작성
//    @Transactional
//    public Post createPost(PostRequestDto postRequestDto, User user) {
//
//        if (postRequestDto.getPostImg() == null) {
//            throw new IllegalArgumentException("이미지를 넣어주세요.");
//        }
//
//        System.out.println("1차 break");
//        String content = postRequestDto.getContent();
//        if (postRequestDto.getContent() == null) {
//            throw new IllegalArgumentException("내용을 입력해주세요.");
//        }
//        if (content.length() > 600) {
//            throw new IllegalArgumentException("600자 이하로 입력해주세요.");
//        }
//
//        System.out.println("2차");
//
//        String postImg = postRequestDto.getPostImg();
//        Post post = new Post(content ,postImg, user);
//
//        System.out.println(post);
//        return postRepository.save(post);
//    }


    // 게시글 수정
//    @Transactional
//    public PostResponseDto updatePost(List<MultipartFile> images,
//                                          PostRequestDto requestDto,
//                                          Long postId,
//                                          User user
//    ) {
//        Post post = postRepository.findById(postId).orElseThrow(
//                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다")
//        );
//
//        if (!user.getId().equals(post.getUser().getId())) {
//            throw new IllegalArgumentException("게시글을 작성한 유저만 수정이 가능합니다.");
//        }
//
//        // 삭제할 사진 아이디 리스트
//        List<Long> photoIdList = requestDto.getPhotoIdList();
//
//        // 게시물에 저장돼있던 사진 객체 리스트
//        List<Photo> photoList = photoRepository.findAllByPost(post);
//
//        for (Photo photo : photoList) {
//            if (photoIdList.contains(photo.getId())) {
//                try {
//                    String oldImageUrl = URLDecoder.decode(photo.getFilePath().replace("https://skifriendbucket.s3.ap-northeast-2.amazonaws.com/", ""), "UTF-8");
//                    s3Uploader.deleteFromS3(oldImageUrl);
//                    photoRepository.deleteById(photo.getId());
//                } catch (Exception ignored) {
//                }
//            }
//        }
//        savePhotoList(images, post);
//        post.update(requestDto);
//        return generatePostResponseDto(post);
//    }

//    @Transactional
//    public ResponseEntity<?> updatePost(Long id, PutRequestDto postRequestDto, UserDetailsImpl userDetails)
//    {
//        Post post = postRepository.findById(id).orElseThrow(
//                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
//        );
//
//        User user = post.getUser();
//
//        System.out.println(userDetails.getUser().getId());
//        System.out.println(user.getId());
//
//        if (!userDetails.getUser().getId().equals(user.getId()))
//        {
//            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
//        }
//
//        if (postRequestDto.getContent() == null) {
//            throw new IllegalArgumentException("내용을 입력해주세요.");
//        }
//        if (postRequestDto.getContent().length() > 600) {
//            throw new IllegalArgumentException("600자 이하로 입력해주세요.");
//        }
//        post.UpdatePost(postRequestDto);
//
//        // 기존에 있던 이미지 파일 S3에서 삭제
//        for ( PostFile deleteS3 : post.getFileList())
//        {
//            String[] fileKey = deleteS3.getUrl().split("static/");
//            try
//            {
//                String decodeKey = URLDecoder.decode(fileKey[1], "UTF-8");
//                s3Uploader.deleteS3("static/" + decodeKey);
//            }
//            catch (UnsupportedEncodingException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        // 기존에 있던 포스트파일 제거
//        postFileRepository.deleteByPost(post);
//
//        // 새로운 이미지 파일 url 배열로 for 반복문을 실행
//        for ( String url : postRequestDto.getFile())
//        {
//            // 이미지 파일 url로 postFile 객체 생성
//            PostFile fileUrl = new PostFile(url);
//
//            // postFile에 savePost를 연관관계 설정
//            fileUrl.setPost(post);
//
//            // 이미지 파일 url 1개에 해당되는 postFile을 DB에 저장
//            PostFile saveFile = postFileRepository.save(fileUrl);
//
//            // 저장된 postFile을 저장된 post에 한개씩 추가함
//            post.getFileList().add(saveFile);
//        }
//
//        return ResponseEntity.ok().body(new BasicResponseDto("true"));
//    }


    //게시글 삭제
    @Transactional
    public Long deletePost(Long postId, UserDetailsImpl userDetails) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        User user = post.getUser();
        Long deleteId = user.getId();
        if (!Objects.equals(userDetails.getUser().getId(), deleteId)) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }
        List<Comment> comments = commentRepository.findAllByPost(post);
        for (Comment comment : comments) {
            commentRepository.deleteById(comment.getId());
        }
        likeRepository.deleteByPost(post);
        postRepository.deleteById(postId);
        return postId;
    }

}