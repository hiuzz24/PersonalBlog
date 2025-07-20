package com.he181180.personalblog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
    private int commentId;

    @Column(name = "content")
    private String content;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "post_id",nullable = false)
    private Posts post;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comments parentComment;

    @OneToMany(mappedBy = "parentComment",cascade = CascadeType.ALL)
    private ArrayList<Comments> children ;

}
