*selectUser*
===
> wfewafewafewafewaf

    select * from import_project
    
selectUser22
---
> wfewafewafewafewaf
    
    select distinct url from sys_resources where id in (
        select "resourcesId" from sys_account_resources
            where mark = 'add'
            and "accountId" = (select id from sys_account where name = ?) UNION
        select srr."resourcesId" from sys_role_resources srr left join sys_account_resources sar
                on srr."resourcesId" = sar."resourcesId" and sar.mark = 'del'
            where "roleId" in (select "roleId" from sys_account_role
                                where "accountId" = (select id from sys_account where name = ?))
            and sar."resourcesId" is null)
            
            #if(id == null)
            id = 
            #end
        
selectUser22
----------------
> wfewafewafewafewaf3333333333   
    
    select {selectUser} from user = fewafewa323232 ${fewa} @{} #{}
        select
        
selectUser22
----------------
> wfewafewafewafewaf
    
    select {selectUser} from user = fewafewa323232
        select 2
        
        
selectDate
-------------
> 测试
> owkfewoao

    select end_time from ad where id = #{id}
    
*selectDate2*
-------------
> 测试2

    select * from {{user_fee323232f}}
    
fee323232f
---------
    ad
        


	


