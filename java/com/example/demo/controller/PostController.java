package com.example.demo.controller;

import com.example.demo.config.S3Uploader;
import com.example.demo.dto.ContentRequestDto;
import com.example.demo.dto.postsdto.PostRequestDto;
import com.example.demo.dto.postsdto.PostResponseDto;
import com.example.demo.model.Response;
import com.example.demo.model.User;
import com.example.demo.security.UserDetailsImpl;
import com.example.demo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final S3Uploader s3Uploader;
    private final PostService postService;


    // 게시글 전체 조회
    @GetMapping("/api/posts")
    public List<PostResponseDto> getPost() {
        return postService.getPost();
    }


    // 게시글 작성
    @PostMapping("/api/posts")
    public Response createpost(@RequestPart(value = "content") String content,
                               @RequestPart(value = "imageUrl", required = false) MultipartFile multipartFile,
                               @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException
    {
        System.out.println(content);

        System.out.println(content.getClass().getName());

        String postImg = s3Uploader.upload(multipartFile, "static");
        System.out.println(postImg);
        User user = userDetails.getUser();

        PostRequestDto dto = new PostRequestDto(postImg, content);

        System.out.println("content: " + content);
        System.out.println("postImg: " + postImg);
        System.out.println("1차 통과");

        postService.createPost(dto, user);


        Response response = new Response();
        response.setResult(true);
        return response;
    }


//    public void createpost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails)
//    {
//        User user = userDetails.getUser();
//        postService.createPost(requestDto, user);
//
//    }
//

    // 게시글 수정
//    @PutMapping("api/posts/{postId}")
//    public ResponseEntity<PostResponseDto> updatePost(@RequestPart(value = "image", required = false) List<MultipartFile> image,
//                                                      @RequestPart(value = "requestDto", required = false) PostRequestDto requestDto,
//                                                      @PathVariable Long postId,
//                                                      @AuthenticationPrincipal UserDetailsImpl userDetails
//    ) {
//        User user = userDetails.getUser();
//        return ResponseEntity.ok().body(postService.updatePost(image, requestDto, postId, user));
//    }

//    @PutMapping("api/posts/{postId}")
//    public boolean updatePost(@PathVariable Long postId,
//                               @RequestBody PostRequestDto postRequestDto,
//                               @AuthenticationPrincipal UserDetailsImpl userDetails){
//        return postService.updatePost(postId, postRequestDto, userDetails);
//    }

    @PutMapping("/api/posts/{postId}")
    public Long updatePost(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.updatePost(postId, userDetails, postRequestDto);
        return postId;
    }


    // 게시글 삭제
    @DeleteMapping("/api/posts/{postId}")
    public Response deletePost(@PathVariable Long postId,
                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.deletePost(postId, userDetails);

        Response response = new Response();
        response.setResult(true);
        return response;
    }
}
