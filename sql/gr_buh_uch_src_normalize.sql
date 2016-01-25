delete from public.loader_little_files_gr_buh_uch where name is null or name = '';

--deleting doubles
drop table if exists public.loader_little_files_gr_buh_uch1;
create table public.loader_little_files_gr_buh_uch1 as
select *, row_number() over(partition by name) rn from public.loader_little_files_gr_buh_uch;
drop table if exists public.loader_little_files_gr_buh_uch;

delete from public.loader_little_files_gr_buh_uch1 where rn > 1;

create table public.loader_little_files_gr_buh_uch as
select * from public.loader_little_files_gr_buh_uch1;

drop table public.loader_little_files_gr_buh_uch1;