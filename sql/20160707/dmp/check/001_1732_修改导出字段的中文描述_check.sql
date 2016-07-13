
SELECT
	'修改导出字段的中文描述' AS '检查项',
IF (
	(
		SELECT
			count(*)
		FROM
			express_ops_setexportfield
		WHERE
			fieldname = '最新流程操作时间'
		AND fieldenglishname = 'Newchangetime')=1,
		'success',
		'failed'
	) AS '执行结果';