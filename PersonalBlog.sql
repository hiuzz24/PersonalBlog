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
                       role VARCHAR(50),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE posts (
                       post_id INT AUTO_INCREMENT PRIMARY KEY,
                       user_id INT NOT NULL,
                       title VARCHAR(255) NOT NULL,
                       content TEXT NOT NULL,
                       image_url VARCHAR(500),
                       body text,
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


INSERT INTO posts (post_id, user_id, title, content, body, is_published, published_at, image_url)
VALUES
-- Post 1
(1, 1, 'Welcome to the Blog', 'This is the first post by the admin.',
 '<h2>Welcome to Our New Blog!</h2>
 <p>We are excited to introduce you to our official blog where we will share insightful articles, tutorials, and community updates.</p>
 <p>Whether you’re a developer, traveler, foodie, or just someone curious — you’ll find something useful here. Stay connected!</p>
 <hr>
 <p>Start exploring our <strong>latest posts</strong>, and don’t forget to follow us on social media for regular updates.</p>',
 TRUE, '2024-01-15 10:00:00',
 'https://9clouds.com/wp-content/uploads/2023/02/istockphoto-1371547852-612x612-1.jpg'),

-- Post 2
(2, 2, 'Top 5 Destinations in Vietnam', 'Explore the most beautiful places across the country.',
 '<h2>Vietnam Travel Guide</h2>
 <p>Vietnam is a beautiful country with a rich cultural heritage and stunning landscapes. Here are our top 5 must-visit destinations:</p>
 <ol>
   <li><strong>Ha Long Bay:</strong> A UNESCO World Heritage Site with limestone islands.</li>
   <li><strong>Sapa:</strong> Mountain scenery and ethnic culture.</li>
   <li><strong>Hoi An:</strong> Ancient town with lanterns and great food.</li>
   <li><strong>Da Nang:</strong> Coastal city with modern charm.</li>
   <li><strong>Phu Quoc Island:</strong> Tropical paradise with white-sand beaches.</li>
 </ol>
 <p>Start planning your adventure today!</p>',
 TRUE, '2024-01-15 10:00:00',
 'https://www.outlooktravelmag.com/media/vietnam-1-1611926800.profileImage.2x-jpg-webp.webp'),

-- Post 3
(3, 3, 'Java Programming Guide', 'Learn how to build Java applications using Spring Boot.',
 '<h2>Java Developer Roadmap</h2>
 <p>Java is a powerful, object-oriented language used for building robust applications. Here are some steps to mastering Java:</p>
 <ul>
   <li>Understand the basics: syntax, data types, OOP principles.</li>
   <li>Work with <strong>collections</strong> and <strong>streams</strong>.</li>
   <li>Learn frameworks like <em>Spring Boot</em> and <em>Hibernate</em>.</li>
   <li>Practice building RESTful APIs and use Maven/Gradle.</li>
 </ul>
 <p>With consistency, you ll become confident building full-stack applications in Java.</p>',
 TRUE, '2024-01-15 10:00:00',
 'https://www.kulkul.tech/wp-content/uploads/2022/06/java-programming-what-is-it.jpg'),

-- Post 4
(4, 2, 'Getting Started with Spring Boot', 'Spring Boot makes it easy to create stand-alone Spring applications.',
 '<h2>Spring Boot Essentials</h2>
 <p>Spring Boot is a framework that helps developers build applications faster and with less configuration. It provides:</p>
 <ul>
   <li>Embedded servers like Tomcat.</li>
   <li>Auto-configuration and starter dependencies.</li>
   <li>Easy integration with JPA, Security, and more.</li>
     </ul>
     <p>Here’s a basic setup:</p>
     <pre><code>
  @SpringBootApplication
  public class MyApp {
  public static void main(String[] args) {
  SpringApplication.run(MyApp.class, args);
 }
 }
 </code></pre>
 <p>Start with Spring Initializr and you re ready to build REST APIs quickly.</p>',
 TRUE, '2024-01-15 10:00:00',
 'https://cdn.bap-software.net/2024/08/26213247/spring.jpg'),

-- Post 5
(5, 2, 'Best Street Food in Ho Chi Minh City', 'Discover the best street food in the bustling city.',
 '<h2>Street Food Heaven</h2>
 <p>Ho Chi Minh City is known for its vibrant street food culture. Here are top dishes you shouldn’t miss:</p>
 <ul>
   <li><strong>Bánh Mì:</strong> Vietnamese sandwich with meat and pickled veggies.</li>
   <li><strong>Phở:</strong> Traditional noodle soup served with beef or chicken.</li>
   <li><strong>Bánh Xèo:</strong> Crispy savory pancake with shrimp and bean sprouts.</li>
 </ul>
 <p>Head to District 1 and Ben Thanh Market for the best street food experience.</p>',
 TRUE, '2024-01-16 14:30:00',
 'https://res.klook.com/image/upload/q_85/c_fill,w_750/q_80/blogen/2018/03/viet-street-food1.png');

/*(3, 'Java 21 New Features Overview','Hello java proiawohtgpiuawhvtuiyawbg 3 itghawiyhvtgwz',TRUE,'2024-11-15 10:00:00',
    'https://m.media-amazon.com/images/I/51OVgdGFlPL._UF1000,1000_QL80_.jpg'),

(3, 'Database Design Best Practices', 'Learn the fundamental principles of database design, including normalization, indexing strategies, and performance optimization techniques for modern applications.', TRUE, '2024-01-18 11:45:00',
    'https://cdn.corporatefinanceinstitute.com/assets/database-1024x703.jpeg'),

(2, 'Hidden Gems in Hanoi Old Quarter', 'Explore the lesser-known attractions in Hanoi Old Quarter. From traditional coffee shops to ancient temples, discover places that most tourists miss.', TRUE, '2024-01-19 16:20:00',
    'https://localvietnam.com/wp-content/uploads/2024/07/old-quarter-ta-hien-1.jpg'),

(3, 'Microservices Architecture with Spring Cloud', 'Building scalable microservices using Spring Cloud. This comprehensive guide covers service discovery, configuration management, and inter-service communication.', TRUE, '2024-01-20 08:30:00',
    'https://cdn.prod.website-files.com/63e3d6905bacd6855fa38c1c/63e3d6905bacd6a44ea39205_Spring%20Cloud%20%20_%20Thumbnail.jpg'),

(2, 'RESTful API Design Guidelines', 'Create robust and maintainable REST APIs following industry best practices. Learn about proper HTTP methods, status codes, and resource naming conventions.', TRUE, '2024-01-21 13:10:00',
    'https://phpenthusiast.com/theme/assets/images/blog/what_is_rest_api.png'),

(2, 'Vietnamese Coffee Culture', 'Dive deep into Vietnam coffee culture, from traditional drip coffee to modern cafe trends. Discover the history and preparation methods of this beloved beverage.', TRUE, '2024-01-22 15:45:00',
    'https://media-cldnry.s-nbcnews.com/image/upload/newscms/2019_33/2203981/171026-better-coffee-boost-se-329p.jpg'),

(3, 'Docker Containerization for Java Apps', 'Learn how to containerize Java applications using Docker. This tutorial covers creating Dockerfiles, managing dependencies, and deployment strategies.', TRUE, '2024-01-23 10:20:00',
    'https://coding-anni.de/wp-content/uploads/2023/04/Docker.001-840x473.jpeg'),

(3, 'Building Secure Web Applications', 'Security is paramount in web development. Learn about common vulnerabilities, authentication methods, and how to implement security best practices in your applications.', TRUE, '2024-01-24 12:00:00',
    'https://topdev.vn/blog/wp-content/uploads/2021/04/Website-1024x538.png'),

(2, 'Food Photography Tips for Bloggers','Photographing food is harder than it seems. My photos have improved with practice. Here are the best tips and tricks I can offer about food photography and equipment.',TRUE,'2024-01-23 10:20:00',
    'https://www.truefoodkitchen.com/wp-content/uploads/2025/01/TFK016_01f_A1B00195-Enhanced-NR_v02.jpg'),

(3, 'Clean Code Principles in Java', 'Writing clean, maintainable code is essential for long-term project success. Explore key principles and practices that will make your Java code more readable and robust.', TRUE, '2024-01-26 09:40:00',
    'https://cdn.hswstatic.com/gif/java-code.jpg'),

(2, 'Introduction to NoSQL Databases', 'Explore the world of NoSQL databases, including MongoDB, Redis, and Cassandra. Learn when to use NoSQL vs traditional relational databases.', TRUE, '2024-01-27 14:15:00',
    'https://blog.purestorage.com/wp-content/uploads/2023/08/shutterstock_467410664-1.jpg'),

(2, 'Weekend Getaways from Ho Chi Minh City', 'Escape the city buzz with these amazing weekend destinations. From beach resorts to mountain retreats, find the perfect short trip for relaxation.', TRUE, '2024-01-28 11:30:00',
    'https://lp-cms-production.imgix.net/2024-09/shutterstockRF1035848284.jpg?auto=format,compress&q=72&w=1440&h=810&fit=crop'),

(3, 'Testing Strategies for Java Applications', 'Comprehensive guide to testing Java applications. Learn about unit testing, integration testing, and test-driven development with JUnit and Mockito.', TRUE, '2024-01-29 08:50:00',
    'https://i0.wp.com/devequipment.com/wp-content/uploads/2025/01/Online-Courses-with-Certificates-The-Ultimate-Guide-to-Mastering-Coding.jpg?resize=800%2C445&ssl=1'),

(3, 'Modern Frontend Development Trends', 'Stay updated with the latest frontend technologies and frameworks. Explore React, Vue.js, and Angular, and learn which one suits your project needs.', TRUE, '2024-01-30 16:00:00',
    'https://miro.medium.com/v2/resize:fit:1400/0*3Ybf3-bCS5R7fbWT'),

(2, 'Traditional Vietnamese Cooking Techniques', 'Master the art of Vietnamese cuisine with these traditional cooking methods. From stir-frying to steaming, learn the techniques that create authentic flavors.', TRUE, '2024-01-31 13:25:00',
    'https://vietnam.travel/sites/default/files/inline-images/vietnamese%20street%20food-3.jpg'),

(3, 'Performance Optimization in Java', 'Optimize your Java applications for better performance. Learn about JVM tuning, memory management, and profiling techniques to identify bottlenecks.', TRUE, '2024-02-01 10:10:00',
    'https://mallow-tech.com/wp-content/uploads/2023/10/Laravel-performance-optimization.jpg'),

(2, 'DevOps Best Practices for Development Teams', 'Implement DevOps practices to improve collaboration between development and operations teams. Learn about CI/CD pipelines, automation, and monitoring.', TRUE, '2024-02-02 15:20:00',
    'https://teambuildinghub.com/wp-content/uploads/2022/04/Build-Strong-Teams-Featured-Image.png'),

(2, 'Exploring Vietnamese National Parks', 'Discover the natural beauty of Vietnam through its national parks. From Phong Nha-Ke Bang to Cat Tien, explore diverse ecosystems and wildlife.', TRUE, '2024-02-03 12:45:00',
    'https://nationalparksassociation.org/wp-content/uploads/2024/02/Cat-Ba-National-Park.webp');*/

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
    (5, 2);
/*    (6, 4),
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
    (23, 1);*/


INSERT INTO comments (post_id, user_id, content, parent_comment_id)
VALUES
    (2, 3, 'Thanks for the recommendations! I love Da Nang.', NULL),
    (3, 2, 'Great tutorial. Java is awesome!', NULL),
    (3, 3, 'Could you share more Spring Boot resources?', 2);
