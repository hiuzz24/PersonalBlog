package com.he181180.personalblog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tags")
public class Tags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private int tagID;

    @Column(name = "tag_name")
    private String tagName;

    @ManyToMany(mappedBy = "tags",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Posts> posts;
}
