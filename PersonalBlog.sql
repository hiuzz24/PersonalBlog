DROP SCHEMA public CASCADE;

Create SCHEMA public;

create table users(
user_id serial primary key,
username varchar(50) unique not null,
email varchar (50) unique not null,
password varchar(50) not null,
full_name varchar(100),
bio text,
avatar_url varchar(255),
role varchar(50) default 'writer',
created_at timestamp default current_timestamp,
is_deleted boolean default false
);


create table posts(
post_id serial primary key,
user_id int not null,
foreign key (user_id) references users(user_id),
title varchar(255) not null,
content text not null,
is_published boolean default true,
published_at date default current_date,
updated_at date
);


create table tags(
tag_id serial primary key,
tag_name varchar(50) unique not null
);


create table post_tags(
tag_id int not null,
post_id int not null,
foreign key (tag_id) references tags(tag_id),
foreign key (post_id) references posts(post_id),
primary key(tag_id,post_id)
);


create table comments(
comment_id serial primary key,
post_id int not null,
user_id int not null,
foreign key (post_id) references posts(post_id),
foreign key (user_id) references users(user_id),
content text not null,
parent_comment_id int,
foreign key (parent_comment_id) references comments(comment_id)
);

INSERT INTO users (username, email, password, full_name, bio, avatar_url, role, is_deleted)
VALUES
    ('admin', 'admin@blog.vn', 'admin123', 'Quản Trị Viên', 'Admin of the blog.', 'https://example.com/avatar_admin.png', 'admin', false),
    ('linhtran', 'linhtran@blog.vn', 'linh123', 'Trần Thị Linh', 'Loves travel and food blogging.', 'https://example.com/avatar_linh.png', 'writer', false),
    ('nguyenvananh', 'vananh@blog.vn', 'anh123', 'Nguyễn Văn Anh', 'Passionate about programming and Java.', 'https://example.com/avatar_anh.png', 'writer', false);

INSERT INTO posts (user_id, title, content, is_published, published_at, updated_at)
VALUES
    (1, 'Welcome to the Blog', 'This is the first post by the admin.', true, CURRENT_DATE, CURRENT_DATE),
    (2, 'Top 5 Destinations in Vietnam', 'Explore the most beautiful places across the country.', true, CURRENT_DATE, CURRENT_DATE),
    (3, 'Java Programming Guide', 'Learn how to build Java applications using Spring Boot.', true, CURRENT_DATE, CURRENT_DATE);

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


