create sequence hibernate_sequence start 1 increment 1;
create table brands (
    name varchar(255) not null,
    primary key (name)
);
create table categories (
    id int8 not null,
    level int4,
    name varchar(255),
    ancestor_id int8,
    descendant_id int8,
    primary key (id)
);
create table colors (
    rgb varchar(255) not null,
    primary key (rgb)
);
create table count_and_price (
    id int8 not null,
    count int4,
    price float8,
    primary key (id)
);
create table images (
    id int8 not null,
    name varchar(255),
    product_id int8,
    primary key (id)
);
create table message (
    id int8 not null,
    filename varchar(255),
    name varchar(255) not null,
    tag varchar(255),
    text varchar(2048) not null,
    user_id int4,
    primary key (id)
);
create table order_product_mapping (
    order_id int8 not null,
    count_and_price_id int8 not null,
    product_id int8 not null,
    primary key (order_id, product_id)
);
create table orders (
    id int8 not null,
    placed_at timestamp,
    total_price float8,
    user_id int4,
    primary key (id)
);
create table orders_products (
    order_id int8 not null,
    products_id int8 not null,
    primary key (order_id, products_id)
);
create table payment (
    id int8 not null,
    cccvv varchar(255),
    cc_expiration varchar(255),
    cc_number varchar(255),
    primary key (id)
);
create table product (
    id int8 not null,
    description varchar(2048),
    filename varchar(255),
    hover_filename varchar(255),
    name varchar(30),
    new_product boolean not null,
    price float8 not null check (price>=0),
    sale int4 check (sale<=100 AND sale>=0),
    brand_name varchar(255),
    category_id int8,
    color_rgb varchar(255),
    primary key (id)
);
create table product_orders (
    product_id int8 not null,
    orders_id int8 not null,
    primary key (product_id, orders_id)
);
create table user_role (
    user_id int4 not null,
    roles varchar(255)
);
create table usr (
    id int4 not null,
    activation_code varchar(255),
    active boolean not null,
    address varchar(255),
    email varchar(255),
    first_name varchar(255),
    last_name varchar(255),
    password varchar(255),
    phone_number varchar(255),
    post_code varchar(255),
    username varchar(255),
    payment_id int8,
    primary key (id)
);
alter table if exists order_product_mapping
    add constraint UK_order_product_mapping_as_count_and_price_id
        unique (count_and_price_id);

alter table if exists categories
    add constraint FK_categories_ancestor_id
        foreign key (ancestor_id) references categories;

alter table if exists categories
    add constraint FK_categories_descendant_id
        foreign key (descendant_id) references categories;

alter table if exists images
    add constraint FK_images_product_id
        foreign key (product_id) references product;

alter table if exists message
    add constraint FK_message_user_id
        foreign key (user_id) references usr;

alter table if exists order_product_mapping
    add constraint FK_order_product_mapping_count_and_price_id
        foreign key (count_and_price_id) references count_and_price;

alter table if exists order_product_mapping
    add constraint FK_order_product_mapping_product_id
        foreign key (product_id) references product;

alter table if exists order_product_mapping
    add constraint FK_order_product_mapping_order_id
        foreign key (order_id) references orders;

alter table if exists orders
    add constraint FK_orders_user_id
        foreign key (user_id) references usr;

alter table if exists orders_products
    add constraint FK_orders_products_products_id
        foreign key (products_id) references product;

alter table if exists orders_products
    add constraint FK_orders_products_order_id
        foreign key (order_id) references orders;

alter table if exists product
    add constraint FK_product_brand_name
        foreign key (brand_name) references brands;

alter table if exists product
    add constraint FK_category_id_product
        foreign key (category_id) references categories;

alter table if exists product
    add constraint FK_color_rgb_product_orders
        foreign key (color_rgb) references colors;

alter table if exists product_orders
    add constraint FK_product_orders_orders_id
        foreign key (orders_id) references orders;

alter table if exists product_orders
    add constraint FK_product_orders_product_id
        foreign key (product_id) references product;

alter table if exists user_role
    add constraint FK_user_role_user_id
        foreign key (user_id) references usr;

alter table if exists usr
    add constraint FK_usr_payment_id
        foreign key (payment_id) references payment;