package com.example.smartshop.specification;


import com.example.smartshop.model.User;
import com.example.smartshop.model.enums.Role;
import org.springframework.data.jpa.domain.Specification;


public class UserSpec {
    public static Specification<User> hasUsername(String username) {
        return (root, query, cb) ->
                username == null ? null : cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%");
    }

    public static Specification<User> hasEmail(String email) {
        return ((root, query, cb) ->
                email == null ? null : cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
    }


    public static Specification<User> hasRole(String role) {
        return (root, query, cb) ->
                role == null || role.isBlank() ? null : cb.equal(root.get("role"), Role.valueOf(role.toUpperCase()));
    }


}
