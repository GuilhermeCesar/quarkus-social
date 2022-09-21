package me.guilherme.quarkussocial.domain.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import me.guilherme.quarkussocial.domain.model.Post;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PostRepository  implements PanacheRepository<Post> {
}
