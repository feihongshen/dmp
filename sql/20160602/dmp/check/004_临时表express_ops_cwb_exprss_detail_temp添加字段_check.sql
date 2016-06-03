select '004express_ops_cwb_exprss_detail_temp添加字段' as '脚本注释', if(
(SELECT count(1) from information_schema.`COLUMNS`
where TABLE_SCHEMA = sCHEMA() and TABLE_NAME='express_ops_cwb_exprss_detail_temp' and COLUMN_NAME in ('cnor_corp_no','cnor_corp_name','freight','account_id','packing_fee','express_image','cnee_corp_name','is_accept_prov','express_product_type')
)=9, 'success', 'failed') as '执行结果' ;