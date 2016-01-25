--searching in inventory.commodity_group
update public.loader_little_files_gr_buh_uch a set id = b.id
from inventory.commodity_group b where a.name = b.name;

alter table public.loader_little_files_gr_buh_uch add column new_id integer;

update public.loader_little_files_gr_buh_uch set new_id = nextval('inventory.commodity_group_seq') where id is null;

--adding new
insert into inventory.commodity_group(id, name)
select new_id, name from public.loader_little_files_gr_buh_uch where new_id is not null;