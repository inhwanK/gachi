
explain
select
    q.qs_idx,
    q.qs_title,
    q.qs_general_content,
    q.qs_code_content,
    q.qs_error_content,
    q.qs_enabled,
    q.qs_enabled,
    q.qs_solved,
    q.user_idx
from gachi_q q
where q.qs_general_content like '%ge%' or qs_code_content like '%ge%' or qs_error_content like '%ge%';

explain
select *
from gachi_q q
where q.qs_general_content like '%ge%' or qs_code_content like '%ge%' or qs_error_content like '%ge%';

explain
select *
from gachi_q q
where q.qs_general_content like '%ge%';