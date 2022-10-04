package me.guilherme.quarkussocial.rest;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import me.guilherme.quarkussocial.domain.model.Follower;
import me.guilherme.quarkussocial.domain.model.Post;
import me.guilherme.quarkussocial.domain.model.User;
import me.guilherme.quarkussocial.domain.repository.FollowerRepository;
import me.guilherme.quarkussocial.domain.repository.PostRepository;
import me.guilherme.quarkussocial.domain.repository.UserRepository;
import me.guilherme.quarkussocial.rest.dto.CreatePostRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@TestHTTPEndpoint(PostResource.class)
@QuarkusTest
class PostResourceTest {

    @Inject
    UserRepository userRepository;
    @Inject
    FollowerRepository followerRepository;
    @Inject
    PostRepository postRepository;

    Long userId;
    Long userNotFollowerId;
    Long userFollowerId;

    @Transactional
    @BeforeEach
    public void setUp() {
        //usuario padrao dos testes
        var user = new User();
        user.setAge(30);
        user.setName("Fulano");

        userRepository.persist(user);
        userId = user.getId();

//        criada a postagem para o usuario
        Post post = new Post();
        post.setText("Hello");
        post.setUser(user);
        postRepository.persist(post);


        //usuario que nao segue ninguemn
        var userNotFollower = new User();
        userNotFollower.setAge(33);
        userNotFollower.setName("Cicrano");
        userRepository.persist(userNotFollower);
        userNotFollowerId = userNotFollower.getId();


        //usuario seguidor
        var usefollower = new User();
        usefollower.setAge(31);
        usefollower.setName("Terceiro");
        userRepository.persist(usefollower);
        userFollowerId = usefollower.getId();

        Follower follower = new Follower();
        follower.setUser(user);
        follower.setFollower(usefollower);
        followerRepository.persist(follower);



    }

    @Test
    @DisplayName("should create a post for a user")
    void createPostTest() {
        var postRequest = new CreatePostRequest();
        postRequest.setText("Some text");

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParams("userId", userId)
                .when()
                .post()
                .then()
                .statusCode(201);
    }


    @Test
    @DisplayName("should return 404 when trying  a post for an inexistent user ")
    void postForAninexistentUserTest() {
        var postRequest = new CreatePostRequest();
        postRequest.setText("Some text");

        var inexistUserId = 999;

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParams("userId", inexistUserId)
                .when()
                .post()
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Should return 404 when user doesn't exist")
    void listPostUserNotFoundTest(){
        var inexistentUserId = 999;

        given()
                .pathParams("userId", inexistentUserId)
                .when()
                .get()
                .then()
                .statusCode(404);
    }


    @Test
    @DisplayName("Should return 400 followerId header is not present")
    void listPostFollowerHeaderNotSendTest(){
        given()
                .pathParams("userId", userId)
                .when()
                .get()
                .then()
                .statusCode(400)
                .body(Matchers.is("You forgot the header followerId"));
    }

    @Test
    @DisplayName("Should return 400 when follower doesn't exist")
    void listPostFollowerNotFoundTest(){
        var inexistentFolloweId = 999;

        given()
                .pathParams("userId", userId)
                .header("followerId", inexistentFolloweId)
                .when()
                .get()
                .then()
                .statusCode(400)
                .body(Matchers.is("Inexistent followerId"));
    }


    @Test
    @DisplayName("Should return 403 when follower isn't a follower")
    void listPostNotAFollower(){
        given()
                .pathParams("userId", userId)
                .header("followerId", userNotFollowerId)
                .when()
                .get()
                .then()
                .statusCode(403)
                .body(Matchers.is("You can't see these posts"));
    }

    @Test
    @DisplayName("Should return posts")
    void listPostTest(){
        given()
                .pathParams("userId", userId)
                .header("followerId", userFollowerId)
                .when()
                .get()
                .then()
                .statusCode(200);
    }

}