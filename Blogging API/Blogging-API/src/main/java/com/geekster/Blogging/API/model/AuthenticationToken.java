package com.geekster.Blogging.API.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;
    private String tokenValue;
    private LocalDateTime timeStamp;
    @OneToOne
    @JoinColumn(name = "fk_UserId")
    private User user;
    public AuthenticationToken(User user)
    {
        this.user=user;
        this.tokenValue= UUID.randomUUID().toString();
        this.timeStamp=LocalDateTime.now();
    }


}
