package me.guilherme.quarkussocial.domain.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import me.guilherme.quarkussocial.domain.User;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
}
