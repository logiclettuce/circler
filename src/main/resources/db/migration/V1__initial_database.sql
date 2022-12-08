create table chats (
    id bigint primary key not null unique,
    client_id varchar unique not null
);

create table chat_members (
    id bigint primary key not null unique,
    client_id varchar not null,
    player_identifier varchar not null,
    chat_id bigint references chats(id)
);