package me.guilherme.quarkussocial.rest.dto;

import lombok.Data;
import me.guilherme.quarkussocial.domain.model.Post;

import java.time.LocalDateTime;

@Data
public class PostResponse {

    private String text;
    private LocalDateTime dateTime;


    public static PostResponse fromEntity(Post post){
        var postresponse = new PostResponse();
        postresponse.setText(post.getText());
        postresponse.setDateTime(post.getDateTime());
        return postresponse;
    }
}
