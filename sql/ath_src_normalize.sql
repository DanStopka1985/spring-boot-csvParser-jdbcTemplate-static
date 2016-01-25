--delete own_code doubles
drop table if exists public.loader_little_files_ath1;
create table public.loader_little_files_ath1 as
select *, row_number()over(partition by own_code order by code) rn from public.loader_little_files_ath;

delete from public.loader_little_files_ath1 where rn > 1;

drop table if exists public.loader_little_files_ath;

create table public.loader_little_files_ath as
select * from public.loader_little_files_ath1;

drop table if exists public.loader_little_files_ath1;