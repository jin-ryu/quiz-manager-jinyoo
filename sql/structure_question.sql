CREATE TABLE QUIZ 
(TITLE VARCHAR(50) PRIMARY KEY);

CREATE TABLE QUESTION 
(ID INT AUTO_INCREMENT PRIMARY KEY, 
QUESTION VARCHAR(1024), 
DIFFICULTY INT);

CREATE TABLE QUIZ_CONTENT 
(TITLE VARCHAR(50), 
QID INT, 
PRIMARY KEY(TITLE, QID), 
FOREIGN KEY(TITLE) REFERENCES QUIZ(TITLE) ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY(QID) REFERENCES QUESTION(ID) ON UPDATE CASCADE ON DELETE CASCADE);

CREATE TABLE TOPIC 
(TOPIC VARCHAR(50) PRIMARY KEY);

CREATE TABLE QUESTION_ABOUT 
(TOPIC VARCHAR(50),
QID INT,
PRIMARY KEY(TOPIC, QID),
FOREIGN KEY(TOPIC) REFERENCES TOPIC(TOPIC) ON UPDATE CASCADE ON DELETE SET NULL,
FOREIGN KEY(QID) REFERENCES QUESTION(ID) ON UPDATE CASCADE ON DELETE CASCADE);

CREATE TABLE STUDENT 
(SID VARCHAR(50) PRIMARY KEY, 
NAME VARCHAR(50));

CREATE TABLE MCQCHOICE 
(CID INT AUTO_INCREMENT,
CHOICE VARCHAR(1024), 
QID INT,
VALID BOOLEAN,
PRIMARY KEY (CID, QID),
FOREIGN KEY(QID) REFERENCES QUESTION(ID) ON DELETE CASCADE);

CREATE TABLE ANSWER 
(TEXT VARCHAR(1024), 
QID INT, 
SID VARCHAR(50), 
TITLE VARCHAR(50), 
PRIMARY KEY(TEXT, QID, SID, TITLE),
FOREIGN KEY(QID) REFERENCES QUESTION(ID) ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY(SID) REFERENCES STUDENT(SID) ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY(TITLE) REFERENCES QUIZ(TITLE) ON UPDATE CASCADE ON DELETE CASCADE);

CREATE TABLE MCQANSWER 
(TITLE VARCHAR(50),
QID INT,
SID VARCHAR(50),
CID INT,
PRIMARY KEY(TITLE, QID, SID, CID),
FOREIGN KEY(TITLE) REFERENCES QUIZ(TITLE),
FOREIGN KEY(SID) REFERENCES STUDENT(SID) ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY(CID, QID) REFERENCES MCQCHOICE(CID, QID) ON UPDATE CASCADE ON DELETE CASCADE);


INSERT INTO QUIZ VALUES ('Pollution of Air and Water');

INSERT INTO QUESTION (QUESTION, DIFFICULTY) VALUES ('Highest percentage of air consists of', 2);
INSERT INTO QUESTION (QUESTION, DIFFICULTY) VALUES ('The Taj Mahal is being affected by', 5);
INSERT INTO QUESTION (QUESTION, DIFFICULTY) VALUES ('Most polluted river in the world is', 5);
INSERT INTO QUESTION (QUESTION, DIFFICULTY) VALUES ('Air pollution causes', 2);
INSERT INTO QUESTION (QUESTION, DIFFICULTY) VALUES ('Green House gas is', 1);
INSERT INTO QUESTION (QUESTION, DIFFICULTY) VALUES ('The percentage of nitrogen is', 4);
INSERT INTO QUESTION (QUESTION, DIFFICULTY) VALUES ('What causes pollution', 3);
INSERT INTO QUESTION (QUESTION, DIFFICULTY) VALUES ('CNG is a', 5);
INSERT INTO QUESTION (QUESTION, DIFFICULTY) VALUES ('Pollution of water is responsible for', 1);
INSERT INTO QUESTION (QUESTION, DIFFICULTY) VALUES ('Chlorofluorocarbon is used in', 5);

INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Oxygen', 1, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Carbon dioxide', 1, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Nitrogen', 1, 'T');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Argon', 1, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Noise pollution', 2, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Air pollution', 2, 'T');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Water pollution', 2, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('None of these', 2, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Yamuna', 3, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Cavery', 3, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Chenab', 3, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Ganga', 3, 'T');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Global warming', 4, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Respiratory problems', 4, 'T');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Soil erosion', 4, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('None of these', 4, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Nitrogen', 5, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Oxygen', 5, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Methane', 5, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Carbon dioxide', 5, 'T');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('21%', 6, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('78%', 6, 'T');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('12%', 6, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('87%', 6, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Human activities', 7, 'T');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Trees', 7, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Both of these', 7, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('None of these', 7, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Polluted fuel', 8, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Clean fuel', 8, 'T');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Harmful fuel', 8, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('None', 8, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Oil refineries', 9, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Paper factories', 9, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Sugar mills', 9, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('All', 9, 'T');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Refrigerators', 10, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Air conditioners', 10, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('Perfumes', 10, 'F');
INSERT INTO MCQCHOICE (CHOICE, QID, VALID) VALUES ('All', 10, 'T');

INSERT INTO QUIZ_CONTENT (TITLE, QID) VALUES ('Pollution of Air and Water', 1);
INSERT INTO QUIZ_CONTENT (TITLE, QID) VALUES ('Pollution of Air and Water', 2);
INSERT INTO QUIZ_CONTENT (TITLE, QID) VALUES ('Pollution of Air and Water', 3);
INSERT INTO QUIZ_CONTENT (TITLE, QID) VALUES ('Pollution of Air and Water', 4);
INSERT INTO QUIZ_CONTENT (TITLE, QID) VALUES ('Pollution of Air and Water', 5);
INSERT INTO QUIZ_CONTENT (TITLE, QID) VALUES ('Pollution of Air and Water', 6);
INSERT INTO QUIZ_CONTENT (TITLE, QID) VALUES ('Pollution of Air and Water', 7);
INSERT INTO QUIZ_CONTENT (TITLE, QID) VALUES ('Pollution of Air and Water', 8);
INSERT INTO QUIZ_CONTENT (TITLE, QID) VALUES ('Pollution of Air and Water', 9);
INSERT INTO QUIZ_CONTENT (TITLE, QID) VALUES ('Pollution of Air and Water', 10);

INSERT INTO TOPIC VALUES ('pollution');
INSERT INTO TOPIC VALUES ('air');
INSERT INTO TOPIC VALUES ('water');

INSERT INTO QUESTION_ABOUT (TOPIC, QID) VALUES ('pollution', 1);
INSERT INTO QUESTION_ABOUT (TOPIC, QID) VALUES ('pollution', 2);
INSERT INTO QUESTION_ABOUT (TOPIC, QID) VALUES ('pollution', 3);
INSERT INTO QUESTION_ABOUT (TOPIC, QID) VALUES ('pollution', 4);
INSERT INTO QUESTION_ABOUT (TOPIC, QID) VALUES ('pollution', 5);
INSERT INTO QUESTION_ABOUT (TOPIC, QID) VALUES ('pollution', 7);
INSERT INTO QUESTION_ABOUT (TOPIC, QID) VALUES ('pollution', 8);
INSERT INTO QUESTION_ABOUT (TOPIC, QID) VALUES ('pollution', 9);
INSERT INTO QUESTION_ABOUT (TOPIC, QID) VALUES ('pollution', 10);
INSERT INTO QUESTION_ABOUT (TOPIC, QID) VALUES ('air', 1);
INSERT INTO QUESTION_ABOUT (TOPIC, QID) VALUES ('water', 3);
INSERT INTO QUESTION_ABOUT (TOPIC, QID) VALUES ('air', 4);
INSERT INTO QUESTION_ABOUT (TOPIC, QID) VALUES ('air', 5);
INSERT INTO QUESTION_ABOUT (TOPIC, QID) VALUES ('air', 6);
INSERT INTO QUESTION_ABOUT (TOPIC, QID) VALUES ('water', 9);