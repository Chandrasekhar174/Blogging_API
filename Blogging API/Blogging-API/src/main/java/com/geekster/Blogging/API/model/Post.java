package com.geekster.Blogging.API.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.geekster.Blogging.API.model.Enum.PostType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    private String postContent;
    private String postCaption;
    private String postLocation;
    private PostType postType;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime postTime;
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JoinColumn(name = "fk_userId")
    private User postOwner;

}
