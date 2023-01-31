-- profile templates
alter table chats
    rename column user_profile_template to html_profile_template;
alter table chats
add text_profile_template varchar not null default '';


-- score templates
alter table chats
    add html_score_template varchar not null default '';
alter table chats
    add text_score_template varchar not null default '';

