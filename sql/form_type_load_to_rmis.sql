
alter table loader_little_files_form_type add column id integer, add column new_id integer;


update loader_little_files_form_type a set id = b.id
from inventory.form_type b where b.short_name = a.short_name;

update loader_little_files_form_type set new_id = nextval('inventory.form_type_seq')
where id is null;

insert into inventory.form_type(id, short_name, full_name)
select new_id, short_name, full_name from loader_little_files_form_type where new_id is not null;