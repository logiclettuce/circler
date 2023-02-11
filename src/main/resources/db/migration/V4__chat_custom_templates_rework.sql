alter table chats drop column html_profile_template;
alter table chats drop column text_profile_template;
alter table chats drop column text_score_template;
alter table chats drop column html_score_template;

create table chat_templates(
    id bigserial primary key not null unique,
    chat_id bigserial references chats(id) not null,
    type varchar not null,
    format varchar not null,
    template varchar not null default '',
    unique(type, format, chat_id)
);

