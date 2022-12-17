create table chats (
    id bigserial primary key not null unique,
    client_type varchar not null,
    client_specific_id varchar not null,
    unique (client_type, client_specific_id)
);

create table chat_members (
    id bigserial primary key not null unique,
    client_type varchar not null,
    client_specific_id varchar not null,
    chat_id bigint not null references chats(id)
);

create table chat_member_server_identifiers (
    id bigserial primary key not null unique,
    chat_member_id bigint not null references chat_members(id),
    player_identifier varchar,
    server_name varchar not null
);