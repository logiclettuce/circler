create procedure addUser(p_client_id varchar, p_chat_id varchar, p_client_type varchar, inout p_result boolean)
    language plpgsql
as
$$
declare
    v_chat_insert boolean;
    v_user_exists boolean;
begin
    select count(cm.id)
    into v_user_exists
    from chat_members cm
    where cm.client_type = p_client_type
      and cm.client_specific_id = p_client_id;

    if v_user_exists then return; end if;

    call addChat(p_chat_id, p_client_type, v_chat_insert);
    insert into chat_members(client_specific_id, client_type, chat_id)
    values (p_client_id, p_client_type, (select c.id from chats c where c.client_specific_id = p_chat_id and c.client_type = p_client_type));
end;
$$;

create procedure addChat(p_client_id varchar, p_client_type varchar, inout p_result boolean)
    language plpgsql
as
$$
declare
    v_chat_exists boolean;
begin
    select count(c.id)
    into v_chat_exists
    from chats c
    where c.client_specific_id = p_client_id
      and c.client_type = p_client_type;
    if not v_chat_exists then
        insert into chats(client_specific_id, client_type) values (p_client_id, p_client_type);
        select true into p_result;
        return;
    end if;
    select false into p_result;
end;
$$;

create procedure setPlayerIdentifier(p_new_identifier varchar,
                                     p_server_name varchar,
                                     p_client_id varchar,
                                     p_chat_id varchar,
                                     p_client_type varchar)
    language plpgsql
as
$$
declare
    dummy_result                    boolean;
    v_chat_member_exists            boolean;
    v_chat_member_identifier_exists boolean;
begin
    select count(cm.id)
    into v_chat_member_exists
    from chat_members cm
             join chats c on cm.chat_id = c.id
    where cm.client_specific_id = p_client_id
      and c.client_specific_id = p_chat_id
      and cm.client_type = p_client_type;

    if not v_chat_member_exists then
        call adduser(p_client_id, p_chat_id, p_client_type, dummy_result);
    end if;
    raise notice '%', v_chat_member_exists;


    select count(cmsi.player_identifier)
    into v_chat_member_identifier_exists
    from chat_member_server_identifiers cmsi
             join chat_members cm on cmsi.chat_member_id = cm.id
             join chats c on c.id = cm.chat_id
    where cm.client_specific_id = p_client_id
      and c.client_specific_id = p_chat_id
      and c.client_type = p_client_type;
    if v_chat_member_identifier_exists then
        update chat_member_server_identifiers
        set player_identifier = p_new_identifier
--         get the right id for update
        where id = (select cmsi.id
                    from chat_member_server_identifiers cmsi
                             join chat_members cm on cmsi.chat_member_id = cm.id
                             join chats c on c.id = cm.chat_id
                    where cm.client_specific_id = p_client_id
                      and c.client_specific_id = p_chat_id
                      and c.client_type = p_client_type);
        return;
    end if;
    insert into chat_member_server_identifiers(chat_member_id, player_identifier, server_name)
    values ((select cm.id as id
             from chat_members cm
                      join chats c on cm.chat_id = c.id
             where cm.client_specific_id = p_client_id
               and c.client_specific_id = p_chat_id),
            p_new_identifier,
            p_server_name);
end;
$$;