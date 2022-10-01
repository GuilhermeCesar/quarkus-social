package me.guilherme.quarkussocial.rest;

import me.guilherme.quarkussocial.domain.model.Follower;
import me.guilherme.quarkussocial.domain.repository.FollowerRepository;
import me.guilherme.quarkussocial.domain.repository.UserRepository;
import me.guilherme.quarkussocial.rest.dto.FollowerRequest;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {

    private FollowerRepository repository;
    private UserRepository userRepository;

    @Inject
    public FollowerResource(FollowerRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Transactional
    @PUT
    public Response followUser(@PathParam("userId") Long userId, FollowerRequest request) {
        var user = userRepository.findById(userId);

        if (userId.equals(request.getFollowerId())) {
            return Response
                    .status(Response.Status.CONFLICT)
                    .entity("You can't follow yourself")
                    .build();
        }

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        var follower = userRepository.findById(request.getFollowerId());

        boolean follow = repository.follows(follower, user);

        if (!follow) {
            var entity = new Follower();
            entity.setUser(user);
            entity.setFollower(follower);

            repository.persist(entity);
        }

        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }
}
