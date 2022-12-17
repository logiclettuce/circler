create procedure addUser(p_client_id varchar, p_chat_id varchar, p_client_type varchar, inout p_result boolean)
    language plpgsql
as
$$
declare
    v_chat_insert boolean;
begin
    select count(*) from chat_members cm where cm.client_type = p_client_type and cm.client_specific_id = p_client_id;
    if found then return; end if;
    select count(*) from chats c where c.client_type = p_client_type and c.client_specific_id = p_chat_id;
    if not found then
        call addChat(p_chat_id, p_client_type, v_chat_insert);
        if v_chat_insert then
            insert into chat_members(client_specific_id, client_type, chat_id)
            values (p_client_id, p_client_type, p_chat_id);
        end if;
    end if;
end;
$$;

create procedure addChat(p_client_id varchar, p_client_type varchar, inout p_result boolean)
    language plpgsql
as
$$
begin
    select c.id from chats c where c.client_specific_id = p_client_id and c.client_type = p_client_type;
    if not found then
        insert into chats(client_specific_id, client_type) values (p_client_id, p_client_type);
        select true into p_result;
        return;
    end if;
    select false into p_result;
end;
$$;