DROP DATABASE IF EXISTS PersonalBlog;
CREATE DATABASE PersonalBlog;
USE PersonalBlog;

CREATE TABLE users (
                       user_id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       email VARCHAR(50) UNIQUE NOT NULL,
                       password VARCHAR(50),
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
                       image_url TEXT,
                       body TEXT,
                       status VARCHAR(50) NOT NULL,
                       reason_rejected MEDIUMTEXT,
                       is_published BOOLEAN,
                        is_deleted boolean default false,
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

CREATE TABLE follows (
                         follower_id INT,
                         following_id INT,
                         PRIMARY KEY (follower_id, following_id),
                         FOREIGN KEY (follower_id) REFERENCES users(user_id),
                         FOREIGN KEY (following_id) REFERENCES users(user_id)
);

CREATE TABLE notifications(
    notification_id int auto_increment primary key,
    user_id int not null,
    type varchar(50) not null,
    from_user_id int not null,
    post_id int,
    message varchar(255),
    is_read boolean default false,
    created_at timestamp default current_timestamp,
    foreign key (user_id) references users(user_id),
    foreign key (from_user_id) references users(user_id),
    foreign key (post_id) references posts(post_id)
);

INSERT INTO users (username, email, password, full_name, bio, avatar_url, role, is_deleted)
VALUES
    ('admin', 'admin@blog.vn', 'admin123', 'Admin', 'Admin of the blog.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTmjC0CZGGDJbJ4hH1_jLKaMzhL96cwCQ8OeQ&s', 'ADMIN', FALSE),
    ('linhtran', 'linhtran@blog.vn', 'linh123', 'Trần Thị Linh', 'Loves travel and food blogging.', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTe7mWcFQ-e97tkOw1TN2252vi4gLuYMTbvXQ&s', 'WRITER', FALSE),
    ('nguyenvananh', 'vananh@blog.vn', 'anh123', 'Nguyễn Văn Anh', 'Passionate about programming and Java.', 'https://i.pinimg.com/736x/b7/91/44/b79144e03dc4996ce319ff59118caf65.jpg', 'WRITER', FALSE);

INSERT INTO posts (post_id, user_id, title, content, image_url, body, status, reason_rejected, is_published,is_deleted, published_at)
VALUES
    -- Post 1
    (1, 1, 'Welcome to the Blog', 'This is the first post by the admin.',
     'https://9clouds.com/wp-content/uploads/2023/02/istockphoto-1371547852-612x612-1.jpg',
     '<h2>Welcome to Our New Blog!</h2>
     <p>We are excited to introduce you to our official blog where we will share insightful articles, tutorials, and community updates.</p>
     <p>Whether you re a developer, traveler, foodie, or just someone curious — you ll find something useful here. Stay connected!</p>
	 <hr>
	 <p>Start exploring our <strong>latest posts</strong>  and don t forget to follow us on social media for regular updates.</p>',
	 'Approved', NULL, TRUE,FALSE, '2024-01-15 10:00:00'),

	-- Post 2
	(2, 2, 'Top 5 Destinations in Vietnam', 'Explore the most beautiful places across the country.',
	 'https://www.outlooktravelmag.com/media/vietnam-1-1611926800.profileImage.2x-jpg-webp.webp',
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
	 'Approved', NULL, TRUE,FALSE, '2024-01-15 10:00:00'),

	-- Post 3
	(3, 3, 'Java Programming Guide', 'Learn how to build Java applications using Spring Boot.',
	 'https://www.kulkul.tech/wp-content/uploads/2022/06/java-programming-what-is-it.jpg',
	 '<h2>Java Developer Roadmap</h2>
        <p>Java is a powerful, object-oriented language used for building robust applications. Here are some steps to mastering Java:</p>
	 <ul>
	   <li>Understand the basics: syntax, data types, OOP principles.</li>
	   <li>Work with <strong>collections</strong> and <strong>streams</strong>.</li>
	   <li>Learn frameworks like <em>Spring Boot</em> and <em>Hibernate</em>.</li>
	   <li>Practice building RESTful APIs and use Maven/Gradle.</li>
	 </ul>
	 <p>With consistency, you ll become confident building full-stack applications in Java.</p>',
	 'Approved', NULL, TRUE,FALSE, '2024-01-15 10:00:00'),

	-- Post 4
	(4, 2, 'Getting Started with Spring Boot', 'Spring Boot makes it easy to create stand-alone Spring applications.',
	 'https://cdn.bap-software.net/2024/08/26213247/spring.jpg',
	 '<h2>Spring Boot Essentials</h2>
	 <p>Spring Boot is a framework that helps developers build applications faster and with less configuration. It provides:</p>
	 <ul>
	   <li>Embedded servers like Tomcat.</li>
	   <li>Auto-configuration and starter dependencies.</li>
	   <li>Easy integration with JPA, Security, and more.</li>
        </ul>
        <p>Here s a basic setup:</p>
		<pre><code>
	 @SpringBootApplication
	 public class MyApp {
	 public static void main(String[] args) {
	 SpringApplication.run(MyApp.class, args);
	}
	}
	</code></pre>
	<p>Start with Spring Initializr and you re ready to build REST APIs quickly.</p>',
     'Approved', NULL, TRUE,FALSE, '2024-01-15 10:00:00'),

    -- Post 5
    (5, 2, 'Best Street Food in Ho Chi Minh City', 'Discover the best street food in the bustling city.',
     'https://res.klook.com/image/upload/q_85/c_fill,w_750/q_80/blogen/2018/03/viet-street-food1.png',
     '<h2>Street Food Heaven</h2>
     <p>Ho Chi Minh City is known for its vibrant street food culture. Here are top dishes you shouldn t miss:</p>
	 <ul>
	   <li><strong>Bánh Mì:</strong> Vietnamese sandwich with meat and pickled veggies.</li>
	   <li><strong>Phở:</strong> Traditional noodle soup served with beef or chicken.</li>
	   <li><strong>Bánh Xèo:</strong> Crispy savory pancake with shrimp and bean sprouts.</li>
	 </ul>
	 <p>Head to District 1 and Ben Thanh Market for the best street food experience.</p>',
	 'Approved', NULL, TRUE,FALSE, '2024-01-16 14:30:00'),

	-- Post 6
	(6, 3, 'Java 21 New Features Overview', 'Java 21, is the latest Long-Term Support (LTS) release of the Java Development Kit (JDK). It introduces several significant features and enhancements, and improvements to the Foreign Function & Memory API.',
	 'https://m.media-amazon.com/images/I/51OVgdGFlPL._UF1000,1000_QL80_.jpg',
	 '<h2>Java 21 Highlights</h2>
	 <p>Java 21 introduces powerful updates to the language and runtime:</p>
	 <ul>
	   <li>Preview features: Pattern Matching, String Templates, and Virtual Threads.</li>
        <li>Enhanced <strong>Foreign Function & Memory API</strong> for native integration.</li>
        <li>Improved performance and better support for cloud-native apps.</li>
        </ul>
        <p>Stay updated with the latest LTS version to future-proof your Java applications.</p>',
	 'Approved', NULL, TRUE,FALSE, '2024-11-15 10:00:00'),

	-- Post 7
	(7, 3, 'Database Design Best Practices', 'Learn the fundamental principles of database design, including normalization, indexing strategies, and performance optimization techniques for modern applications.',
	 'https://cdn.corporatefinanceinstitute.com/assets/database-1024x703.jpeg',
	 '<h2>Mastering Database Design</h2>
        <p>Efficient database design is crucial for performance and scalability:</p>
        <ul>
        <li>Apply normalization rules to reduce redundancy.</li>
        <li>Use <strong>indexes</strong> smartly to optimize query performance.</li>
        <li>Balance data integrity with performance.</li>
        </ul>
        <p>Build solid foundations for reliable and scalable applications.</p>',
	 'Approved', NULL, TRUE,FALSE, '2024-01-18 11:45:00'),

	-- Post 8
	(8, 3, 'Hidden Gems in Hanoi Old Quarter', 'Explore the lesser-known attractions in Hanoi Old Quarter. From traditional coffee shops to ancient temples, discover places that most tourists miss.',
	 'https://localvietnam.com/wp-content/uploads/2024/07/old-quarter-ta-hien-1.jpg',
	 '<h2>Explore Hidden Corners of Hanoi</h2>
	 <p>The Old Quarter is full of secret charms:</p>
	 <ul>
	   <li>Authentic coffee spots known only to locals.</li>
	   <li>Quiet temples nestled in narrow alleys.</li>
	   <li>Historic architecture and cultural tales.</li>
	 </ul>
	 <p>Go beyond the tourist trails and truly feel Hanoi.</p>',
	 'Approved', NULL, TRUE,FALSE, '2024-01-19 16:20:00'),

	-- Post 9
	(9, 3, 'Microservices Architecture with Spring Cloud', 'Building scalable microservices using Spring Cloud. This comprehensive guide covers service discovery, configuration management, and inter-service communication.',
	 'https://cdn.prod.website-files.com/63e3d6905bacd6855fa38c1c/63e3d6905bacd6a44ea39205_Spring%20Cloud%20%20_%20Thumbnail.jpg',
	 '<h2>Microservices with Spring Cloud</h2>
        <p>Spring Cloud simplifies the microservices journey:</p>
        <ul>
        <li>Use <strong>Eureka</strong> for service discovery.</li>
        <li>Manage config centrally with <strong>Spring Cloud Config</strong>.</li>
        <li>Enable seamless communication with Feign and Gateway.</li>
        </ul>
        <p>Build resilient, scalable systems using proven tools.</p>',
	 'Approved', NULL, TRUE,FALSE, '2024-01-20 08:30:00'),

	-- Post 10
	(10, 3, 'RESTful API Design Guidelines', 'Create robust and maintainable REST APIs following industry best practices. Learn about proper HTTP methods, status codes, and resource naming conventions.',
	 'https://phpenthusiast.com/theme/assets/images/blog/what_is_rest_api.png',
	 '<h2>Designing Better REST APIs</h2>
        <p>Follow key practices for professional API design:</p>
        <ul>
        <li>Use correct HTTP verbs (GET, POST, PUT, DELETE).</li>
        <li>Implement status codes appropriately.</li>
        <li>Name resources clearly and consistently.</li>
        </ul>
        <p>Build APIs that are easy to consume and maintain.</p>',
	 'Approved', NULL, TRUE,FALSE, '2024-01-21 13:10:00'),

	-- Post 11
	(11, 3, 'Vietnamese Coffee Culture', 'Dive deep into Vietnam coffee culture, from traditional drip coffee to modern cafe trends. Discover the history and preparation methods of this beloved beverage.',
	 'https://media-cldnry.s-nbcnews.com/image/upload/newscms/2019_33/2203981/171026-better-coffee-boost-se-329p.jpg',
	 '<h2>A Taste of Vietnam in Every Sip</h2>
        <p>Vietnamese coffee culture is rich and unique:</p>
        <ul>
        <li>Enjoy <strong>cà phê phin</strong> – traditional drip-brewed coffee.</li>
        <li>Explore modern interpretations in local cafes.</li>
        <li>Understand the role of coffee in social life.</li>
        </ul>
        <p>Experience Vietnam through its iconic drink.</p>',
	 'Approved', NULL, TRUE,FALSE, '2024-01-22 15:45:00'),

	-- Post 12
	(12, 3, 'Docker Containerization for Java Apps', 'Learn how to containerize Java applications using Docker. This tutorial covers creating Dockerfiles, managing dependencies, and deployment strategies.',
	 'https://coding-anni.de/wp-content/uploads/2023/04/Docker.001-840x473.jpeg',
	 '<h2>Containerize Java Apps with Docker</h2>
        <p>Docker simplifies deployment and scaling:</p>
        <ul>
        <li>Write clean <strong>Dockerfiles</strong> for your Java projects.</li>
        <li>Use <em>multi-stage builds</em> to optimize image size.</li>
        <li>Run containers using <strong>Docker Compose</strong>.</li>
        </ul>
        <p>Take your Java apps to the next level with containerization.</p>',
	 'Approved', NULL, TRUE,FALSE, '2024-01-23 10:20:00'),

	-- Post 13
	(13, 3, 'Building Secure Web Applications', 'Security is paramount in web development. Learn about common vulnerabilities, authentication methods, and how to implement security best practices in your applications.',
	 'https://topdev.vn/blog/wp-content/uploads/2021/04/Website-1024x538.png',
	 '<h2>Secure Your Web Applications</h2>
        <p>Protect your users and data effectively:</p>
        <ul>
        <li>Understand <strong>OWASP Top 10</strong> threats.</li>
        <li>Use modern <em>authentication</em> methods like JWT.</li>
        <li>Apply input validation and secure coding practices.</li>
        </ul>
        <p>Build apps with trust and resilience from the ground up.</p>',
	 'Approved', NULL, TRUE,FALSE, '2024-01-24 12:00:00'),

	-- Post 14
	(14, 3, 'Food Photography Tips for Bloggers','Photographing food is harder than it seems. My photos have improved with practice. Here are the best tips and tricks I can offer about food photography and equipment.',
	 'https://www.truefoodkitchen.com/wp-content/uploads/2025/01/TFK016_01f_A1B00195-Enhanced-NR_v02.jpg',
	 '<h2>Level Up Your Food Photos</h2>
        <p>Capture the essence of flavor through the lens:</p>
        <ul>
        <li>Use natural light for authentic tones.</li>
        <li>Frame with intention – think top-down or 45 degrees.</li>
        <li>Invest in basic gear: tripod, reflectors, and styling props.</li>
        </ul>
        <p>Practice makes perfect – and deliciously aesthetic.</p>',
	 'Approved', NULL, TRUE,FALSE, '2024-01-23 10:20:00'),

	-- Post 15
	(15, 3, 'Clean Code Principles in Java', 'Writing clean, maintainable code is essential for long-term project success. Explore key principles and practices that will make your Java code more readable and robust.',
	 'https://cdn.hswstatic.com/gif/java-code.jpg',
	 '<h2>Writing Clean Java Code</h2>
	 <p>Keep your codebase professional and scalable:</p>
	 <ul>
	   <li>Follow naming conventions and meaningful identifiers.</li>
	   <li>Keep methods small and focused.</li>
	   <li>Use <strong>SOLID principles</strong> and avoid code smells.</li>
	 </ul>
	 <p>Readable code is maintainable code.</p>',
	 'Approved', NULL, TRUE,FALSE, '2024-01-26 09:40:00'),

	-- Post 16
	(16, 2, 'Introduction to NoSQL Databases', 'Explore the world of NoSQL databases, including MongoDB, Redis, and Cassandra. Learn when to use NoSQL vs traditional relational databases.',
	 'https://blog.purestorage.com/wp-content/uploads/2023/08/shutterstock_467410664-1.jpg',
	 '<h2>Getting Started with NoSQL</h2>
        <p>NoSQL offers flexible data storage solutions:</p>
        <ul>
        <li>Use <strong>MongoDB</strong> for document-based data.</li>
        <li>Choose <em>Redis</em> for in-memory caching needs.</li>
        <li>Scale with distributed systems like Cassandra.</li>
        </ul>
        <p>Pick the right tool for the right data scenario.</p>',
	 'Approved', NULL, TRUE,FALSE, '2024-01-27 14:15:00');

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
	(16, 3);

INSERT INTO comments (post_id, user_id, content, parent_comment_id)
VALUES
    (2, 3, 'Thanks for the recommendations! I love Da Nang.', NULL),
    (3, 2, 'Great tutorial. Java is awesome!', NULL),
    (3, 3, 'Could you share more Spring Boot resources?', NULL);


INSERT INTO follows (follower_id, following_id) VALUES
(2, 1),
(3, 1),
(1, 3);

