package cn.explink.b2c.jumei;

import org.springframework.stereotype.Service;

import cn.explink.pos.alipay.AlipayException;

@Service
public class JointException {

	private JuMeiException jumeiExpt;
	private AlipayException alipayExpt;

	public AlipayException getAlipayExpt() {
		return alipayExpt;
	}

	public void setAlipayExpt(AlipayException alipayExpt) {
		this.alipayExpt = alipayExpt;
	}

	public JuMeiException getJumeiExpt() {
		return jumeiExpt;
	}

	public void setJumeiExpt(JuMeiException jumeiExpt) {
		this.jumeiExpt = jumeiExpt;
	}

}
