package cn.explink.b2c.yangguang;

/**
 * 央广的仓库编号
 * 
 * @author Administrator
 *
 */
public enum YgWareHoueEnum {

	BeiJingCangKu("北京仓库", 105), NanJingCangKu("南京仓库", 106), ChangChunCangKu("长春仓库", 103), ChangShaCangKu("长沙仓库", 104), ChangShaCangKu1("长沙仓库", 110),

	;

	private String warehouseName; // 列 名
	private int no; // 开始字符串位置

	private YgWareHoueEnum(String warehouseName, int no) {
		this.warehouseName = warehouseName;
		this.no = no;
	}

	public static YgWareHoueEnum YgWarehouse(int no) {
		for (YgWareHoueEnum ygenum : YgWareHoueEnum.values()) {
			if (ygenum.getNo() == no) {
				return ygenum;
			}
		}
		return null;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

}
