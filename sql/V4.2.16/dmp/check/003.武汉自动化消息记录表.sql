select '003.����Զ�����Ϣ��¼��' as '�ű�ע��', if(
(SELECT count(1) from information_schema.`TABLES`
where TABLE_SCHEMA = SCHEMA() and TABLE_NAME='express_ops_auto_intowarehouse_message'
)=1, 'success', 'failed') as 'ִ�н��' ;