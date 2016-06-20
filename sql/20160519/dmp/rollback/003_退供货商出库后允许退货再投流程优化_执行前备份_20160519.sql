/*保存执行结果，用于回滚*/
select fromstate, tostate FROM express_set_cwb_state_control where fromstate = 27 and tostate = 15;
select cwbstate, toflowtype from express_set_cwb_allstate_control where cwbstate = 5 and toflowtype = 15;
select tanscwbstate, toflowtype from express_set_transcwb_allstate_control where tanscwbstate = 5 and toflowtype = 15;