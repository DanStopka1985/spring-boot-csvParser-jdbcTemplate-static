--searching name_rus in inventory.inn.name_rus and set new_id where not found

update public.loader_little_files_mnn a set id = b.id
from inventory.inn b where a.name_rus = b.name_rus;

alter table public.loader_little_files_mnn add column new_id integer;

update public.loader_little_files_mnn a set new_id = nextval('inventory.inn_seq')
where id is null;

--adding new inn
insert into inventory.inn(id, name_rus, name_latin, code)
select new_id, name_rus, name_latin, code from public.loader_little_files_mnn a where new_id is not null;