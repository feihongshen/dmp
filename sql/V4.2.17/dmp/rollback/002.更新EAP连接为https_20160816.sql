-- 把EAP的地址由https回滚为http
update express_set_system_install set value = replace(value,'https://','http://') where name = 'eapUrl';