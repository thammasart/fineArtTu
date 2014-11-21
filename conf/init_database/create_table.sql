create table durable_articles_ai_committee (
  id                        bigint not null AUTO_INCREMENT,
  employees_type            varchar(255),
  committee_position        varchar(255),
  committee_username        varchar(255),
  procurement_id            bigint,
  constraint pk_durable_articles_ai_committee primary key (id))
;

create table durable_goods_ai_committee (
  id                        bigint not null AUTO_INCREMENT,
  employees_type            varchar(255),
  committee_position        varchar(255),
  committee_username        varchar(255),
  procurement_id            bigint,
  constraint pk_durable_goods_ai_committee primary key (id))
;

create table address (
  id                        bigint not null AUTO_INCREMENT,
  building_no               varchar(255),
  village                   varchar(255),
  alley                     varchar(255),
  road                      varchar(255),
  parish                    varchar(255),
  district                  varchar(255),
  province                  varchar(255),
  telephone_number          varchar(255),
  fax                       varchar(255),
  post_code                 varchar(255),
  email                     varchar(255),
  constraint pk_address primary key (id))
;

create table auction (
  id                        bigint not null AUTO_INCREMENT,
  title                     varchar(255),
  contract_no               varchar(255),
  total_price               double,
  approve_date              timestamp,
  status                    integer,
  company_id                bigint,
  constraint ck_auction_status check (status in (0,1,2,3,4,5)),
  constraint pk_auction primary key (id))
;

create table auction_detail (
  id                        bigint not null AUTO_INCREMENT,
  durable_articles_id       bigint,
  auction_id                bigint,
  constraint pk_auction_detail primary key (id))
;

create table auction_d_committee (
  id                        bigint not null AUTO_INCREMENT,
  employees_type            varchar(255),
  committee_position        varchar(255),
  user_username             varchar(255),
  auction_id                bigint,
  constraint pk_auction_d_committee primary key (id))
;

create table auction_e_committee (
  id                        bigint not null AUTO_INCREMENT,
  employees_type            varchar(255),
  committee_position        varchar(255),
  user_username             varchar(255),
  auction_id                bigint,
  constraint pk_auction_e_committee primary key (id))
;

create table auction_ff_committee (
  id                        bigint not null AUTO_INCREMENT,
  employees_type            varchar(255),
  committee_position        varchar(255),
  user_username             varchar(255),
  auction_id                bigint,
  constraint pk_auction_ff_committee primary key (id))
;

create table borrow (
  id                        bigint not null AUTO_INCREMENT,
  title                     varchar(255),
  number                    varchar(255),
  date_of_start_borrow      timestamp,
  date_of_end_borrow        timestamp,
  description               varchar(255),
  status                    integer,
  user_username             varchar(255),
  approver_username         varchar(255),
  constraint ck_borrow_status check (status in (0,1,2,3,4,5)),
  constraint pk_borrow primary key (id))
;

create table borrow_detail (
  id                        bigint not null AUTO_INCREMENT,
  durable_articles_id       bigint,
  borrow_id                 bigint,
  constraint pk_borrow_detail primary key (id))
;

create table committee (
  identification_no         varchar(255) not null,
  title                     varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  position                  varchar(255),
  constraint pk_committee primary key (identification_no))
;

create table company (
  id                        bigint not null AUTO_INCREMENT,
  type_entrepreneur         varchar(255),
  typedealer                varchar(255),
  name_entrepreneur         varchar(255),
  name_dealer               varchar(255),
  pay_codition              varchar(255),
  pay_period                integer,
  send_period               integer,
  durable_articles_type     varchar(255),
  durable_goods_type        varchar(255),
  consumable_goods_type     varchar(255),
  other_detail              varchar(255),
  file_name                 varchar(255),
  path                      varchar(255),
  file_type                 varchar(255),
  address_id                bigint,
  constraint pk_company primary key (id))
;

create table donation (
  id                        bigint not null AUTO_INCREMENT,
  title                     varchar(255),
  contract_no               varchar(255),
  approve_date              timestamp,
  status                    integer,
  company_id                bigint,
  constraint ck_donation_status check (status in (0,1,2,3,4,5)),
  constraint pk_donation primary key (id))
;

create table donation_detail (
  id                        bigint not null AUTO_INCREMENT,
  durable_articles_id       bigint,
  donation_id               bigint,
  constraint pk_donation_detail primary key (id))
;

create table donation_d_committee (
  id                        bigint not null AUTO_INCREMENT,
  employees_type            varchar(255),
  committee_position        varchar(255),
  user_username             varchar(255),
  donation_id               bigint,
  constraint pk_donation_d_committee primary key (id))
;

create table donation_ff_committee (
  id                        bigint not null AUTO_INCREMENT,
  employees_type            varchar(255),
  committee_position        varchar(255),
  user_username             varchar(255),
  donation_id               bigint,
  constraint pk_donation_ff_committee primary key (id))
;

create table durable_articles (
  id                        bigint not null AUTO_INCREMENT,
  department                varchar(255),
  room                      varchar(255),
  floor_level               varchar(255),
  code                      varchar(255),
  title                     varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  code_from_stock           varchar(255),
  status                    integer,
  bar_code                  varchar(255),
  detail_id                 bigint,
  constraint ck_durable_articles_status check (status in (0,1,2,3,4,5,6,7,8)),
  constraint pk_durable_articles primary key (id))
;

create table durable_goods (
  id                        bigint not null AUTO_INCREMENT,
  department                varchar(255),
  room                      varchar(255),
  floor_level               varchar(255),
  codes                     varchar(255),
  title                     varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  type_of_durable_goods     integer,
  bar_code                  varchar(255),
  status                    integer,
  detail_id                 bigint,
  constraint ck_durable_goods_status check (status in (0,1,2,3,4,5,6,7,8)),
  constraint pk_durable_goods primary key (id))
;

create table durable_articles_eo_committee (
  id                        bigint not null AUTO_INCREMENT,
  employees_type            varchar(255),
  committee_position        varchar(255),
  committee_username        varchar(255),
  procurement_id            bigint,
  constraint pk_durable_articles_eo_committee primary key (id))
;

create table fsn_class (
  class_id                  varchar(4) not null,
  class_description         varchar(255) not null,
  group_group_id            varchar(2),
  constraint pk_fsn_class primary key (class_id))
;

create table fsn_description (
  description_id            varchar(13) not null,
  description_description   varchar(255) not null,
  classifier                varchar(255),
  other_detail              varchar(255),
  remain                    integer,
  price_per_each            double,
  file_name                 varchar(255),
  path                      varchar(255),
  file_type                 varchar(255),
  typ_type_id               varchar(8),
  constraint pk_fsn_description primary key (description_id))
;

create table fsn_group (
  group_id                  varchar(2) not null,
  group_description         varchar(255) not null,
  constraint pk_fsn_group primary key (group_id))
;

create table fsn_type (
  type_id                   varchar(8) not null,
  type_description          varchar(255) not null,
  group_class_class_id      varchar(4),
  constraint pk_fsn_type primary key (type_id))
;

create table internal_transfer (
  id                        bigint not null AUTO_INCREMENT,
  title                     varchar(255),
  number                    varchar(255),
  approve_date              timestamp,
  status                    integer,
  approver_username         varchar(255),
  constraint ck_internal_transfer_status check (status in (0,1,2,3,4,5)),
  constraint pk_internal_transfer primary key (id))
;

create table internal_transfer_detail (
  id                        bigint not null AUTO_INCREMENT,
  first_department          varchar(255),
  first_room                varchar(255),
  first_floor_level         varchar(255),
  first_first_name          varchar(255),
  first_last_name           varchar(255),
  new_department            varchar(255),
  new_room                  varchar(255),
  new_floor_level           varchar(255),
  new_first_name            varchar(255),
  new_last_name             varchar(255),
  new_position              varchar(255),
  durable_articles_id       bigint,
  internal_transfer_id      bigint,
  constraint pk_internal_transfer_detail primary key (id))
;

create table material_code (
  code                      varchar(255) not null,
  description               varchar(255) not null,
  classifier                varchar(255),
  min_number_to_alert       integer,
  other_detail              varchar(255),
  remain                    integer,
  price_per_each            double,
  file_name                 varchar(255),
  path                      varchar(255),
  file_type                 varchar(255),
  material_type_pre_code_id varchar(255),
  constraint pk_material_code primary key (code))
;

create table material_type (
  pre_code_id               varchar(255) not null,
  type_name                 varchar(255) not null,
  acronym                   varchar(255) not null,
  constraint pk_material_type primary key (pre_code_id))
;

create table order_goods (
  id                        bigint not null AUTO_INCREMENT,
  title                     varchar(255),
  number                    varchar(255),
  approve_date              timestamp,
  status                    integer,
  user_username             varchar(255),
  approver_username         varchar(255),
  constraint ck_order_goods_status check (status in (0,1,2,3,4,5)),
  constraint pk_order_goods primary key (id))
;

create table order_goods_detail (
  id                        bigint not null AUTO_INCREMENT,
  description               varchar(255),
  department                varchar(255),
  room                      varchar(255),
  floor_level               varchar(255),
  codes                     varchar(255),
  title                     varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  position                  varchar(255),
  goods_id                  bigint,
  withdrawer_username       varchar(255),
  order_id                  bigint,
  constraint pk_order_goods_detail primary key (id))
;

create table other_transfer (
  id                        bigint not null AUTO_INCREMENT,
  title                     varchar(255),
  number                    varchar(255),
  approve_date              timestamp,
  description               varchar(255),
  status                    integer,
  approver_username         varchar(255),
  constraint ck_other_transfer_status check (status in (0,1,2,3,4,5)),
  constraint pk_other_transfer primary key (id))
;

create table other_transfer_detail (
  id                        bigint not null AUTO_INCREMENT,
  durable_articles_id       bigint,
  other_transfer_id         bigint,
  constraint pk_other_transfer_detail primary key (id))
;

create table other_d_committee (
  id                        bigint not null AUTO_INCREMENT,
  employees_type            varchar(255),
  committee_position        varchar(255),
  user_username             varchar(255),
  other_transfer_id         bigint,
  constraint pk_other_d_committee primary key (id))
;

create table other_ff_committee (
  id                        bigint not null AUTO_INCREMENT,
  employees_type            varchar(255),
  committee_position        varchar(255),
  user_username             varchar(255),
  other_transfer_id         bigint,
  constraint pk_other_ff_committee primary key (id))
;

create table durable_goods_procurement (
  id                        bigint not null AUTO_INCREMENT,
  title                     varchar(255),
  contract_no               varchar(255),
  add_date                  timestamp,
  check_date                timestamp,
  budget_type               varchar(255),
  budget_year               integer,
  status                    integer,
  file_name                 varchar(255),
  file_type                 varchar(255),
  path                      varchar(255),
  bar_code                  varchar(255),
  company_id                bigint,
  constraint ck_durable_goods_procurement_status check (status in (0,1,2,3,4)),
  constraint pk_durable_goods_procurement primary key (id))
;

create table durable_articles_procurement (
  id                        bigint not null AUTO_INCREMENT,
  title                     varchar(255),
  contract_no               varchar(255),
  add_date                  timestamp,
  check_date                timestamp,
  budget_type               varchar(255),
  budget_year               integer,
  status                    integer,
  file_name                 varchar(255),
  file_type                 varchar(255),
  path                      varchar(255),
  bar_code                  varchar(255),
  company_id                bigint,
  constraint ck_durable_articles_procurement_status check (status in (0,1,2,3,4)),
  constraint pk_durable_articles_procurement primary key (id))
;

create table durable_articles_procurement_detail (
  id                        bigint not null AUTO_INCREMENT,
  description               varchar(255),
  price                     double,
  price_no_vat              double,
  depreciation_price        double,
  depreciation_of_year      double,
  quantity                  integer,
  llife_time                double,
  alert_time                double,
  seller                    varchar(255),
  phone                     varchar(255),
  brand                     varchar(255),
  serial_number             varchar(255),
  status                    integer,
  fsn_description_id        varchar(13),
  procurement_id            bigint,
  constraint ck_durable_articles_procurement_detail_status check (status in (0,1,2,3)),
  constraint pk_durable_articles_procurement_ primary key (id))
;

create table durable_goods_procurement_detail (
  id                        bigint not null AUTO_INCREMENT,
  code                      varchar(255),
  description               varchar(255),
  price                     double,
  price_no_vat              double,
  quantity                  integer,
  remain                    integer,
  seller                    varchar(255),
  phone                     varchar(255),
  brand                     varchar(255),
  serial_number             varchar(255),
  part_of_pic               varchar(255),
  type_of_durable_goods     integer,
  status                    integer,
  procurement_id            bigint,
  constraint ck_durable_goods_procurement_detail_status check (status in (0,1,2,3)),
  constraint pk_durable_goods_procurement_det primary key (id))
;

create table repairing (
  id                        bigint not null AUTO_INCREMENT,
  title                     varchar(255),
  number                    varchar(255),
  date_of_sent_to_repair    timestamp,
  date_of_receive_from_repair timestamp,
  repair_costs              double,
  status                    integer,
  company_id                bigint,
  approver_username         varchar(255),
  constraint ck_repairing_status check (status in (0,1,2,3,4,5)),
  constraint pk_repairing primary key (id))
;

create table repairing_detail (
  id                        bigint not null AUTO_INCREMENT,
  description               varchar(255),
  price                     double,
  durable_articles_id       bigint,
  repairing_id              bigint,
  constraint pk_repairing_detail primary key (id))
;

create table requisition (
  id                        bigint not null AUTO_INCREMENT,
  title                     varchar(255),
  number                    varchar(255),
  approve_date              timestamp,
  status                    integer,
  user_username             varchar(255),
  approver_username         varchar(255),
  constraint ck_requisition_status check (status in (0,1,2,3,4,5)),
  constraint pk_requisition primary key (id))
;

create table requisition_detail (
  id                        bigint not null AUTO_INCREMENT,
  quantity                  integer,
  price                     double,
  totle_price               double,
  description               varchar(255),
  year                      integer,
  status                    integer,
  code_code                 varchar(255),
  withdrawer_username       varchar(255),
  requisition_id            bigint,
  constraint ck_requisition_detail_status check (status in (0,1,2,3,4,5)),
  constraint pk_requisition_detail primary key (id))
;

create table user (
  username                  varchar(255) not null,
  password                  varchar(255) not null,
  departure                 varchar(255),
  position                  varchar(255),
  name_prefix               varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  status_name               varchar(255),
  constraint pk_user primary key (username))
;

create table user_status (
  name                      varchar(255) not null,
  module1                   boolean,
  module2                   boolean,
  module3                   boolean,
  module4                   boolean,
  module5                   boolean,
  module6                   boolean,
  constraint pk_user_status primary key (name))
;
