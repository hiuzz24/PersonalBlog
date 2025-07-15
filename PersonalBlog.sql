
DROP DATABASE IF EXISTS PersonalBlog;
CREATE DATABASE PersonalBlog;
USE PersonalBlog;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    full_name VARCHAR(100),
    bio TEXT,
    avatar_url VARCHAR(255),
    role VARCHAR(50) DEFAULT 'writer',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);


CREATE TABLE posts (
    post_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    is_published BOOLEAN DEFAULT TRUE,
    published_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at DATE,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE tags (
    tag_id INT AUTO_INCREMENT PRIMARY KEY,
    tag_name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE post_tags (
    tag_id INT NOT NULL,
    post_id INT NOT NULL,
    PRIMARY KEY (tag_id, post_id),
    FOREIGN KEY (tag_id) REFERENCES tags(tag_id),
    FOREIGN KEY (post_id) REFERENCES posts(post_id)
);

CREATE TABLE comments (
    comment_id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT NOT NULL,
    user_id INT NOT NULL,
    content TEXT NOT NULL,
    parent_comment_id INT,
    is_deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (post_id) REFERENCES posts(post_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (parent_comment_id) REFERENCES comments(comment_id)
);

INSERT INTO users (username, email, password, full_name, bio, avatar_url, role, is_deleted)
VALUES
    ('admin', 'admin@blog.vn', 'admin123', 'Quản Trị Viên', 'Admin of the blog.', 'https://example.com/avatar_admin.png', 'admin', FALSE),
    ('linhtran', 'linhtran@blog.vn', 'linh123', 'Trần Thị Linh', 'Loves travel and food blogging.', 'https://example.com/avatar_linh.png', 'writer', FALSE),
    ('nguyenvananh', 'vananh@blog.vn', 'anh123', 'Nguyễn Văn Anh', 'Passionate about programming and Java.', 'https://example.com/avatar_anh.png', 'writer', FALSE);

INSERT INTO posts (user_id, title, content, is_published, published_at, updated_at)
VALUES
    (1, 'Welcome to the Blog', 'This is the first post by the admin.', TRUE, CURRENT_DATE, CURRENT_DATE),
    (2, 'Top 5 Destinations in Vietnam', 'Explore the most beautiful places across the country.', TRUE, CURRENT_DATE, CURRENT_DATE),
    (3, 'Java Programming Guide', 'Learn how to build Java applications using Spring Boot.', TRUE, CURRENT_DATE, CURRENT_DATE);

INSERT INTO tags (tag_name)
VALUES
    ('travel'),
    ('food'),
    ('programming'),
    ('java'),
    ('technology');

INSERT INTO post_tags (post_id, tag_id)
VALUES
    (1, 5),
    (2, 1),
    (2, 2),
    (3, 3),
    (3, 4);

INSERT INTO comments (post_id, user_id, content, parent_comment_id)
VALUES
    (2, 3, 'Thanks for the recommendations! I love Da Nang.', NULL),
    (3, 2, 'Great tutorial. Java is awesome!', NULL),
    (3, 3, 'Could you share more Spring Boot resources?', 2);
