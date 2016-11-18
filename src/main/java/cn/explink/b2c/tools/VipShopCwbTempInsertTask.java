package cn.explink.b2c.tools;

/**
 * 订单临时表转主表业务
 * 使用 Fork join 框架 
 * @date 2016-11-15
 * @author jian.xie
 *
 */
public class VipShopCwbTempInsertTask {
	
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//	
//	private VipshopInsertCwbDetailTimmer 			vipshopInsertCwbDetailTimmer;
//	
//	private List<B2cEnum> 							b2cEnumList;
//	
//	public VipShopCwbTempInsertTask(VipshopInsertCwbDetailTimmer vipshopInsertCwbDetailTimmer, List<B2cEnum> b2cEnumList){
//		this.vipshopInsertCwbDetailTimmer = vipshopInsertCwbDetailTimmer;
//		this.b2cEnumList = b2cEnumList;
//	}
//
//	@Override
//	protected void compute() {
//		if (!CollectionUtils.isEmpty(b2cEnumList)) {
//			if (b2cEnumList.size() == 1) {
//				vipshopInsertCwbDetailTimmer.selectTempAndInsertToCwbDetail(b2cEnumList.get(0).getKey());
//			} else {
//				int middle = b2cEnumList.size() / 2;
//				VipShopCwbTempInsertTask left = new VipShopCwbTempInsertTask(vipshopInsertCwbDetailTimmer, b2cEnumList.subList(0, middle));
//				VipShopCwbTempInsertTask right = new VipShopCwbTempInsertTask(vipshopInsertCwbDetailTimmer,b2cEnumList.subList(middle, b2cEnumList.size()));
//				left.fork();
//				right.fork();
//			}
//		}
//	}

}
