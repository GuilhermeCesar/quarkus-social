package me.guilherme.quarkussocial.rest.dto;

import lombok.Data;

import java.util.List;

@Data
public class FollowersPerUserResponse {

    private Integer followersCouunt;
    private List<FollowerResponse> content;

}
