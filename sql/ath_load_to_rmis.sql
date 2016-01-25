--defining parent codes
create table public.loader_little_files_ath1 as
select
 own_code,
 (select b.own_code from public.loader_little_files_ath b where a.own_code != b.own_code and position(b.own_code in a.own_code) = 1 order by length(b.own_code) desc limit 1) parent_own_code
from public.loader_little_files_ath a
;

update public.loader_little_files_ath a set parent_own_code = b.parent_own_code
from public.loader_little_files_ath1 b where a.own_code = b.own_code;

--searching own_codes in db
update public.loader_little_files_ath a set id = b.id
from inventory.atc b where a.own_code = b.code;

--creating new id
alter table public.loader_little_files_ath add column new_id integer;
update public.loader_little_files_ath set new_id = nextval('inventory.holding_atc_seq') where id is null;

--adding new records
insert into inventory.atc(id, code, name)
select new_id, own_code, name from public.loader_little_files_ath where new_id is not null;

--searching parent in db
update public.loader_little_files_ath a set parent_id = b.id
from inventory.atc b where a.parent_own_code = b.code;

--updating parent_id in db, where null
update inventory.atc a set parent_id = b.parent_id
from public.loader_little_files_ath b
where a.id = coalesce(b.id, b.new_id) and a.parent_id is null;