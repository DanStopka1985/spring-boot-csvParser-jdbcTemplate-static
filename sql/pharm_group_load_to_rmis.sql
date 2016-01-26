alter table public.loader_little_files_pharm_group add column new_id integer;

update public.loader_little_files_pharm_group set new_id = nextval('inventory.holding_pharm_group_seq') where id is null;

insert into inventory.pharm_group(id, name)
select new_id, name from public.loader_little_files_pharm_group where new_id is not null;

update public.loader_little_files_pharm_group set id = new_id where id is null;

alter table public.loader_little_files_pharm_group drop column new_id;


drop table if exists public.loader_little_files_pharm_group1;

create table public.loader_little_files_pharm_group1 as
select a.name, a.id, b.id parent_id from public.loader_little_files_pharm_group a
left join public.loader_little_files_pharm_group b on a.parent_uid = b.uid;

update inventory.pharm_group a set parent_id = b.parent_id
from public.loader_little_files_pharm_group1 b where a.id = b.id;