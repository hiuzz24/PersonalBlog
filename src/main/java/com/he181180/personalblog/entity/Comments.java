package com.he181180.personalblog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "comments")
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private int commentID;

    @Column(name = "content")
    private String content;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "post_id",nullable = false)
    private Posts posts;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private Users users;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comments parentComment;

    @OneToMany(mappedBy = "parentComment",cascade = CascadeType.ALL)
    private List<Comments> replies;
}
