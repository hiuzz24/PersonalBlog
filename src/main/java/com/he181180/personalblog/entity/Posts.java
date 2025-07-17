package com.he181180.personalblog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "posts")
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private int postID;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "image_url")
    private String imageUrl;

    @Lob
    @Column(name = "body",columnDefinition = "text")
    private String body;

    @Column(name = "is_published",insertable = false)
    private boolean isPublished;

    @Column(name = "published_at",insertable = false)
    private Timestamp publishedAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private Users users;

    @ManyToMany
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tags> tags;

    @OneToMany(mappedBy = "posts",cascade = CascadeType.ALL)
    private List<Comments> comments;
}
