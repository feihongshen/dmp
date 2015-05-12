package cn.explink.domain;

import java.math.BigDecimal;

/**
 * 
 * @author wzy
 * 
 *
 */
public class DeliveryStations {
		private int d_id; //站点id
		private String name; //站点名称
		private String coordinate; //站点范围经纬度
		private BigDecimal mapcenter_lng; //站点经纬度
		private BigDecimal mapcenter_lat; //站点经纬度
		private String address; //站点地址
		private int typeid;
		private int brach_id;
		
		
		public int getBrach_id() {
			return brach_id;
		}
		public void setBrach_id(int brach_id) {
			this.brach_id = brach_id;
		}
		public int getTypeid() {
			return typeid;
		}
		public void setTypeid(int typeid) {
			this.typeid = typeid;
		}
		public int getD_id() {
			return d_id;
		}
		public void setD_id(int d_id) {
			this.d_id = d_id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getCoordinate() {
			return coordinate;
		}
		public void setCoordinate(String coordinate) {
			this.coordinate = coordinate;
		}
		public BigDecimal getMapcenter_lng() {
			return mapcenter_lng;
		}
		public void setMapcenter_lng(BigDecimal mapcenter_lng) {
			this.mapcenter_lng = mapcenter_lng;
		}
		public BigDecimal getMapcenter_lat() {
			return mapcenter_lat;
		}
		public void setMapcenter_lat(BigDecimal mapcenter_lat) {
			this.mapcenter_lat = mapcenter_lat;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		
		

		
}
