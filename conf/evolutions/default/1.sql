# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table compo (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  start_date                datetime,
  end_date                  datetime,
  directory_path            varchar(255),
  vote_open                 tinyint(1) default 0,
  upload_open               tinyint(1) default 0,
  constraint pk_compo primary key (id))
;

create table production (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  comment                   varchar(255),
  user                      varchar(255),
  compo                     varchar(255),
  filename                  varchar(255),
  upload_date               datetime,
  constraint pk_production primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
  username                  varchar(255),
  password                  varchar(255),
  nickname                  varchar(255),
  groupname                 varchar(255),
  constraint pk_user primary key (id))
;

create table vote (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  compo_id                  bigint,
  production_id             bigint,
  note                      bigint,
  constraint pk_vote primary key (id))
;

create table votekey (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  votekey                   varchar(255),
  constraint uq_votekey_votekey unique (votekey),
  constraint pk_votekey primary key (id))
;

alter table vote add constraint fk_vote_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_vote_user_1 on vote (user_id);
alter table vote add constraint fk_vote_compo_2 foreign key (compo_id) references compo (id) on delete restrict on update restrict;
create index ix_vote_compo_2 on vote (compo_id);
alter table vote add constraint fk_vote_production_3 foreign key (production_id) references production (id) on delete restrict on update restrict;
create index ix_vote_production_3 on vote (production_id);
alter table votekey add constraint fk_votekey_user_4 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_votekey_user_4 on votekey (user_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table compo;

drop table production;

drop table user;

drop table vote;

drop table votekey;

SET FOREIGN_KEY_CHECKS=1;

