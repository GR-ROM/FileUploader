CREATE TABLE filemetadata{
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    filename VARCHAR(80) NOT NULL,
    displayname VARCHAR(80),
    description VARCHAR(500),
    hashcode INT,
    filesize LONG NOT NULL,
    state INT NOT NULL,
    createdtime DATETIME NOT NULL
}