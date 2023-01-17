create table chats
(
    id          bigserial primary key not null unique,
    client_type varchar               not null,
    client_id   varchar               not null,
    unique (client_type, client_id)
);

create table chat_members
(
    id        bigserial primary key not null unique,
    client_id varchar               not null,
    chat_id   bigserial             not null references chats (id),
    unique (client_id, chat_id)
);

create table chat_member_server_identifiers
(
    id                bigserial primary key not null unique,
    chat_member_id    bigserial             not null references chat_members (id),
    player_identifier varchar,
    server            varchar               not null,
    unique (chat_member_id, server)
);