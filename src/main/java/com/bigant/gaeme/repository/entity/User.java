package com.bigant.gaeme.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(indexes = {@Index(name = "idx_oauth_id", columnList = "oauthId")})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private String name;

    private String address;

    private String phoneNumber;

    private String email;

    private String oauthId;

}
