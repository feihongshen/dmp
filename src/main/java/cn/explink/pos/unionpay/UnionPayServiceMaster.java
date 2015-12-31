package cn.explink.pos.unionpay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnionPayServiceMaster {
	@Autowired
	private UnionPayService_Login unionPayService_Login;
	@Autowired
	private UnionPayService_LoginOut unionPayService_LoginOut;
	@Autowired
	private UnionPayService_Search unionPayService_Search;
	@Autowired
	private UnionPayService_Delivery unionPayService_Delivery;

	public UnionPayService_Login getUnionPayService_Login() {
		return unionPayService_Login;
	}

	public void setUnionPayService_Login(UnionPayService_Login unionPayService_Login) {
		this.unionPayService_Login = unionPayService_Login;
	}

	public UnionPayService_LoginOut getUnionPayService_LoginOut() {
		return unionPayService_LoginOut;
	}

	public void setUnionPayService_LoginOut(UnionPayService_LoginOut unionPayService_LoginOut) {
		this.unionPayService_LoginOut = unionPayService_LoginOut;
	}

	public UnionPayService_Search getUnionPayService_Search() {
		return unionPayService_Search;
	}

	public void setUnionPayService_Search(UnionPayService_Search unionPayService_Search) {
		this.unionPayService_Search = unionPayService_Search;
	}

	public UnionPayService_Delivery getUnionPayService_Delivery() {
		return unionPayService_Delivery;
	}

	public void setUnionPayService_Delivery(UnionPayService_Delivery unionPayService_Delivery) {
		this.unionPayService_Delivery = unionPayService_Delivery;
	}

}
