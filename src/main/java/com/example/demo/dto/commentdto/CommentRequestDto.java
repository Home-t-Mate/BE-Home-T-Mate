package com.example.demo.dto.commentdto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentRequestDto {

    @Size(min = 1,max = 300, message = "댓글은 300자 이하로 입력해 주세요!")
    @NotBlank(message = "댓글을 입력해주세요.")
    private String comment;

}
