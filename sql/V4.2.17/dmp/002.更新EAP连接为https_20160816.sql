-- 更改EAP的地址为https
update express_set_system_install set value = replace(value,'http://','https://') where name = 'eapUrl';