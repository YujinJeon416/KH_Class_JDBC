--==================================
--관리자(system)계정
--===================================
--student계정 생성 및 권한부여
create user student
identified by student
default tablespace users;

grant connect, resource to student;

--========================================
--Student 계정
--=========================================
--member 테이블 생성
create table member (
    member_id varchar2(20),
    password varchar2(20) not null,
    member_name varchar2(100) not null,
    gender char(1),
    age number,
    email varchar2(200),
    phone char(11) not null,
    address varchar2(1000),
    hobby varchar2(100), --농구, 음악감상, 영화
    enroll_date date default sysdate,
    constraint pk_member_id primary key(member_id),
    constraint ck_member_gender check(gender in ('M', 'F'))
);

--sample data 추가
insert into member
values(
    'honggd', '1234', '홍길동', 'M', 33, 
    'honggd@naver.com', '01012341234',
    '서울 강남구 테헤란로', '등산, 그림, 요리', default
);

select * from member;

insert into member
values(
    'gogd', '1234', '고길동', 'M', 35, 
    'gogd@naver,com', '01012345678',
    '서울 서초구', '산책, 영화', default
);
insert into member
values(
    'leejh', '1234', '이재현', 'M', 25, 
    'leejh@naver,com', '01000000913',
    '서울 강남구 청담동', '노래,춤', default
);
insert into member
values(
    'junew', '1234', '전유진', 'F', 34, 
    'junew@naver.com', '01025080416',
    '경기도 남양주', '게임,코딩', default
);
insert into member
values(
    'sejong', '1234', '세종대왕', 'M', 76, 
    'sejong@naver.com', '01033332345',
    '서울시 중구', '독서, 서예', default
);

insert into member
values(
    'yugs', '1234', '유관순', 'F', null, 
    null, '01031313131',
    null, null, default
);
select * from member;

commit;

create table member_del
    as
    select member.*, sysdate del_date from member
    where 1=0;
    --drop table member_del;
    desc member_del;
    --@실습문제 : 탈퇴회원 관리정책변경
    --삭제트리거 생성
	--resource 롤에 create trigger권한이 있기때문에 별도의 DCL없이 진행할 수 있음.
	create or replace trigger trig_delete_member
		before delete on member
		for each row
	begin
		insert into member_del
		values(:old.member_id, :old.password, :old.member_name, :old.gender, :old.age, :old.email, :old.phone, :old.address, :old.hobby, :old.enroll_date, sysdate);
	end;
	/
	--데이터확인
	select * from member_del;
