package cn.explink.b2c.huanqiugou.respDto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="Response")
public class ResponseDto {
	private ReturnDto returnDto;
	@XmlElement(name="return")
	public ReturnDto getReturnDto() {
		return returnDto;
	}

	public void setReturnDto(ReturnDto returnDto) {
		this.returnDto = returnDto;
	}
	
}
