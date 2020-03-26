-- USE RedditDB;

DROP TABLE IF EXISTS UserAuthentication;
DROP TABLE IF EXISTS VoteComment;
DROP TABLE IF EXISTS VotePost;
DROP TABLE IF EXISTS Comment;
DROP TABLE IF EXISTS Post; 
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

CREATE TABLE Post(
	id BINARY(16) NOT NULL,
    title VARCHAR(300) NOT NULL,
    description TEXT,
    link VARCHAR(1000),
    deleted BOOLEAN, 
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    user_id BINARY(16),
    subreddit_id BINARY(16) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES User (id),
    FOREIGN KEY (subreddit_id) REFERENCES Subreddit (id),
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