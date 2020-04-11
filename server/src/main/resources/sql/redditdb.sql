-- USE RedditDB;

DROP TABLE IF EXISTS UserAuthentication;
DROP TABLE IF EXISTS VoteComment;
DROP TABLE IF EXISTS VotePost;
DROP TABLE IF EXISTS Comment;
DROP TABLE IF EXISTS Post;
DROP TABLE IF EXISTS PostType;
DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS Subreddit;


CREATE TABLE User (
	id BINARY(16) NOT NULL,
    username VARCHAR(40) NOT NULL UNIQUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX (username)
);

CREATE TABLE UserAuthentication(
	user_id BINARY(16) NOT NULL,
    password VARCHAR(68) NOT NULL,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES User (id),
    INDEX (user_id)
);


CREATE TABLE Subreddit(
	id BINARY(16) NOT NULL,
    name VARCHAR(30) NOT NULL UNIQUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX (name)
);

CREATE TABLE PostType(
    id INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE Post(
	id BINARY(16) NOT NULL,
    title VARCHAR(300) NOT NULL,
    description TEXT,
    link VARCHAR(1000),
    image_path VARCHAR(300),
    deleted BOOLEAN, 
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    user_id BINARY(16),
    subreddit_id BINARY(16) NOT NULL,
    post_type_id INTEGER NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES User (id),
    FOREIGN KEY (subreddit_id) REFERENCES Subreddit (id),
    FOREIGN KEY (post_type_id) REFERENCES PostType (id),
    INDEX (title),
    INDEX (user_id),
    INDEX (subreddit_id)
);

CREATE TABLE Comment(
	id BINARY(16) NOT NULL,
    comment TEXT NOT NULL,
    deleted BOOLEAN,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    user_id BINARY(16),
    post_id BINARY(16) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES User (id),
    FOREIGN KEY (post_id) REFERENCES Post (id),
    INDEX (user_id),
    INDEX (post_id)
);

CREATE TABLE VoteComment(
	user_id BINARY(16) NOT NULL,
    comment_id BINARY(16) NOT NULL,
    vote TINYINT,
    PRIMARY KEY (user_id, comment_id),
    FOREIGN KEY (user_id) REFERENCES User (id),
    FOREIGN KEY (comment_id) REFERENCES Comment (id),
    INDEX (user_id),
    INDEX (comment_id)
);

CREATE TABLE VotePost(
	user_id BINARY(16) NOT NULL,
    post_id BINARY(16) NOT NULL,
    vote TINYINT,
    PRIMARY KEY (user_id, post_id),
    FOREIGN KEY (user_id) REFERENCES User (id),
    FOREIGN KEY (post_id) REFERENCES Post (id),
    INDEX (user_id),
    INDEX (post_id)
);

ALTER TABLE Post
ADD CONSTRAINT post_image_check
CHECK (
CASE
    WHEN post_type_id = 2
        THEN
        CASE
            WHEN image_path IS NOT NULL
                THEN 1
                ELSE 0
            END
    ELSE 1
END = 1
);

ALTER TABLE Post
ADD CONSTRAINT post_link_check
CHECK (
CASE
    WHEN post_type_id = 3
        THEN
        CASE
            WHEN link IS NOT NULL
                THEN 1
                ELSE 0
            END
    ELSE 1
END = 1
);