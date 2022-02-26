/*-- Create table
create table ZZDTSZF
(
  dm    VARCHAR2(1) not null,
  dmmc  VARCHAR2(30),
  xh    NUMBER(3),
  dm1   VARCHAR2(1),
  dmmc1 VARCHAR2(30),
  wh    CHAR(1),
  gbbz  CHAR(1),
  fh    VARCHAR2(2),
  isdel VARCHAR2(1),
  xgsj  VARCHAR2(20)
);
create table item_Log(
 id varchar2(32),
 tjsj varchar2(20),
 tjr varchar2(32),
 chsj varchar2(20),
 chr varchar2(32)
);
create table Item_Kz (
  id varchar2(32),
  tjkssj varchar2(20),
  tjjssj varchar2(20),
  pjzf varchar2(20),
  xms varchar2(20)
);
create table tritem (
 id varchar2(32),
 cgmc varchar2(4000),
 zywcr varchar2(4000),
 cgnrlb varchar2(4000),
 dw varchar2(4000),
 xypx varchar2(100),
 avg_Score Number,
 abs_Avg_Score Number,
 itemorder Number
);
create table User(
 id varchar2(32) primary key,
 username varchar2(4000),
 password varchar2(4000)
);
create table t_review(
 id varchar2(32) primary key,
 TRItemid varchar2(32),
 psr varchar2(32),
 pf Number,
 xgyj varchar2(4000),
 istj varchar2(32)
);
create table T_Review_Log(
 id varchar2(32) primary key auto_increment,
 TRItemid varchar2(32),
 pf Number,
 xgyj varchar2(4000),
 logid varchar2(32)
);
create table item_File(
 fileid varchar2(32),
 file_name varchar2(4000),
 file_path varchar2(4000),
 cjsj varchar2(20)
);
*/






