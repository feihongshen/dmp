package cn.explink.b2c.zhongliang.xml;

import org.junit.Test;

import cn.explink.b2c.tools.ObjectUnMarchal;

public class TestXml {
	String str = "<Response service=\"WaitOrder\"  lang=\"zh-CN\">" + "<Head>server</Head>" + "<Body>" + "<Order>" + "<PurchaseNO>网络单号</PurchaseNO>" + "<SendOrderID>发货单号</SendOrderID>"
			+ "<PackageDetail>" + "<PackageID>包裹号</PackageID>" + "</PackageDetail>" + "<GoodDetail>" + "<ID>001</ID>" + "<GoodsID>goods001</GoodsID>" + "<GoodsName>羽毛球</GoodsName>" + "<QTY>QTY</QTY>"
			+ "<UnitName>SSSS</UnitName>" + "<Price>19.0</Price>" + "<Remark>测试</Remark>" + "</GoodDetail>" + "<GoodDetail>" + "<ID>001</ID>" + "<GoodsID>goods001</GoodsID>"
			+ "<GoodsName>羽毛球111111111</GoodsName>" + "<QTY>QTY</QTY>" + "<UnitName>SSSS</UnitName>" + "<Price>19.0</Price>" + "<Remark>测试</Remark>" + "</GoodDetail>" + "</Order>" + "<Order>"
			+ "<PurchaseNO>网络单号1</PurchaseNO>" + "<SendOrderID>发货单号1</SendOrderID>" + "<PackageDetail>" + "<PackageID>包裹号1</PackageID>" + "</PackageDetail>" + "<GoodDetail>" + "<ID>001</ID>"
			+ "<GoodsID>goods001</GoodsID>" + "<GoodsName>羽毛球</GoodsName>" + "<QTY>QTY</QTY>" + "<UnitName>SSSS</UnitName>" + "<Price>19.0</Price>" + "<Remark>测试</Remark>" + "</GoodDetail>"
			+ "</Order>" + "</Body>	" + "</Response>";
	Response_WaitOrder response = null;

	String str1 = "<Response service=\"CancleOrder\" lang=\"zh-CN\">" + "<Head>server</Head>" + "<Body>" + "<Order>" + "<SendOrderID>发货单号</SendOrderID>" + "<Remark>备注说明</Remark>" + "</Order></Body>"
			+ "</Response>";
	Response_CancleOrder cancleOrder = null;

	@Test
	public void test1() {
		try {

			response = (Response_WaitOrder) ObjectUnMarchal
					.XmltoPOJO(
							"<Response service=\"WaitOrder\"  lang=\"zh-CN\"><Head>server</Head><Body><Order><PurchaseNO>412538321746690</PurchaseNO><SendOrderID>BJZC13090105080</SendOrderID><OrderID>A00A201309015065</OrderID><OrderType>70</OrderType><linkMan>韩玉娥</linkMan><TelNO></TelNO><HandsetNO>15843419558</HandsetNO><Address>西农园海银帝景F3</Address><PostalCode>136400</PostalCode><ExpCompany >北京宅急送快运股份有限公司</ExpCompany><Freight>0</Freight><TaxFlag>不要发票</TaxFlag><TaxType>不要发票</TaxType><ValuableFlag>普通商品</ValuableFlag><OrderTemark>--食品专家--</OrderTemark><Boxes>1</Boxes><OrderValue>92.40</OrderValue><GetValue>0</GetValue><PayType>现金</PayType><FreshFlag>普通</FreshFlag><SendTime></SendTime><ProvinceName>吉林省</ProvinceName><CityName>四平市</CityName><AreaName>铁西区</AreaName><OrderStatus>15</OrderStatus><ReturnTaxValue></ReturnTaxValue><PurLevel>0</PurLevel><ReturnTaxFlag></ReturnTaxFlag><PackageDetail><PackageID>BJZC13090105080-01</PackageID></PackageDetail></Order><Order><PurchaseNO>411925618381585</PurchaseNO><SendOrderID>BJZC13090103406</SendOrderID><OrderID>A00A201309013017</OrderID><OrderType>70</OrderType><linkMan>王南益</linkMan><TelNO>0574-65601031</TelNO><HandsetNO>15867414976</HandsetNO><Address>爵溪街道镇前街18号</Address><PostalCode>315708</PostalCode><ExpCompany >北京宅急送快运股份有限公司</ExpCompany><Freight>0</Freight><TaxFlag>不要发票</TaxFlag><TaxType>不要发票</TaxType><ValuableFlag>普通商品</ValuableFlag><OrderTemark>--食品专家--</OrderTemark><Boxes>1</Boxes><OrderValue>124</OrderValue><GetValue>0</GetValue><PayType>现金</PayType><FreshFlag>普通</FreshFlag><SendTime></SendTime><ProvinceName>浙江省</ProvinceName><CityName>宁波</CityName><AreaName>象山县</AreaName><OrderStatus>15</OrderStatus><ReturnTaxValue></ReturnTaxValue><PurLevel>0</PurLevel><ReturnTaxFlag></ReturnTaxFlag><PackageDetail><PackageID>BJZC13090103406-01</PackageID></PackageDetail></Order><Order><PurchaseNO>412286165927494</PurchaseNO><SendOrderID>BJZC13090103986</SendOrderID><OrderID>A00A201309013729</OrderID><OrderType>70</OrderType><linkMan>李怡</linkMan><TelNO></TelNO><HandsetNO>13778099828</HandsetNO><Address>临园路西段30号新世界宾馆七楼</Address><PostalCode>621000</PostalCode><ExpCompany >北京宅急送快运股份有限公司</ExpCompany><Freight>0</Freight><TaxFlag>不要发票</TaxFlag><TaxType>不要发票</TaxType><ValuableFlag>普通商品</ValuableFlag><OrderTemark>--食品专家--</OrderTemark><Boxes>1</Boxes><OrderValue>124</OrderValue><GetValue>0</GetValue><PayType>现金</PayType><FreshFlag>普通</FreshFlag><SendTime></SendTime><ProvinceName>四川省</ProvinceName><CityName>绵阳市</CityName><AreaName>涪城区</AreaName><OrderStatus>15</OrderStatus><ReturnTaxValue></ReturnTaxValue><PurLevel>0</PurLevel><ReturnTaxFlag></ReturnTaxFlag><PackageDetail><PackageID>BJZC13090103986-01</PackageID></PackageDetail></Order><Order><PurchaseNO>722277089</PurchaseNO><SendOrderID>BJZC13090103979</SendOrderID><OrderID>A00A201309013716</OrderID><OrderType>70</OrderType><linkMan>王玮</linkMan><TelNO>65286096</TelNO><HandsetNO>18980099336</HandsetNO><Address>四川成都市高新区绕城环线以外高新区吉泰路688号石化办公基地</Address><PostalCode></PostalCode><ExpCompany >北京宅急送快运股份有限公司</ExpCompany><Freight>0</Freight><TaxFlag>不要发票</TaxFlag><TaxType>不要发票</TaxType><ValuableFlag>普通商品</ValuableFlag><OrderTemark>异常：淘宝区域编码未匹配到我买区域编码！--食品专家--</OrderTemark><Boxes>1</Boxes><OrderValue>65</OrderValue><GetValue>0</GetValue><PayType>现金</PayType><FreshFlag>普通</FreshFlag><SendTime></SendTime><ProvinceName>四川省</ProvinceName><CityName>成都市</CityName><AreaName>其它区</AreaName><OrderStatus>15</OrderStatus><ReturnTaxValue></ReturnTaxValue><PurLevel>0</PurLevel><ReturnTaxFlag></ReturnTaxFlag><PackageDetail><PackageID>BJZC13090103979-01</PackageID></PackageDetail></Order><Order><PurchaseNO>412380834878195</PurchaseNO><SendOrderID>BJZC13090105081</SendOrderID><OrderID>A00A201309015066</OrderID><OrderType>70</OrderType><linkMan>韩开洋</linkMan><TelNO></TelNO><HandsetNO>18745962851</HandsetNO><Address>大庆师范学院</Address><PostalCode>163311</PostalCode><ExpCompany >北京宅急送快运股份有限公司</ExpCompany><Freight>0</Freight><TaxFlag>不要发票</TaxFlag><TaxType>不要发票</TaxType><ValuableFlag>普通商品</ValuableFlag><OrderTemark>--食品专家--</OrderTemark><Boxes>1</Boxes><OrderValue>61.60</OrderValue><GetValue>0</GetValue><PayType>现金</PayType><FreshFlag>普通</FreshFlag><SendTime></SendTime><ProvinceName>黑龙江省</ProvinceName><CityName>大庆市</CityName><AreaName>让胡路区</AreaName><OrderStatus>15</OrderStatus><ReturnTaxValue></ReturnTaxValue><PurLevel>0</PurLevel><ReturnTaxFlag></ReturnTaxFlag><PackageDetail><PackageID>BJZC13090105081-01</PackageID></PackageDetail></Order><Order><PurchaseNO>411837698588705</PurchaseNO><SendOrderID>BJZC13090102896</SendOrderID><OrderID>A00A201309012429</OrderID><OrderType>70</OrderType><linkMan>罗卫</linkMan><TelNO>029-84833139</TelNO><HandsetNO>13991153739</HandsetNO><Address>户县工商小区</Address><PostalCode>710300</PostalCode><ExpCompany >北京宅急送快运股份有限公司</ExpCompany><Freight>0</Freight><TaxFlag>不要发票</TaxFlag><TaxType>不要发票</TaxType><ValuableFlag>普通商品</ValuableFlag><OrderTemark>--食品专家--</OrderTemark><Boxes>2</Boxes><OrderValue>198</OrderValue><GetValue>0</GetValue><PayType>现金</PayType><FreshFlag>普通</FreshFlag><SendTime></SendTime><ProvinceName>陕西省</ProvinceName><CityName>西安市</CityName><AreaName>户县</AreaName><OrderStatus>15</OrderStatus><ReturnTaxValue></ReturnTaxValue><PurLevel>0</PurLevel><ReturnTaxFlag></ReturnTaxFlag><PackageDetail><PackageID>BJZC13090102896-02</PackageID><PackageID>BJZC13090102896-01</PackageID></PackageDetail></Order><Order><PurchaseNO>412246179299192-1</PurchaseNO><SendOrderID>BJZC13090105036</SendOrderID><OrderID>A00A201309015007</OrderID><OrderType>70</OrderType><linkMan>赵小芳</linkMan><TelNO>0571-82566338</TelNO><HandsetNO>13588775988</HandsetNO><Address>金惠路银河小区73栋1单元302</Address><PostalCode>311241</PostalCode><ExpCompany >北京宅急送快运股份有限公司</ExpCompany><Freight>0</Freight><TaxFlag>不要发票</TaxFlag><TaxType>不要发票</TaxType><ValuableFlag>普通商品</ValuableFlag><OrderTemark>买家留言不为空--食品专家--</OrderTemark><Boxes>1</Boxes><OrderValue>26.56</OrderValue><GetValue>0</GetValue><PayType>现金</PayType><FreshFlag>普通</FreshFlag><SendTime></SendTime><ProvinceName>浙江省</ProvinceName><CityName>杭州</CityName><AreaName>萧山区</AreaName><OrderStatus>15</OrderStatus><ReturnTaxValue></ReturnTaxValue><PurLevel>0</PurLevel><ReturnTaxFlag></ReturnTaxFlag><PackageDetail><PackageID>BJZC13090105036-01</PackageID></PackageDetail></Order><Order><PurchaseNO>412304516939569</PurchaseNO><SendOrderID>BJZC13090105052</SendOrderID><OrderID>A00A201309015025</OrderID><OrderType>70</OrderType><linkMan>韩博</linkMan><TelNO></TelNO><HandsetNO>18697950891</HandsetNO><Address>北京市昌平区沙河机场</Address><PostalCode>102200</PostalCode><ExpCompany >北京宅急送快运股份有限公司</ExpCompany><Freight>0</Freight><TaxFlag>不要发票</TaxFlag><TaxType>不要发票</TaxType><ValuableFlag>普通商品</ValuableFlag><OrderTemark>--食品专家--</OrderTemark><Boxes>1</Boxes><OrderValue>26</OrderValue><GetValue>0</GetValue><PayType>现金</PayType><FreshFlag>普通</FreshFlag><SendTime></SendTime><ProvinceName>北京</ProvinceName><CityName>昌平区</CityName><AreaName>其他区域</AreaName><OrderStatus>15</OrderStatus><ReturnTaxValue></ReturnTaxValue><PurLevel>0</PurLevel><ReturnTaxFlag></ReturnTaxFlag><PackageDetail><PackageID>BJZC13090105052-01</PackageID></PackageDetail></Order><Order><PurchaseNO>411683794205064</PurchaseNO><SendOrderID>BJZC13090102442</SendOrderID><OrderID>A00A201309011786</OrderID><OrderType>70</OrderType><linkMan>牛曼利</linkMan><TelNO>0913-7663081</TelNO><HandsetNO>13892370900</HandsetNO><Address>陕西华电蒲城发电有限责任公司汽机专业</Address><PostalCode>715501</PostalCode><ExpCompany >北京宅急送快运股份有限公司</ExpCompany><Freight>0</Freight><TaxFlag>不要发票</TaxFlag><TaxType>不要发票</TaxType><ValuableFlag>普通商品</ValuableFlag><OrderTemark>--食品专家--</OrderTemark><Boxes>1</Boxes><OrderValue>119</OrderValue><GetValue>0</GetValue><PayType>现金</PayType><FreshFlag>普通</FreshFlag><SendTime></SendTime><ProvinceName>陕西省</ProvinceName><CityName>渭南市</CityName><AreaName>蒲城县</AreaName><OrderStatus>15</OrderStatus><ReturnTaxValue></ReturnTaxValue><PurLevel>0</PurLevel><ReturnTaxFlag></ReturnTaxFlag><PackageDetail><PackageID>BJZC13090102442-01</PackageID></PackageDetail></Order><Order><PurchaseNO>720681959</PurchaseNO><SendOrderID>BJZC13083108326</SendOrderID><OrderID>A00A201308319481</OrderID><OrderType>70</OrderType><linkMan>李先生</linkMan><TelNO>18982032746</TelNO><HandsetNO>18982032746</HandsetNO><Address>四川成都市高新区三环以内兴蓉北街1号君和源2栋2单元</Address><PostalCode></PostalCode><ExpCompany >北京宅急送快运股份有限公司</ExpCompany><Freight>0</Freight><TaxFlag>不要发票</TaxFlag><TaxType>不要发票</TaxType><ValuableFlag>普通商品</ValuableFlag><OrderTemark>异常：淘宝区域编码未匹配到我买区域编码！--食品专家--</OrderTemark><Boxes>1</Boxes><OrderValue>94</OrderValue><GetValue>0</GetValue><PayType>现金</PayType><FreshFlag>普通</FreshFlag><SendTime></SendTime><ProvinceName>四川省</ProvinceName><CityName>成都市</CityName><AreaName>其它区</AreaName><OrderStatus>15</OrderStatus><ReturnTaxValue></ReturnTaxValue><PurLevel>0</PurLevel><ReturnTaxFlag></ReturnTaxFlag><PackageDetail><PackageID>BJZC13083108326-01</PackageID></PackageDetail></Order><Order><PurchaseNO>721486457</PurchaseNO><SendOrderID>BJZC13090100060</SendOrderID><OrderID>A00A201309010332</OrderID><OrderType>70</OrderType><linkMan>杨建龙</linkMan><TelNO>13621768706</TelNO><HandsetNO>13621768706</HandsetNO><Address>上海青浦区外环以外赵巷镇业煌路168弄192号</Address><PostalCode></PostalCode><ExpCompany >北京宅急送快运股份有限公司</ExpCompany><Freight>0</Freight><TaxFlag>不要发票</TaxFlag><TaxType>不要发票</TaxType><ValuableFlag>普通商品</ValuableFlag><OrderTemark>--食品专家--</OrderTemark><Boxes>2</Boxes><OrderValue>320</OrderValue><GetValue>0</GetValue><PayType>现金</PayType><FreshFlag>普通</FreshFlag><SendTime></SendTime><ProvinceName>上海市</ProvinceName><CityName>青浦区</CityName><AreaName>青浦区</AreaName><OrderStatus>15</OrderStatus><ReturnTaxValue></ReturnTaxValue><PurLevel>0</PurLevel><ReturnTaxFlag></ReturnTaxFlag><PackageDetail><PackageID>BJZC13090100060-02</PackageID><PackageID>BJZC13090100060-01</PackageID></PackageDetail></Order><Order><PurchaseNO>721435897</PurchaseNO><SendOrderID>BJZC13083108385</SendOrderID><OrderID>A00A201308319554</OrderID><OrderType>70</OrderType><linkMan>牟博</linkMan><TelNO>18601101450</TelNO><HandsetNO>18601101450</HandsetNO><Address>内蒙古锡林郭勒盟锡林浩特市希日塔拉街道希日塔拉街道天津路骏景花园大门北品色</Address><PostalCode></PostalCode><ExpCompany >北京宅急送快运股份有限公司</ExpCompany><Freight>0</Freight><TaxFlag>需要发票</TaxFlag><TaxType>随货</TaxType><ValuableFlag>普通商品</ValuableFlag><OrderTemark>异常：省未匹配到仓库信息！--食品专家--</OrderTemark><Boxes>1</Boxes><OrderValue>63</OrderValue><GetValue>0</GetValue><PayType>现金</PayType><FreshFlag>普通</FreshFlag><SendTime></SendTime><ProvinceName>内蒙古自治区</ProvinceName><CityName>锡林郭勒盟</CityName><AreaName>锡林浩特市</AreaName><OrderStatus>15</OrderStatus><ReturnTaxValue></ReturnTaxValue><PurLevel>0</PurLevel><ReturnTaxFlag></ReturnTaxFlag><PackageDetail><PackageID>BJZC13083108385-01</PackageID></PackageDetail></Order><Order><PurchaseNO>721544583-1</PurchaseNO><SendOrderID>BJZC13090100050</SendOrderID><OrderID>A00A201309010312</OrderID><OrderType>70</OrderType><linkMan>赖冰松</linkMan><TelNO>13804567452</TelNO><HandsetNO>13804567452</HandsetNO><Address>黑龙江哈尔滨市道里区经纬街道端街24-5号5楼哈尔滨市公安局技侦支队</Address><PostalCode></PostalCode><ExpCompany >北京宅急送快运股份有限公司</ExpCompany><Freight>0</Freight><TaxFlag>需要发票</TaxFlag><TaxType>随货</TaxType><ValuableFlag>普通商品</ValuableFlag><OrderTemark>该订单为京东拆分订单--食品专家--</OrderTemark><Boxes>1</Boxes><OrderValue>46.20</OrderValue><GetValue>0</GetValue><PayType>现金</PayType><FreshFlag>普通</FreshFlag><SendTime></SendTime><ProvinceName>黑龙江省</ProvinceName><CityName>哈尔滨市</CityName><AreaName>道里区</AreaName><OrderStatus>15</OrderStatus><ReturnTaxValue></ReturnTaxValue><PurLevel>0</PurLevel><ReturnTaxFlag></ReturnTaxFlag><PackageDetail><PackageID>BJZC13090100050-01</PackageID></PackageDetail></Order><Order><PurchaseNO>1683517220407</PurchaseNO><SendOrderID>BJZC13090104440</SendOrderID><OrderID>A00A201309014347</OrderID><OrderType>70</OrderType><linkMan>宋建陶</linkMan><TelNO>0832-8228962</TelNO><HandsetNO>13990542480</HandsetNO><Address>东街川剧团楼下恒威网吧</Address><PostalCode>642450</PostalCode><ExpCompany >北京宅急送快运股份有限公司</ExpCompany><Freight>0</Freight><TaxFlag>不要发票</TaxFlag><TaxType>不要发票</TaxType><ValuableFlag>普通商品</ValuableFlag><OrderTemark>分销方式：AGENT(代销)买家留言不为空--食品专家--</OrderTemark><Boxes>4</Boxes><OrderValue>230.40</OrderValue><GetValue>0</GetValue><PayType>现金</PayType><FreshFlag>普通</FreshFlag><SendTime></SendTime><ProvinceName>四川省</ProvinceName><CityName>内江市</CityName><AreaName>威远县</AreaName><OrderStatus>15</OrderStatus><ReturnTaxValue></ReturnTaxValue><PurLevel>0</PurLevel><ReturnTaxFlag></ReturnTaxFlag><PackageDetail><PackageID>BJZC13090104440-03</PackageID><PackageID>BJZC13090104440-02</PackageID><PackageID>BJZC13090104440-01</PackageID><PackageID>BJZC13090104440-04</PackageID></PackageDetail></Order><Order><PurchaseNO>412291475567397</PurchaseNO><SendOrderID>BJZC13090105049</SendOrderID><OrderID>A00A201309015018</OrderID><OrderType>70</OrderType><linkMan>侯春萌</linkMan><TelNO></TelNO><HandsetNO>13796701861</HandsetNO><Address>牌路大街289号恒达民爆公司 侯春萌</Address><PostalCode>150300</PostalCode><ExpCompany >北京宅急送快运股份有限公司</ExpCompany><Freight>0</Freight><TaxFlag>不要发票</TaxFlag><TaxType>不要发票</TaxType><ValuableFlag>普通商品</ValuableFlag><OrderTemark>--食品专家--</OrderTemark><Boxes>1</Boxes><OrderValue>298</OrderValue><GetValue>0</GetValue><PayType>现金</PayType><FreshFlag>普通</FreshFlag><SendTime></SendTime><ProvinceName>黑龙江省</ProvinceName><CityName>哈尔滨市</CityName><AreaName>阿城市</AreaName><OrderStatus>15</OrderStatus><ReturnTaxValue></ReturnTaxValue><PurLevel>0</PurLevel><ReturnTaxFlag></ReturnTaxFlag><PackageDetail><PackageID>BJZC13090105049-01</PackageID></PackageDetail></Order><Order><PurchaseNO>721017671-1</PurchaseNO><SendOrderID>BJZC13083108297</SendOrderID><OrderID>A00A201308319450</OrderID><OrderType>70</OrderType><linkMan>巫俊琦</linkMan><TelNO>13763389857</TelNO><HandsetNO>13763389857</HandsetNO><Address>广东佛山市南海区大沥镇盐步镇盐秀路1号海纳天河12栋904</Address><PostalCode></PostalCode><ExpCompany >北京宅急送快运股份有限公司</ExpCompany><Freight>0</Freight><TaxFlag>不要发票</TaxFlag><TaxType>不要发票</TaxType><ValuableFlag>普通商品</ValuableFlag><OrderTemark>该订单为京东拆分订单--食品专家--</OrderTemark><Boxes>1</Boxes><OrderValue>28.19</OrderValue><GetValue>0</GetValue><PayType>现金</PayType><FreshFlag>普通</FreshFlag><SendTime></SendTime><ProvinceName>广东省</ProvinceName><CityName>佛山市</CityName><AreaName>南海区</AreaName><OrderStatus>15</OrderStatus><ReturnTaxValue></ReturnTaxValue><PurLevel>0</PurLevel><ReturnTaxFlag></ReturnTaxFlag><PackageDetail><PackageID>BJZC13083108297-01</PackageID></PackageDetail></Order><Order><PurchaseNO>722056838</PurchaseNO><SendOrderID>BJZC13090102915</SendOrderID><OrderID>A00A201309012458</OrderID><OrderType>70</OrderType><linkMan>梁峰源</linkMan><TelNO>13929997206</TelNO><HandsetNO>13929997206</HandsetNO><Address>广东佛山市顺德区容桂街道容桂镇桂新西路10号欧美陶瓷</Address><PostalCode></PostalCode><ExpCompany >北京宅急送快运股份有限公司</ExpCompany><Freight>0</Freight><TaxFlag>需要发票</TaxFlag><TaxType>随货</TaxType><ValuableFlag>普通商品</ValuableFlag><OrderTemark>--食品专家--</OrderTemark><Boxes>1</Boxes><OrderValue>196</OrderValue><GetValue>0</GetValue><PayType>现金</PayType><FreshFlag>普通</FreshFlag><SendTime></SendTime><ProvinceName>广东省</ProvinceName><CityName>佛山市</CityName><AreaName>顺德区</AreaName><OrderStatus>15</OrderStatus><ReturnTaxValue></ReturnTaxValue><PurLevel>0</PurLevel><ReturnTaxFlag></ReturnTaxFlag><PackageDetail><PackageID>BJZC13090102915-01</PackageID></PackageDetail></Order></Body></Response>",
							new Response_WaitOrder());
			System.out.println(response);
			System.out.println(ObjectUnMarchal.POJOtoXml(response));
			System.out.println(str1);
			cancleOrder = (Response_CancleOrder) ObjectUnMarchal.XmltoPOJO(str1, new Response_CancleOrder());
			System.out.println(cancleOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
