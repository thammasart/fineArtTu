# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

<<<<<<< HEAD
=======
create table address (
  id                        bigint not null,
  building_no               varchar(255),
  alley                     varchar(255),
  road                      varchar(255),
  parish                    varchar(255),
  district                  varchar(255),
  province                  varchar(255),
  post_code                 varchar(255),
  constraint pk_address primary key (id))
;

create table company (
  id                        bigint not null,
  name                      varchar(255),
  dealer                    varchar(255),
  telephone_number          varchar(255),
  pay_period                integer,
  send_period               integer,
  durable_type              varchar(255),
  consumable_type           varchar(255),
  address_id                bigint,
  constraint pk_company primary key (id))
;

create table consumable (
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
  brand                     varchar(255),
  dealer                    varchar(255),
  telephone_number          varchar(255),
  details                   varchar(255),
  part_of_pic               varchar(255),
  company_id                bigint,
  constraint pk_consumable primary key (id))
;

create table consumable_code (
  id                        bigint not null,
  number                    bigint not null,
  code                      integer not null,
  description               varchar(255) not null,
  consumable_type_id        bigint,
  constraint pk_consumable_code primary key (id))
;

create table consumable_type (
  id                        bigint not null,
  type_name                 varchar(255) not null,
  acronym                   varchar(255) not null,
  constraint pk_consumable_type primary key (id))
;

>>>>>>> 80be4116ed3f7859eeb2b2372d112a6867b5f077
create table durable_articles (
  id                        bigint not null,
  group_class               varchar(255),
  type                      varchar(255),
  name                      varchar(255),
<<<<<<< HEAD
  budget_type               varchar(255),
  budget_year               integer,
=======
  code_from_stock           varchar(255),
  budget_type               varchar(255),
  budget_year               integer,
  llife_time                double,
  alert_time                double,
>>>>>>> 80be4116ed3f7859eeb2b2372d112a6867b5f077
  price_no_vat              double,
  price                     double,
  balance                   integer,
  classifier                varchar(255),
  brand                     varchar(255),
<<<<<<< HEAD
=======
  serial_number             varchar(255),
>>>>>>> 80be4116ed3f7859eeb2b2372d112a6867b5f077
  dealer                    varchar(255),
  telephone_number          varchar(255),
  details                   varchar(255),
  part_of_pic               varchar(255),
  fsn_number_type_id        varchar(255),
<<<<<<< HEAD
  constraint pk_durable_articles primary key (id))
;

=======
  company_id                bigint,
  constraint pk_durable_articles primary key (id))
;

create table durable_goods (
  id                        bigint not null,
  group_class               varchar(255),
  type                      varchar(255),
  name                      varchar(255),
  budget_type               varchar(255),
  budget_year               integer,
  llife_time                double,
  price_no_vat              double,
  price                     double,
  balance                   integer,
  classifier                varchar(255),
  brand                     varchar(255),
  serial_number             varchar(255),
  dealer                    varchar(255),
  telephone_number          varchar(255),
  details                   varchar(255),
  part_of_pic               varchar(255),
  company_id                bigint,
  constraint pk_durable_goods primary key (id))
;

>>>>>>> 80be4116ed3f7859eeb2b2372d112a6867b5f077
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

<<<<<<< HEAD
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
  brand                     varchar(255),
  dealer                    varchar(255),
  telephone_number          varchar(255),
  details                   varchar(255),
  part_of_pic               varchar(255),
  constraint pk_supplies primary key (id))
;

=======
>>>>>>> 80be4116ed3f7859eeb2b2372d112a6867b5f077
create table user (
  username                  varchar(255) not null,
  password                  varchar(255) not null,
  constraint pk_user primary key (username))
;

<<<<<<< HEAD
create sequence durable_articles_seq;

=======
create sequence address_seq;

create sequence company_seq;

create sequence consumable_seq;

create sequence consumable_code_seq;

create sequence consumable_type_seq;

create sequence durable_articles_seq;

create sequence durable_goods_seq;

>>>>>>> 80be4116ed3f7859eeb2b2372d112a6867b5f077
create sequence fsn_class_seq;

create sequence fsn_group_seq;

create sequence fsn_type_seq;

create sequence user_seq;

<<<<<<< HEAD
alter table durable_articles add constraint fk_durable_articles_fsn_number_1 foreign key (fsn_number_type_id) references fsn_type (type_id) on delete restrict on update restrict;
create index ix_durable_articles_fsn_number_1 on durable_articles (fsn_number_type_id);
alter table fsn_class add constraint fk_fsn_class_group_2 foreign key (group_group_id) references fsn_group (group_id) on delete restrict on update restrict;
create index ix_fsn_class_group_2 on fsn_class (group_group_id);
alter table fsn_type add constraint fk_fsn_type_groupClass_3 foreign key (group_class_group_class_id) references fsn_class (group_class_id) on delete restrict on update restrict;
create index ix_fsn_type_groupClass_3 on fsn_type (group_class_group_class_id);
=======
alter table company add constraint fk_company_address_1 foreign key (address_id) references address (id) on delete restrict on update restrict;
create index ix_company_address_1 on company (address_id);
alter table consumable add constraint fk_consumable_company_2 foreign key (company_id) references company (id) on delete restrict on update restrict;
create index ix_consumable_company_2 on consumable (company_id);
alter table consumable_code add constraint fk_consumable_code_consumableT_3 foreign key (consumable_type_id) references consumable_type (id) on delete restrict on update restrict;
create index ix_consumable_code_consumableT_3 on consumable_code (consumable_type_id);
alter table durable_articles add constraint fk_durable_articles_fsn_number_4 foreign key (fsn_number_type_id) references fsn_type (type_id) on delete restrict on update restrict;
create index ix_durable_articles_fsn_number_4 on durable_articles (fsn_number_type_id);
alter table durable_articles add constraint fk_durable_articles_company_5 foreign key (company_id) references company (id) on delete restrict on update restrict;
create index ix_durable_articles_company_5 on durable_articles (company_id);
alter table durable_goods add constraint fk_durable_goods_company_6 foreign key (company_id) references company (id) on delete restrict on update restrict;
create index ix_durable_goods_company_6 on durable_goods (company_id);
alter table fsn_class add constraint fk_fsn_class_group_7 foreign key (group_group_id) references fsn_group (group_id) on delete restrict on update restrict;
create index ix_fsn_class_group_7 on fsn_class (group_group_id);
alter table fsn_type add constraint fk_fsn_type_groupClass_8 foreign key (group_class_group_class_id) references fsn_class (group_class_id) on delete restrict on update restrict;
create index ix_fsn_type_groupClass_8 on fsn_type (group_class_group_class_id);
>>>>>>> 80be4116ed3f7859eeb2b2372d112a6867b5f077



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

<<<<<<< HEAD
drop table if exists durable_articles;

=======
drop table if exists address;

drop table if exists company;

drop table if exists consumable;

drop table if exists consumable_code;

drop table if exists consumable_type;

drop table if exists durable_articles;

drop table if exists durable_goods;

>>>>>>> 80be4116ed3f7859eeb2b2372d112a6867b5f077
drop table if exists fsn_class;

drop table if exists fsn_group;

drop table if exists fsn_type;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

<<<<<<< HEAD
drop sequence if exists durable_articles_seq;

=======
drop sequence if exists address_seq;

drop sequence if exists company_seq;

drop sequence if exists consumable_seq;

drop sequence if exists consumable_code_seq;

drop sequence if exists consumable_type_seq;

drop sequence if exists durable_articles_seq;

drop sequence if exists durable_goods_seq;

>>>>>>> 80be4116ed3f7859eeb2b2372d112a6867b5f077
drop sequence if exists fsn_class_seq;

drop sequence if exists fsn_group_seq;

drop sequence if exists fsn_type_seq;

drop sequence if exists user_seq;

