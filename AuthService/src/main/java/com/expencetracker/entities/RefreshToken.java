package com.expencetracker.entities;

import jakarta.persistence.*;
import lombok.*;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Table(name = "tokens")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String token;
    private Instant expiryDate;
    @OneToOne
//    @JoinColumn(name = "id", referencedColumnName = "user_id")
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, unique = true)
    private UserInfo userInfo;

    @Override
    public String toString() {
        return "RefreshToken(id=" + id + ", token=" + token + ", expiryDate=" + expiryDate + ")";
    }
}
