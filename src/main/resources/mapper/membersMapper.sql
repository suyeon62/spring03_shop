CREATE TABLE   members(
  memberEmail varchar2(50) ,  --이메일
  memberPass varchar2(30),  --비밀번호
  memberName varchar2(30), --이름
  memberPhone char(11),  --전화번호  
  memberType number(1),  --회원구분 일반회원 1, 관리자 2
  constraint members_email primary key(memberEmail)
);

-- MYSQL ===================================================================================
CREATE TABLE members (
  memberEmail VARCHAR(50),
  memberPass VARCHAR(100),
  memberName VARCHAR(30),
  memberPhone CHAR(11),
  memberType INT DEFAULT 1,
  PRIMARY KEY (memberEmail)
);

INSERT INTO members(memberEmail, memberPass, memberName, memberPhone)
VALUES ('kkk@daum.net','1234','홍길동','00000000');

-- memberType 컬럼 삭제
ALTER TABLE members
DROP column memberType;

-- memberType 컬럼 추가
ALTER TABLE members
ADD COLUMN memberType INT DEFAULT 1;

-- memgerPass 크기 수정
ALTER TABLE members
MODIFY COLUMN memberPass varchar(100);

SELECT * FROM members;

-- default 변경
ALTER TABLE members
ALTER column memberType SET DEFAULT 1;

SELECT * FROM board;
SELECT * FROM members;

DELETE FROM members;

ALTER TABLE board
DROP constraint board_memberEmail;

ALTER TABLE board
ADD CONSTRAINT board_memberEmail FOREIGN KEY(memberEmail) REFERENCES members(memberEmail)
ON DELETE CASCADE;

DELETE FROM board
WHERE memberEmail='min@daum.net';

SELECT * FROM board
WHERE memberEmail='min@daum.net';
