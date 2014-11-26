package cn.explink.aspect;


public aspect OperationRecordMore {

	declare precedence : OperationRecord,org.springframework.transaction.aspectj.AnnotationTransactionAspect,CwbTranslateAspect ;

	
}
