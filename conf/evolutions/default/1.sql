# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table durable_articles_ai_committee (
  id                        bigint not null,
  committee_id              bigint,
  procurement_id            bigint,
  constraint pk_durable_articles_ai_committee primary key (id))
;

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

create table durable_articles_auction (
  id                        bigint not null,
  total_price               double,
  approve_date              timestamp,
  company_id                bigint,
  constraint pk_durable_articles_auction primary key (id))
;

create table auction_detail (
  id                        bigint not null,
  code                      varchar(255),
  price                     double,
  auction_id                bigint,
  constraint pk_auction_detail primary key (id))
;

create table auction_d_committee (
  id                        bigint not null,
  committee_id              bigint,
  auction_id                bigint,
  constraint pk_auction_d_committee primary key (id))
;

create table auction_e_committee (
  id                        bigint not null,
  committee_id              bigint,
  auction_id                bigint,
  constraint pk_auction_e_committee primary key (id))
;

create table auction_ff_committee (
  id                        bigint not null,
  committee_id              bigint,
  auction_id                bigint,
  constraint pk_auction_ff_committee primary key (id))
;

create table consumable_committee (
  id                        bigint not null,
  type                      integer,
  identification_no         varchar(255),
  title                     varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  position                  varchar(255),
  employees_type            varchar(255),
  committee_position        varchar(255),
  constraint ck_consumable_committee_type check (type in (0,1,2,3,4)),
  constraint pk_consumable_committee primary key (id))
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
  code_id                   bigint,
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

create table durable_articles_donation (
  id                        bigint not null,
  approve_date              timestamp,
  company_id                bigint,
  constraint pk_durable_articles_donation primary key (id))
;

create table durable_articles_donation_detail (
  id                        bigint not null,
  constraint pk_durable_articles_donation_det primary key (id))
;

create table donation_d_committee (
  id                        bigint not null,
  committee_id              bigint,
  donation_id               bigint,
  constraint pk_donation_d_committee primary key (id))
;

create table donation_ff_committee (
  id                        bigint not null,
  committee_id              bigint,
  donation_id               bigint,
  constraint pk_donation_ff_committee primary key (id))
;

create table durable_articles (
  id                        bigint not null,
  code                      varchar(255),
  code_from_stock           varchar(255),
  status                    integer,
  detail_id                 bigint,
  constraint ck_durable_articles_status check (status in (0,1,2,3,4)),
  constraint pk_durable_articles primary key (id))
;

create table durable_goods (
  id                        bigint not null,
  remain                    integer,
  code_id                   bigint,
  detail_id                 bigint,
  constraint pk_durable_goods primary key (id))
;

create table durable_articles_eo_committee (
  id                        bigint not null,
  committee_id              bigint,
  procurement_id            bigint,
  constraint pk_durable_articles_eo_committee primary key (id))
;

create table consumable_eo_committee (
  id                        bigint not null,
  committee_id              bigint,
  procurement_id            bigint,
  constraint pk_consumable_eo_committee primary key (id))
;

create table durable__goods_eo_committee (
  id                        bigint not null,
  committee_id              bigint,
  procurement_id            bigint,
  constraint pk_durable__goods_eo_committee primary key (id))
;

create table external_transfer (
  id                        bigint not null,
  approve_date              timestamp,
  company_id                bigint,
  constraint pk_external_transfer primary key (id))
;

create table external_transfer_detail (
  id                        bigint not null,
  constraint pk_external_transfer_detail primary key (id))
;

create table ex_transfer_d_committee (
  id                        bigint not null,
  committee_id              bigint,
  ex_transfer_id            bigint,
  constraint pk_ex_transfer_d_committee primary key (id))
;

create table ex_transfer_ff_committee (
  id                        bigint not null,
  committee_id              bigint,
  ex_transfer_id            bigint,
  constraint pk_ex_transfer_ff_committee primary key (id))
;

create table fsn_class (
  group_class_id            varchar(2) not null,
  group_class_description   varchar(255) not null,
  group_group_id            varchar(2),
  constraint pk_fsn_class primary key (group_class_id))
;

create table fsn_description (
  description_id            varchar(4) not null,
  description               varchar(255) not null,
  type_type_id              varchar(255),
  constraint pk_fsn_description primary key (description_id))
;

create table fsn_group (
  group_id                  varchar(2) not null,
  group_description         varchar(255) not null,
  constraint pk_fsn_group primary key (group_id))
;

create table fsn_type (
  type_id                   varchar(255) not null,
  type_description          varchar(255) not null,
  group_class_group_class_id varchar(2),
  constraint pk_fsn_type primary key (type_id))
;

create table internal_transfer (
  id                        bigint not null,
  constraint pk_internal_transfer primary key (id))
;

create table internal_transfer_detail (
  id                        bigint not null,
  constraint pk_internal_transfer_detail primary key (id))
;

create table other_transfer (
  id                        bigint not null,
  approve_date              timestamp,
  description               varchar(255),
  constraint pk_other_transfer primary key (id))
;

create table other_transfer_detail (
  id                        bigint not null,
  constraint pk_other_transfer_detail primary key (id))
;

create table other_transfer_d_committee (
  id                        bigint not null,
  committee_id              bigint,
  other_transfer_id         bigint,
  constraint pk_other_transfer_d_committee primary key (id))
;

create table other_transfer_ff_committee (
  id                        bigint not null,
  committee_id              bigint,
  other_transfer_id         bigint,
  constraint pk_other_transfer_ff_committee primary key (id))
;

create table durable_goods_procurement (
  id                        bigint not null,
  contract_no               varchar(255),
  budget_type               varchar(255),
  budget_year               integer,
  date_of_approval          timestamp,
  dealer                    varchar(255),
  telephone_number          varchar(255),
  company_id                bigint,
  constraint pk_durable_goods_procurement primary key (id))
;

create table durable_articles_procurement (
  id                        bigint not null,
  contract_no               varchar(255),
  budget_type               varchar(255),
  budget_year               integer,
  date_of_approval          timestamp,
  dealer                    varchar(255),
  telephone_number          varchar(255),
  company_id                bigint,
  constraint pk_durable_articles_procurement primary key (id))
;

create table consumable_procurement (
  id                        bigint not null,
  contract_no               varchar(255),
  budget_type               varchar(255),
  budget_year               integer,
  date_of_approval          timestamp,
  dealer                    varchar(255),
  telephone_number          varchar(255),
  company_id                bigint,
  constraint pk_consumable_procurement primary key (id))
;

create table durable_articles_procurement_detail (
  id                        bigint not null,
  code                      varchar(255),
  description               varchar(255),
  quantity                  integer,
  classifier                varchar(255),
  price_no_vat              double,
  price                     double,
  llife_time                double,
  alert_time                double,
  brand                     varchar(255),
  serial_number             varchar(255),
  part_of_pic               varchar(255),
  fsn_description_id        varchar(4),
  procurement_id            bigint,
  constraint pk_durable_articles_procurement_ primary key (id))
;

create table consumable_procurement_detail (
  id                        bigint not null,
  code                      varchar(255),
  description               varchar(255),
  quantity                  integer,
  classifier                varchar(255),
  price_no_vat              double,
  price                     double,
  brand                     varchar(255),
  part_of_pic               varchar(255),
  fsn_description_id        varchar(4),
  procurement_id            bigint,
  constraint pk_consumable_procurement_detail primary key (id))
;

create table durable_goods_procurement_detail (
  id                        bigint not null,
  code                      varchar(255),
  description               varchar(255),
  quantity                  integer,
  classifier                varchar(255),
  price_no_vat              double,
  price                     double,
  brand                     varchar(255),
  part_of_pic               varchar(255),
  fsn_description_id        varchar(4),
  procurement_id            bigint,
  constraint pk_durable_goods_procurement_det primary key (id))
;

create table durable_articles_repairing (
  id                        bigint not null,
  constraint pk_durable_articles_repairing primary key (id))
;

create table durable_articles_repairing_detail (
  id                        bigint not null,
  constraint pk_durable_articles_repairing_de primary key (id))
;

create table durable_goods_requisition (
  id                        bigint not null,
  approve_date              timestamp,
  user_username             varchar(255),
  approver_username         varchar(255),
  constraint pk_durable_goods_requisition primary key (id))
;

create table consumable_requisition (
  id                        bigint not null,
  approve_date              timestamp,
  user_username             varchar(255),
  approver_username         varchar(255),
  constraint pk_consumable_requisition primary key (id))
;

create table durable_goods_requisition_detail (
  id                        bigint not null,
  quantity                  integer,
  description               varchar(255),
  withdrawer_username       varchar(255),
  requisition_id            bigint,
  constraint pk_durable_goods_requisition_det primary key (id))
;

create table consumable_requisition_detail (
  id                        bigint not null,
  quantity                  integer,
  description               varchar(255),
  withdrawer_username       varchar(255),
  requisition_id            bigint,
  constraint pk_consumable_requisition_detail primary key (id))
;

create table user (
  username                  varchar(255) not null,
  password                  varchar(255) not null,
  status                    integer,
  constraint ck_user_status check (status in (0,1,2)),
  constraint pk_user primary key (username))
;

create sequence durable_articles_ai_committee_seq;

create sequence address_seq;

create sequence durable_articles_auction_seq;

create sequence auction_detail_seq;

create sequence auction_d_committee_seq;

create sequence auction_e_committee_seq;

create sequence auction_ff_committee_seq;

create sequence consumable_committee_seq;

create sequence company_seq;

create sequence consumable_seq;

create sequence consumable_code_seq;

create sequence consumable_type_seq;

create sequence durable_articles_donation_seq;

create sequence durable_articles_donation_detail_seq;

create sequence donation_d_committee_seq;

create sequence donation_ff_committee_seq;

create sequence durable_articles_seq;

create sequence durable_goods_seq;

create sequence durable_articles_eo_committee_seq;

create sequence consumable_eo_committee_seq;

create sequence durable__goods_eo_committee_seq;

create sequence external_transfer_seq;

create sequence external_transfer_detail_seq;

create sequence ex_transfer_d_committee_seq;

create sequence ex_transfer_ff_committee_seq;

create sequence fsn_class_seq;

create sequence fsn_description_seq;

create sequence fsn_group_seq;

create sequence fsn_type_seq;

create sequence internal_transfer_seq;

create sequence internal_transfer_detail_seq;

create sequence other_transfer_seq;

create sequence other_transfer_detail_seq;

create sequence other_transfer_d_committee_seq;

create sequence other_transfer_ff_committee_seq;

create sequence durable_goods_procurement_seq;

create sequence durable_articles_procurement_seq;

create sequence consumable_procurement_seq;

create sequence durable_articles_procurement_detail_seq;

create sequence consumable_procurement_detail_seq;

create sequence durable_goods_procurement_detail_seq;

create sequence durable_articles_repairing_seq;

create sequence durable_articles_repairing_detail_seq;

create sequence durable_goods_requisition_seq;

create sequence consumable_requisition_seq;

create sequence durable_goods_requisition_detail_seq;

create sequence consumable_requisition_detail_seq;

create sequence user_seq;

alter table durable_articles_ai_committee add constraint fk_durable_articles_ai_committ_1 foreign key (committee_id) references consumable_committee (id) on delete restrict on update restrict;
create index ix_durable_articles_ai_committ_1 on durable_articles_ai_committee (committee_id);
alter table durable_articles_ai_committee add constraint fk_durable_articles_ai_committ_2 foreign key (procurement_id) references durable_articles_procurement (id) on delete restrict on update restrict;
create index ix_durable_articles_ai_committ_2 on durable_articles_ai_committee (procurement_id);
alter table durable_articles_auction add constraint fk_durable_articles_auction_co_3 foreign key (company_id) references company (id) on delete restrict on update restrict;
create index ix_durable_articles_auction_co_3 on durable_articles_auction (company_id);
alter table auction_detail add constraint fk_auction_detail_auction_4 foreign key (auction_id) references durable_articles_auction (id) on delete restrict on update restrict;
create index ix_auction_detail_auction_4 on auction_detail (auction_id);
alter table auction_d_committee add constraint fk_auction_d_committee_committ_5 foreign key (committee_id) references consumable_committee (id) on delete restrict on update restrict;
create index ix_auction_d_committee_committ_5 on auction_d_committee (committee_id);
alter table auction_d_committee add constraint fk_auction_d_committee_auction_6 foreign key (auction_id) references durable_articles_auction (id) on delete restrict on update restrict;
create index ix_auction_d_committee_auction_6 on auction_d_committee (auction_id);
alter table auction_e_committee add constraint fk_auction_e_committee_committ_7 foreign key (committee_id) references consumable_committee (id) on delete restrict on update restrict;
create index ix_auction_e_committee_committ_7 on auction_e_committee (committee_id);
alter table auction_e_committee add constraint fk_auction_e_committee_auction_8 foreign key (auction_id) references durable_articles_auction (id) on delete restrict on update restrict;
create index ix_auction_e_committee_auction_8 on auction_e_committee (auction_id);
alter table auction_ff_committee add constraint fk_auction_ff_committee_commit_9 foreign key (committee_id) references consumable_committee (id) on delete restrict on update restrict;
create index ix_auction_ff_committee_commit_9 on auction_ff_committee (committee_id);
alter table auction_ff_committee add constraint fk_auction_ff_committee_aucti_10 foreign key (auction_id) references durable_articles_auction (id) on delete restrict on update restrict;
create index ix_auction_ff_committee_aucti_10 on auction_ff_committee (auction_id);
alter table company add constraint fk_company_address_11 foreign key (address_id) references address (id) on delete restrict on update restrict;
create index ix_company_address_11 on company (address_id);
alter table consumable add constraint fk_consumable_code_12 foreign key (code_id) references consumable_code (id) on delete restrict on update restrict;
create index ix_consumable_code_12 on consumable (code_id);
alter table consumable add constraint fk_consumable_company_13 foreign key (company_id) references company (id) on delete restrict on update restrict;
create index ix_consumable_company_13 on consumable (company_id);
alter table consumable_code add constraint fk_consumable_code_consumable_14 foreign key (consumable_type_id) references consumable_type (id) on delete restrict on update restrict;
create index ix_consumable_code_consumable_14 on consumable_code (consumable_type_id);
alter table durable_articles_donation add constraint fk_durable_articles_donation__15 foreign key (company_id) references company (id) on delete restrict on update restrict;
create index ix_durable_articles_donation__15 on durable_articles_donation (company_id);
alter table donation_d_committee add constraint fk_donation_d_committee_commi_16 foreign key (committee_id) references consumable_committee (id) on delete restrict on update restrict;
create index ix_donation_d_committee_commi_16 on donation_d_committee (committee_id);
alter table donation_d_committee add constraint fk_donation_d_committee_donat_17 foreign key (donation_id) references durable_articles_donation (id) on delete restrict on update restrict;
create index ix_donation_d_committee_donat_17 on donation_d_committee (donation_id);
alter table donation_ff_committee add constraint fk_donation_ff_committee_comm_18 foreign key (committee_id) references consumable_committee (id) on delete restrict on update restrict;
create index ix_donation_ff_committee_comm_18 on donation_ff_committee (committee_id);
alter table donation_ff_committee add constraint fk_donation_ff_committee_dona_19 foreign key (donation_id) references durable_articles_donation (id) on delete restrict on update restrict;
create index ix_donation_ff_committee_dona_19 on donation_ff_committee (donation_id);
alter table durable_articles add constraint fk_durable_articles_detail_20 foreign key (detail_id) references durable_articles_procurement_detail (id) on delete restrict on update restrict;
create index ix_durable_articles_detail_20 on durable_articles (detail_id);
alter table durable_goods add constraint fk_durable_goods_code_21 foreign key (code_id) references consumable_code (id) on delete restrict on update restrict;
create index ix_durable_goods_code_21 on durable_goods (code_id);
alter table durable_goods add constraint fk_durable_goods_detail_22 foreign key (detail_id) references durable_goods_procurement_detail (id) on delete restrict on update restrict;
create index ix_durable_goods_detail_22 on durable_goods (detail_id);
alter table durable_articles_eo_committee add constraint fk_durable_articles_eo_commit_23 foreign key (committee_id) references consumable_committee (id) on delete restrict on update restrict;
create index ix_durable_articles_eo_commit_23 on durable_articles_eo_committee (committee_id);
alter table durable_articles_eo_committee add constraint fk_durable_articles_eo_commit_24 foreign key (procurement_id) references durable_articles_procurement (id) on delete restrict on update restrict;
create index ix_durable_articles_eo_commit_24 on durable_articles_eo_committee (procurement_id);
alter table consumable_eo_committee add constraint fk_consumable_eo_committee_co_25 foreign key (committee_id) references consumable_committee (id) on delete restrict on update restrict;
create index ix_consumable_eo_committee_co_25 on consumable_eo_committee (committee_id);
alter table consumable_eo_committee add constraint fk_consumable_eo_committee_pr_26 foreign key (procurement_id) references consumable_procurement (id) on delete restrict on update restrict;
create index ix_consumable_eo_committee_pr_26 on consumable_eo_committee (procurement_id);
alter table durable__goods_eo_committee add constraint fk_durable__goods_eo_committe_27 foreign key (committee_id) references consumable_committee (id) on delete restrict on update restrict;
create index ix_durable__goods_eo_committe_27 on durable__goods_eo_committee (committee_id);
alter table durable__goods_eo_committee add constraint fk_durable__goods_eo_committe_28 foreign key (procurement_id) references durable_goods_procurement (id) on delete restrict on update restrict;
create index ix_durable__goods_eo_committe_28 on durable__goods_eo_committee (procurement_id);
alter table external_transfer add constraint fk_external_transfer_company_29 foreign key (company_id) references company (id) on delete restrict on update restrict;
create index ix_external_transfer_company_29 on external_transfer (company_id);
alter table ex_transfer_d_committee add constraint fk_ex_transfer_d_committee_co_30 foreign key (committee_id) references consumable_committee (id) on delete restrict on update restrict;
create index ix_ex_transfer_d_committee_co_30 on ex_transfer_d_committee (committee_id);
alter table ex_transfer_d_committee add constraint fk_ex_transfer_d_committee_ex_31 foreign key (ex_transfer_id) references external_transfer (id) on delete restrict on update restrict;
create index ix_ex_transfer_d_committee_ex_31 on ex_transfer_d_committee (ex_transfer_id);
alter table ex_transfer_ff_committee add constraint fk_ex_transfer_ff_committee_c_32 foreign key (committee_id) references consumable_committee (id) on delete restrict on update restrict;
create index ix_ex_transfer_ff_committee_c_32 on ex_transfer_ff_committee (committee_id);
alter table ex_transfer_ff_committee add constraint fk_ex_transfer_ff_committee_e_33 foreign key (ex_transfer_id) references external_transfer (id) on delete restrict on update restrict;
create index ix_ex_transfer_ff_committee_e_33 on ex_transfer_ff_committee (ex_transfer_id);
alter table fsn_class add constraint fk_fsn_class_group_34 foreign key (group_group_id) references fsn_group (group_id) on delete restrict on update restrict;
create index ix_fsn_class_group_34 on fsn_class (group_group_id);
alter table fsn_description add constraint fk_fsn_description_type_35 foreign key (type_type_id) references fsn_type (type_id) on delete restrict on update restrict;
create index ix_fsn_description_type_35 on fsn_description (type_type_id);
alter table fsn_type add constraint fk_fsn_type_groupClass_36 foreign key (group_class_group_class_id) references fsn_class (group_class_id) on delete restrict on update restrict;
create index ix_fsn_type_groupClass_36 on fsn_type (group_class_group_class_id);
alter table other_transfer_d_committee add constraint fk_other_transfer_d_committee_37 foreign key (committee_id) references consumable_committee (id) on delete restrict on update restrict;
create index ix_other_transfer_d_committee_37 on other_transfer_d_committee (committee_id);
alter table other_transfer_d_committee add constraint fk_other_transfer_d_committee_38 foreign key (other_transfer_id) references other_transfer (id) on delete restrict on update restrict;
create index ix_other_transfer_d_committee_38 on other_transfer_d_committee (other_transfer_id);
alter table other_transfer_ff_committee add constraint fk_other_transfer_ff_committe_39 foreign key (committee_id) references consumable_committee (id) on delete restrict on update restrict;
create index ix_other_transfer_ff_committe_39 on other_transfer_ff_committee (committee_id);
alter table other_transfer_ff_committee add constraint fk_other_transfer_ff_committe_40 foreign key (other_transfer_id) references other_transfer (id) on delete restrict on update restrict;
create index ix_other_transfer_ff_committe_40 on other_transfer_ff_committee (other_transfer_id);
alter table durable_goods_procurement add constraint fk_durable_goods_procurement__41 foreign key (company_id) references company (id) on delete restrict on update restrict;
create index ix_durable_goods_procurement__41 on durable_goods_procurement (company_id);
alter table durable_articles_procurement add constraint fk_durable_articles_procureme_42 foreign key (company_id) references company (id) on delete restrict on update restrict;
create index ix_durable_articles_procureme_42 on durable_articles_procurement (company_id);
alter table consumable_procurement add constraint fk_consumable_procurement_com_43 foreign key (company_id) references company (id) on delete restrict on update restrict;
create index ix_consumable_procurement_com_43 on consumable_procurement (company_id);
alter table durable_articles_procurement_detail add constraint fk_durable_articles_procureme_44 foreign key (fsn_description_id) references fsn_description (description_id) on delete restrict on update restrict;
create index ix_durable_articles_procureme_44 on durable_articles_procurement_detail (fsn_description_id);
alter table durable_articles_procurement_detail add constraint fk_durable_articles_procureme_45 foreign key (procurement_id) references durable_articles_procurement (id) on delete restrict on update restrict;
create index ix_durable_articles_procureme_45 on durable_articles_procurement_detail (procurement_id);
alter table consumable_procurement_detail add constraint fk_consumable_procurement_det_46 foreign key (fsn_description_id) references fsn_description (description_id) on delete restrict on update restrict;
create index ix_consumable_procurement_det_46 on consumable_procurement_detail (fsn_description_id);
alter table consumable_procurement_detail add constraint fk_consumable_procurement_det_47 foreign key (procurement_id) references consumable_procurement (id) on delete restrict on update restrict;
create index ix_consumable_procurement_det_47 on consumable_procurement_detail (procurement_id);
alter table durable_goods_procurement_detail add constraint fk_durable_goods_procurement__48 foreign key (fsn_description_id) references fsn_description (description_id) on delete restrict on update restrict;
create index ix_durable_goods_procurement__48 on durable_goods_procurement_detail (fsn_description_id);
alter table durable_goods_procurement_detail add constraint fk_durable_goods_procurement__49 foreign key (procurement_id) references durable_goods_procurement (id) on delete restrict on update restrict;
create index ix_durable_goods_procurement__49 on durable_goods_procurement_detail (procurement_id);
alter table durable_goods_requisition add constraint fk_durable_goods_requisition__50 foreign key (user_username) references user (username) on delete restrict on update restrict;
create index ix_durable_goods_requisition__50 on durable_goods_requisition (user_username);
alter table durable_goods_requisition add constraint fk_durable_goods_requisition__51 foreign key (approver_username) references user (username) on delete restrict on update restrict;
create index ix_durable_goods_requisition__51 on durable_goods_requisition (approver_username);
alter table consumable_requisition add constraint fk_consumable_requisition_use_52 foreign key (user_username) references user (username) on delete restrict on update restrict;
create index ix_consumable_requisition_use_52 on consumable_requisition (user_username);
alter table consumable_requisition add constraint fk_consumable_requisition_app_53 foreign key (approver_username) references user (username) on delete restrict on update restrict;
create index ix_consumable_requisition_app_53 on consumable_requisition (approver_username);
alter table durable_goods_requisition_detail add constraint fk_durable_goods_requisition__54 foreign key (withdrawer_username) references user (username) on delete restrict on update restrict;
create index ix_durable_goods_requisition__54 on durable_goods_requisition_detail (withdrawer_username);
alter table durable_goods_requisition_detail add constraint fk_durable_goods_requisition__55 foreign key (requisition_id) references durable_goods_requisition (id) on delete restrict on update restrict;
create index ix_durable_goods_requisition__55 on durable_goods_requisition_detail (requisition_id);
alter table consumable_requisition_detail add constraint fk_consumable_requisition_det_56 foreign key (withdrawer_username) references user (username) on delete restrict on update restrict;
create index ix_consumable_requisition_det_56 on consumable_requisition_detail (withdrawer_username);
alter table consumable_requisition_detail add constraint fk_consumable_requisition_det_57 foreign key (requisition_id) references consumable_requisition (id) on delete restrict on update restrict;
create index ix_consumable_requisition_det_57 on consumable_requisition_detail (requisition_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists durable_articles_ai_committee;

drop table if exists address;

drop table if exists durable_articles_auction;

drop table if exists auction_detail;

drop table if exists auction_d_committee;

drop table if exists auction_e_committee;

drop table if exists auction_ff_committee;

drop table if exists consumable_committee;

drop table if exists company;

drop table if exists consumable;

drop table if exists consumable_code;

drop table if exists consumable_type;

drop table if exists durable_articles_donation;

drop table if exists durable_articles_donation_detail;

drop table if exists donation_d_committee;

drop table if exists donation_ff_committee;

drop table if exists durable_articles;

drop table if exists durable_goods;

drop table if exists durable_articles_eo_committee;

drop table if exists consumable_eo_committee;

drop table if exists durable__goods_eo_committee;

drop table if exists external_transfer;

drop table if exists external_transfer_detail;

drop table if exists ex_transfer_d_committee;

drop table if exists ex_transfer_ff_committee;

drop table if exists fsn_class;

drop table if exists fsn_description;

drop table if exists fsn_group;

drop table if exists fsn_type;

drop table if exists internal_transfer;

drop table if exists internal_transfer_detail;

drop table if exists other_transfer;

drop table if exists other_transfer_detail;

drop table if exists other_transfer_d_committee;

drop table if exists other_transfer_ff_committee;

drop table if exists durable_goods_procurement;

drop table if exists durable_articles_procurement;

drop table if exists consumable_procurement;

drop table if exists durable_articles_procurement_detail;

drop table if exists consumable_procurement_detail;

drop table if exists durable_goods_procurement_detail;

drop table if exists durable_articles_repairing;

drop table if exists durable_articles_repairing_detail;

drop table if exists durable_goods_requisition;

drop table if exists consumable_requisition;

drop table if exists durable_goods_requisition_detail;

drop table if exists consumable_requisition_detail;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists durable_articles_ai_committee_seq;

drop sequence if exists address_seq;

drop sequence if exists durable_articles_auction_seq;

drop sequence if exists auction_detail_seq;

drop sequence if exists auction_d_committee_seq;

drop sequence if exists auction_e_committee_seq;

drop sequence if exists auction_ff_committee_seq;

drop sequence if exists consumable_committee_seq;

drop sequence if exists company_seq;

drop sequence if exists consumable_seq;

drop sequence if exists consumable_code_seq;

drop sequence if exists consumable_type_seq;

drop sequence if exists durable_articles_donation_seq;

drop sequence if exists durable_articles_donation_detail_seq;

drop sequence if exists donation_d_committee_seq;

drop sequence if exists donation_ff_committee_seq;

drop sequence if exists durable_articles_seq;

drop sequence if exists durable_goods_seq;

drop sequence if exists durable_articles_eo_committee_seq;

drop sequence if exists consumable_eo_committee_seq;

drop sequence if exists durable__goods_eo_committee_seq;

drop sequence if exists external_transfer_seq;

drop sequence if exists external_transfer_detail_seq;

drop sequence if exists ex_transfer_d_committee_seq;

drop sequence if exists ex_transfer_ff_committee_seq;

drop sequence if exists fsn_class_seq;

drop sequence if exists fsn_description_seq;

drop sequence if exists fsn_group_seq;

drop sequence if exists fsn_type_seq;

drop sequence if exists internal_transfer_seq;

drop sequence if exists internal_transfer_detail_seq;

drop sequence if exists other_transfer_seq;

drop sequence if exists other_transfer_detail_seq;

drop sequence if exists other_transfer_d_committee_seq;

drop sequence if exists other_transfer_ff_committee_seq;

drop sequence if exists durable_goods_procurement_seq;

drop sequence if exists durable_articles_procurement_seq;

drop sequence if exists consumable_procurement_seq;

drop sequence if exists durable_articles_procurement_detail_seq;

drop sequence if exists consumable_procurement_detail_seq;

drop sequence if exists durable_goods_procurement_detail_seq;

drop sequence if exists durable_articles_repairing_seq;

drop sequence if exists durable_articles_repairing_detail_seq;

drop sequence if exists durable_goods_requisition_seq;

drop sequence if exists consumable_requisition_seq;

drop sequence if exists durable_goods_requisition_detail_seq;

drop sequence if exists consumable_requisition_detail_seq;

drop sequence if exists user_seq;

