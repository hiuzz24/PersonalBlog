package com.he181180.personalblog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "posts")
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

    @Lob
    @Column(name = "image_url",columnDefinition = "text")
    private String imageUrl;

    @Lob
    @Column(name = "body",columnDefinition = "mediumtext")
    private String body;

    @Column(name = "is_published")
    private boolean published;

    @Column(name = "is_deleted")
    private Boolean deleted;

    @Column(name = "published_at")
    private Timestamp publishedAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "status")
    private String status;

    @Column(name = "reason_rejected")
    private String reasonRejected;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private Users users;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @JsonIgnore
    private List<Tags> tags;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<Comments> comments;
}
