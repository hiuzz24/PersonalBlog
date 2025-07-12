create table Users(
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
create table Posts(
post_id serial primary key,
user_id int not null,
foreign key (user_id) references Users(user_id),
title varchar(255) not null,
content text not null,
is_published boolean default true,
published_at date default current_date,
update_at date
);
create table Tags(
tag_id serial primary key,
tag_name varchar(50) unique not null
);
create table Post_Tags(
tag_id int not null,
post_id int not null,
foreign key (tag_id) references Tags(tag_id),
foreign key (post_id) references Posts(post_id),
primary key(tag_id,post_id)
);
create table Comments(
comment_id serial primary key,
post_id int not null,
user_id int not null,
foreign key (post_id) references Posts(post_id),
foreign key (user_id) references Users(user_id),
content text not null,
parent_comment_id int,
foreign key (parent_comment_id) references Comments(comment_id)
);
