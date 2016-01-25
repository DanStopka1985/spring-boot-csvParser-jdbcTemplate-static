
--deleting outer of constraints

--name_rus is null
delete from public.loader_little_files_mnn where name_rus is null;

--name_latin is null
delete from public.loader_little_files_mnn where name_latin is null;


--name_rus suppose unique - cancel
--adding row number of name_rus part value
--then drop doubles by name_rus

drop table if exists public.loader_little_files_mnn1;
create table public.loader_little_files_mnn1 as
select *, row_number() over(partition by name_rus order by code) rn from public.loader_little_files_mnn;

drop table if exists public.loader_little_files_mnn;
create table public.loader_little_files_mnn as
select * from public.loader_little_files_mnn1;
drop table public.loader_little_files_mnn1;

delete from public.loader_little_files_mnn where rn > 1;

alter table public.loader_little_files_mnn drop column rn;