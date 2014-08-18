# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table fsn_class (
  group_class_id            varchar(255) not null,
  group_class_description   varchar(255) not null,
  group_group_id            varchar(255),
  constraint pk_fsn_class primary key (group_class_id))
;

create table fsn_group (
  group_id                  varchar(255) not null,
  group_description         varchar(255) not null,
  constraint pk_fsn_group primary key (group_id))
;

create table fsn_type (
  type_id                   varchar(255) not null,
  type_description          varchar(255) not null,
  group_class_group_class_id varchar(255),
  constraint pk_fsn_type primary key (type_id))
;

create table supplies (
  id                        bigint not null,
  group_class               varchar(255),
  type                      varchar(255),
  name                      varchar(255),
  budget_type               varchar(255),
  budget_year               integer,
  price_no_vat              double,
  price                     double,
  balance                   integer,
  classifier                varchar(255),
  constraint pk_supplies primary key (id))
;

create table user (
  username                  varchar(255) not null,
  password                  varchar(255) not null,
  constraint pk_user primary key (username))
;

create sequence fsn_class_seq;

create sequence fsn_group_seq;

create sequence fsn_type_seq;

create sequence supplies_seq;

create sequence user_seq;

alter table fsn_class add constraint fk_fsn_class_group_1 foreign key (group_group_id) references fsn_group (group_id) on delete restrict on update restrict;
create index ix_fsn_class_group_1 on fsn_class (group_group_id);
alter table fsn_type add constraint fk_fsn_type_groupClass_2 foreign key (group_class_group_class_id) references fsn_class (group_class_id) on delete restrict on update restrict;
create index ix_fsn_type_groupClass_2 on fsn_type (group_class_group_class_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists fsn_class;

drop table if exists fsn_group;

drop table if exists fsn_type;

drop table if exists supplies;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists fsn_class_seq;

drop sequence if exists fsn_group_seq;

drop sequence if exists fsn_type_seq;

drop sequence if exists supplies_seq;

drop sequence if exists user_seq;

