package cn.explink.b2c.jumei;

public class JuMeiException extends RuntimeException {

	private JuMeiExptMessageEnum error;

	public JuMeiException(JuMeiExptMessageEnum error) {
		super(error.getResp_code());
		this.error = error;
	}

	public JuMeiExptMessageEnum getError() {
		return error;
	}
}
