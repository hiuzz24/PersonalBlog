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
                       image_url VARCHAR(500),
                       is_published BOOLEAN DEFAULT TRUE,
                       published_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NULL DEFAULT NULL,
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


-- Insert 20 additional posts
INSERT INTO posts (user_id, title, content, is_published, published_at, image_url)
VALUES
    (1, 'Welcome to the Blog', 'This is the first post by the admin.',TRUE, '2024-01-15 10:00:00','https://img.tripi.vn/cdn-cgi/image/width=700,height=700/https://gcs.tripi.vn/public-tripi/tripi-feed/img/482974vzV/anh-mo-ta.png'),
    (2, 'Top 5 Destinations in Vietnam', 'Explore the most beautiful places across the country.',TRUE, '2024-01-15 10:00:00', 'https://cdn.pixabay.com/photo/2021/12/12/20/00/play-6865967_640.jpg'),
    (3, 'Java Programming Guide', 'Learn how to build Java applications using Spring Boot.',TRUE, '2024-01-15 10:00:00','https://www.cameo.com/cdn-cgi/image/fit=cover,format=auto,width=210,height=278/https://cdn.cameo.com/thumbnails/676f52ff3c3c8b378e754b47-processed.jpg'),
    (2, 'Getting Started with Spring Boot', 'Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can just run. In this guide, we will explore the basics of Spring Boot and how to get started with your first application.', TRUE, '2024-01-15 10:00:00','https://images.unsplash.com/photo-1575936123452-b67c3203c357?fm=jpg&q=60&w=3000&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8aW1hZ2V8ZW58MHx8MHx8fDA%3D'),

    (2, 'Best Street Food in Ho Chi Minh City', 'Ho Chi Minh City offers an incredible variety of street food. From banh mi to pho, discover the must-try dishes and where to find them in this bustling metropolis.', TRUE, '2024-01-16 14:30:00','https://thumbs.dreamstime.com/b/innovative-medical-device-featuring-eye-image-illustrating-advanced-tracking-technology-generated-ai-358374352.jpg'),

    (3, 'Java 21 New Features Overview', 'Java 21 brings exciting new features including Virtual Threads, Pattern Matching, and more. Learn about these enhancements and how they can improve your Java development experience.', TRUE, '2024-01-17 09:15:00','https://static.vecteezy.com/system/resources/thumbnails/057/068/323/small/single-fresh-red-strawberry-on-table-green-background-food-fruit-sweet-ma...to.jpg'),

    (3, 'Database Design Best Practices', 'Learn the fundamental principles of database design, including normalization, indexing strategies, and performance optimization techniques for modern applications.', TRUE, '2024-01-18 11:45:00','https://cdn.prod.website-files.com/62d84e447b4f9e7263d31e94/6399a4d27711a5ad2c9bf5cd_ben-sweet-2LowviVHZ-E-unsplash-1.jpeg'),

    (2, 'Hidden Gems in Hanoi Old Quarter', 'Explore the lesser-known attractions in Hanoi Old Quarter. From traditional coffee shops to ancient temples, discover places that most tourists miss.', TRUE, '2024-01-19 16:20:00','https://static.vecteezy.com/system/resources/thumbnails/036/324/708/small/ai-generated-picture-of-a-tiger-walking-in-the-forest-photo.jpg'),
    (3, 'Microservices Architecture with Spring Cloud', 'Building scalable microservices using Spring Cloud. This comprehensive guide covers service discovery, configuration management, and inter-service communication.', TRUE, '2024-01-20 08:30:00','https://imagekit.io/blog/content/images/2019/12/image-optimization.jpg'),

    (2, 'RESTful API Design Guidelines', 'Create robust and maintainable REST APIs following industry best practices. Learn about proper HTTP methods, status codes, and resource naming conventions.', TRUE, '2024-01-21 13:10:00','https://petapixel.com/assets/uploads/2024/01/High-resolution-image-of-sun.jpg'),

    (2, 'Vietnamese Coffee Culture', 'Dive deep into Vietnam coffee culture, from traditional drip coffee to modern cafe trends. Discover the history and preparation methods of this beloved beverage.', TRUE, '2024-01-22 15:45:00','https://gratisography.com/wp-content/uploads/2025/01/gratisography-dog-vacation-800x525.jpg'),

    (3, 'Docker Containerization for Java Apps', 'Learn how to containerize Java applications using Docker. This tutorial covers creating Dockerfiles, managing dependencies, and deployment strategies.', TRUE, '2024-01-23 10:20:00','https://www.adobe.com/creativecloud/file-types/image/raster/media_11b1f18d198f70c3c75d86f8d6226caf617240e78.png?width=750&format=png&optimize=medium'),

    (3, 'Building Secure Web Applications', 'Security is paramount in web development. Learn about common vulnerabilities, authentication methods, and how to implement security best practices in your applications.', TRUE, '2024-01-24 12:00:00','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSUfKkAw3WJUy_fow3DjoBxlnBlC_KxUceXeg&s'),

    (2, 'Food Photography Tips for Bloggers', 'Capture stunning food photos for your blog with these professional tips. Learn about lighting, composition, and editing techniques to make your dishes look irresistible.', TRUE, '2024-01-25 17:30:00','https://png.pngtree.com/thumb_back/fh260/background/20230612/pngtree-beautiful-black-horses-pictures-best-of-free-hd-wallpapers-horse-images-image_...47.jpg'),

    (3, 'Clean Code Principles in Java', 'Writing clean, maintainable code is essential for long-term project success. Explore key principles and practices that will make your Java code more readable and robust.', TRUE, '2024-01-26 09:40:00','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQcS06-zImX9b-ow_spLhrKaUve-Tquz8pIhQ&s'),

    (2, 'Introduction to NoSQL Databases', 'Explore the world of NoSQL databases, including MongoDB, Redis, and Cassandra. Learn when to use NoSQL vs traditional relational databases.', TRUE, '2024-01-27 14:15:00','https://images.ctfassets.net/hrltx12pl8hq/01rJn4TormMsGQs1ZRIpzX/16a1cae2440420d0fd0a7a9a006f2dcb/Artboard_Copy_231.jpg?fit=fill&w=480&h=400'),
    (2, 'Weekend Getaways from Ho Chi Minh City', 'Escape the city buzz with these amazing weekend destinations. From beach resorts to mountain retreats, find the perfect short trip for relaxation.', TRUE, '2024-01-28 11:30:00','https://static01.nyt.com/images/2025/05/05/multimedia/05dc-trump-pope-lzjb/05dc-trump-pope-lzjb-articleLarge.jpg?quality=75&auto=webp&disable=upscale'),

    (3, 'Testing Strategies for Java Applications', 'Comprehensive guide to testing Java applications. Learn about unit testing, integration testing, and test-driven development with JUnit and Mockito.', TRUE, '2024-01-29 08:50:00','https://th.bing.com/th/id/OIG2.VaLBvwJFzNjPrD3lKJmM'),

    (3, 'Modern Frontend Development Trends', 'Stay updated with the latest frontend technologies and frameworks. Explore React, Vue.js, and Angular, and learn which one suits your project needs.', TRUE, '2024-01-30 16:00:00','https://static.vecteezy.com/system/resources/thumbnails/041/166/062/small/ai-generated-hawk-high-quality-image-free-photo.jpg'),

    (2, 'Traditional Vietnamese Cooking Techniques', 'Master the art of Vietnamese cuisine with these traditional cooking methods. From stir-frying to steaming, learn the techniques that create authentic flavors.', TRUE, '2024-01-31 13:25:00','https://media.licdn.com/dms/image/sync/v2/D4D27AQFeTQZdq3uRyg/articleshare-shrink_800/articleshare-shrink_800/0/1712166466706?e=2147483647&v=beta&...cDoXfQc'),

    (3, 'Performance Optimization in Java', 'Optimize your Java applications for better performance. Learn about JVM tuning, memory management, and profiling techniques to identify bottlenecks.', TRUE, '2024-02-01 10:10:00','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSlfmlcpQ3vwI0-vy9RHO7zdTpEnbFAR5nw14V-lXOWKbtvqAuNu3pA2JVV0cBeKf87bcM&usqp=CAU'),

    (2, 'DevOps Best Practices for Development Teams', 'Implement DevOps practices to improve collaboration between development and operations teams. Learn about CI/CD pipelines, automation, and monitoring.', TRUE, '2024-02-02 15:20:00','https://i0.wp.com/picjumbo.com/wp-content/uploads/beautiful-fall-natural-scenery-painting-cabin-by-a-lake-free-image.jpeg?w=600&quality=80'),

    (2, 'Exploring Vietnamese National Parks', 'Discover the natural beauty of Vietnam through its national parks. From Phong Nha-Ke Bang to Cat Tien, explore diverse ecosystems and wildlife.', TRUE, '2024-02-03 12:45:00','https://buffer.com/resources/content/images/2024/11/Instagram-Image-size.png');

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
    (3, 4),
    (4, 3),
    (4, 4),
    (5, 2),
    (6, 4),
    (6, 5),
    (7, 3),
    (7, 5),
    (8, 1),
    (9, 3),
    (9, 4),
    (9, 5),
    (10, 3),
    (10, 5),
    (11, 2),
    (12, 3),
    (12, 4),
    (12, 5),
    (13, 3),
    (13, 5),
    (14, 2),
    (15, 3),
    (15, 4),
    (16, 3),
    (16, 5),
    (17, 1),
    (18, 3),
    (18, 4),
    (19, 5),
    (20, 2),
    (21, 3),
    (21, 4),
    (21, 5),
    (22, 5),
    (23, 1);


INSERT INTO comments (post_id, user_id, content, parent_comment_id)
VALUES
    (2, 3, 'Thanks for the recommendations! I love Da Nang.', NULL),
    (3, 2, 'Great tutorial. Java is awesome!', NULL),
    (3, 3, 'Could you share more Spring Boot resources?', 2);