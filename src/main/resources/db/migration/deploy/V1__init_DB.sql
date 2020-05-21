create sequence hibernate_sequence start 1 increment 1;

create table brands (
    name varchar(255) not null,
    filename varchar(255),
    primary key (name)
);

create table categories (
    id int8 not null,
    level int4,
    name varchar(255),
    ancestor_id int8,
    descendant_id int8 not null,
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
    placed_at timestamp,
    rating int4,
    subject varchar(256) not null,
    text varchar(1024) not null,
    user_id int4,
    product_id int8,
    primary key (id)
);

create table order_product_mapping (
    order_id int8 not null,
    count_and_price_id int8 not null,
    product_id int8 not null,
    primary key (order_id, product_id)
);

create table orders (id int8 not null,
    active int4,
    placed_at timestamp,
    total_price float8,
    user_id int4,
    primary key (id)
);

create table orders_params (
    order_id int8 not null,
    params_id int8 not null
);

create table orders_products (
    order_id int8 not null,
    products_id int8 not null,
    primary key (order_id, products_id)
);

create table params (
    id int8 not null,
    color_rgb varchar(255),
    product_id int8,
    size_size varchar(255),
    primary key (id)
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
    sale int4 check (sale>=0 AND sale<=100),
    brand_name varchar(255),
    category_id int8,
    primary key (id)
);

create table product_color (
    product_id int8 not null,
    color_rgb varchar(255) not null,
    primary key (product_id, color_rgb)
);

create table product_orders (
    product_id int8 not null,
    orders_id int8 not null,
    primary key (product_id, orders_id)
);

create table product_sizes (
    product_id int8 not null,
    sizes_size varchar(255) not null,
    primary key (product_id, sizes_size)
);

create table sizes (
    size varchar(255) not null,
    primary key (size)
);

create table slide (
    id int4 not null,
    filename varchar(255),
    product_id int8,
    primary key (id)
);

create table user_products (
    user_id int4 not null,
    product_id int8 not null,
    primary key (user_id, product_id)
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

create table usr_orders (
    user_id int4 not null,
    orders_id int8 not null,
    primary key (user_id, orders_id)
);

alter table if exists order_product_mapping
    add constraint UK_order_product_mapping__count_and_price_id
    unique (count_and_price_id);

alter table if exists orders_params
    add constraint UK_orders_params__params_id
    unique (params_id);

alter table if exists usr_orders
    add constraint UK_usr_orders__orders_id
    unique (orders_id);

alter table if exists categories
    add constraint FK_categories__ancestor_id__ref__categories
    foreign key (ancestor_id) references categories;

alter table if exists categories
    add constraint FK_categories__descendant_id__ref__categories
    foreign key (descendant_id) references categories;

alter table if exists images
    add constraint FK_images__product_id__ref__product
    foreign key (product_id) references product;

alter table if exists message
    add constraint FK_message__user_id__ref__usr
    foreign key (user_id) references usr;

alter table if exists message
    add constraint FK_message__product_id__ref__product
    foreign key (product_id) references product;

alter table if exists order_product_mapping
    add constraint FK_order_product_mapping__count_and_price_id__ref__count_and_price
    foreign key (count_and_price_id) references count_and_price;

alter table if exists order_product_mapping
    add constraint FK__order_product_mapping__product_id__ref__product
    foreign key (product_id) references product;

alter table if exists order_product_mapping
    add constraint FK_order_product_mapping__order_id__ref__orders
    foreign key (order_id) references orders;

alter table if exists orders
    add constraint FK_orders__user_id__ref__usr
    foreign key (user_id) references usr;

alter table if exists orders_params
    add constraint FK_orders_params__params_id__ref__params
    foreign key (params_id) references params;

alter table if exists orders_params
    add constraint FK_orders_params__order_id__ref__orders
    foreign key (order_id) references orders;

alter table if exists orders_products
    add constraint FK__orders_products__products_id__ref__product
    foreign key (products_id) references product;

alter table if exists orders_products
    add constraint FK_orders_products__order_id__ref__orders
    foreign key (order_id) references orders;

alter table if exists params
    add constraint FK_params__color_rgb__ref__colors
    foreign key (color_rgb) references colors;

alter table if exists params
    add constraint FK_params__product_id__ref__prodcut
    foreign key (product_id) references product;

alter table if exists params
    add constraint FK_params__size_size__ref__sizes
    foreign key (size_size) references sizes;

alter table if exists product
    add constraint FK_product__brand_name__ref__brands
    foreign key (brand_name) references brands;

alter table if exists product
    add constraint FK_product__category_id__ref__categories
    foreign key (category_id) references categories;

alter table if exists product_color
    add constraint FK_product_color__color_rgb__ref__colors
    foreign key (color_rgb) references colors;

alter table if exists product_color
    add constraint FK_product_color__product_id__ref__product
    foreign key (product_id) references product;

alter table if exists product_orders
    add constraint FK_product_orders__orders_id__ref__orders
    foreign key (orders_id) references orders;

alter table if exists product_orders
    add constraint FK_product_orders__product_id__ref__product
    foreign key (product_id) references product;

alter table if exists product_sizes
    add constraint FK_product_sizes__sizes_size__ref__sizes
    foreign key (sizes_size) references sizes;

alter table if exists product_sizes
    add constraint FK_product_sizes__product_id__ref__product
    foreign key (product_id) references product;

alter table if exists slide
    add constraint FK_slide__product_id__ref__product
    foreign key (product_id) references product;

alter table if exists user_products
    add constraint FK_user_products__product_id__ref__product
    foreign key (product_id) references product;

alter table if exists user_products
    add constraint FK_user_products__user_id__ref__usr
    foreign key (user_id) references usr;

alter table if exists user_role
    add constraint FK_user_role__user_id__ref__usr
    foreign key (user_id) references usr;

alter table if exists usr
    add constraint FK_usr__payment_id__ref__payment
    foreign key (payment_id) references payment;

alter table if exists usr_orders
    add constraint FK_usr_orders__orders_id__ref__orders
    foreign key (orders_id) references orders;

alter table if exists usr_orders
    add constraint FK_usr_orders__user_id__ref__usr
    foreign key (user_id) references usr;


insert into usr (id, username, password, active)
        values (1, 'admin', 'admin', true);
insert into user_role(user_id, roles)
        values (1, 'ROLE_USER'), (1, 'ROLE_ADMIN');


create extension  if not exists pgcrypto;

update usr set password = crypt(password, gen_salt('bf', 8));
