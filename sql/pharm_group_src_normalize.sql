delete from public.loader_little_files_pharm_group where name is null;
delete from public.loader_little_files_pharm_group where uid is null;
drop table if exists public.loader_little_files_pharm_group1;

create table public.loader_little_files_pharm_group1 as
select *, row_number() over(partition by name order by uid) rn, row_number() over(partition by uid order by name) rn1 from public.loader_little_files_pharm_group;

delete from public.loader_little_files_pharm_group1 where rn > 1 or rn1 > 1;

drop table if exists public.loader_little_files_pharm_group;

create table public.loader_little_files_pharm_group as
select name, uid from public.loader_little_files_pharm_group1;

drop table if exists public.loader_little_files_pharm_group1;

create table public.loader_little_files_pharm_group1 as
select
 *,
 (select b.uid from public.loader_little_files_pharm_group b where a.uid != b.uid and position(b.uid in a.uid) = 1 order by length(b.uid) desc limit 1) parent_uid
from public.loader_little_files_pharm_group a
;

update public.loader_little_files_pharm_group1 set parent_uid = null where parent_uid = '';
--todo next normalize