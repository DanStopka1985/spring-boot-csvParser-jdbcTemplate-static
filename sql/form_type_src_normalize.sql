delete from loader_little_files_form_type where short_name is null;

delete from loader_little_files_form_type where full_name is null;

drop table if exists loader_little_files_form_type1;

create table loader_little_files_form_type1 as
select *, row_number() over(partition by short_name order by full_name) rn from loader_little_files_form_type;

delete from loader_little_files_form_type1 where rn > 1;

drop table if exists loader_little_files_form_type;

create table loader_little_files_form_type as
select short_name, full_name from loader_little_files_form_type1;