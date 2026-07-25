package com.store.backend.dto;

import lombok.*;
import java.util.Set;

@Data @Builder
public class AuthResponse {
    private String token;
    private Long id;
    private String name;
    private String email;
    private Set<String> roles;
}