package cn.explink.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import junit.framework.TestCase;

public class CODReader extends TestCase {
	CODPayment codpayment;
	InputStreamReader isr;
	BufferedReader reader;
	FileInputStream fis;

	public void setUp() throws Exception {
		String fileName = "dss_payment_scac_20130130_120158.txt";

		codpayment = new CODPayment();
		fis = new FileInputStream("D:\\amazon\\upload\\" + fileName);
		isr = new java.io.InputStreamReader(fis, "UTF-8");
		reader = new BufferedReader(isr);
		String codString = reader.readLine();

		codpayment.setHeader(codString.substring(0, 192));

		String codRemainingString = codString.substring(192);
		int recordcount = codRemainingString.length() / 214;
		String[] detailList = new String[recordcount];

		for (int i = 0; i < recordcount; i++) {
			detailList[i] = codRemainingString.substring(0, 214);
			codRemainingString = codRemainingString.substring(214);
		}

		codpayment.setDetailList(detailList);
		codpayment.setTrailer(codRemainingString);

		reader.close();
		isr.close();
		fis.close();

	}

	public void testCheckingDetailCount()
			throws StringIndexOutOfBoundsException {
		assertTrue(Integer.parseInt(codpayment.getTrailer().substring(1, 10)
				.trim()) == codpayment.getDetailList().length);

		System.out.println("Record count passed. "
				+ codpayment.getDetailList().length + " records");

	}

	public void testCODPaymentBasic() {
		try {
			// testCheckingDetailCount();
			testCheckingHeaderIdentifier();
			testCheckingTrailerIdentifer();
			testCheckingDetailIdentifier();
			System.out.println("Basic COD checking passed");

		} catch (StringIndexOutOfBoundsException e) {
			System.out.println("The input file may not in correct format!");
		}
	}

	public void tearDown() throws Exception {
		// System.out.println("Junit��Ԫ���Խ���");

	}

	public void testCheckingHeaderIdentifier() {
		assertEquals('H', codpayment.getHeader().charAt(0));
	}

	public void testCheckingTrailerIdentifer() {
		assertEquals('T', codpayment.getTrailer().charAt(0));
	}

	public void testCheckingTrailerLength() {
		assertEquals(192, codpayment.getTrailer().length());
	}

	public void testCheckingDetailIdentifier() {
		String[] detailList = codpayment.getDetailList();
		for (int i = 0; i < detailList.length; i++) {
			assertEquals('D', detailList[i].charAt(0));
		}
	}

	public void testCheckingDetailTransactionType() {
		String[] detailList = codpayment.getDetailList();
		for (int i = 0; i < detailList.length; i++) {
			assertTrue(detailList[i].charAt(155) == '1'
					|| detailList[i].charAt(155) == '2');
		}
	}

	public void testCheckingDetailUnusedCharge() {
		String[] detailList = codpayment.getDetailList();
		for (int i = 0; i < detailList.length; i++) {
			assertEquals(detailList[i].substring(129, 146), "00000000+00000000");
		}
	}

	public void testCheckingDetailSuccessCharge() {
		String[] detailList = codpayment.getDetailList();

		int j = 0;
		for (int i = 0; i < detailList.length; i++) {
			if (detailList[i].charAt(155) == '1') {
				j++;
				assertEquals(detailList[i].substring(120, 128), detailList[i]
						.substring(157, 165));

				assertEquals("00000000", detailList[i].substring(147, 155));
			}
		}

		System.out.println(j + " successful tranactions verified.");
	}

	public void testCheckingDetailFailureCharge() {
		String[] detailList = codpayment.getDetailList();
		int j = 0;
		for (int i = 0; i < detailList.length; i++) {
			// assertEquals('2',detailList[i].charAt(155));
			if (detailList[i].charAt(155) == '2') {
				j++;
				assertEquals(detailList[i].substring(120, 128), detailList[i]
						.substring(147, 155));

				assertEquals("00000000", detailList[i].substring(157, 165));
			}
		}

		System.out.println(j + " failure tranactions verified.");
	}

	public void testCheckingTrailerUnusedCharge() {
		String trailer = codpayment.getTrailer();

		assertEquals(trailer.substring(20, 40), "00000000000000000000");
	}

	public void testCheckingTrailerChargeSum() {
		String[] detailList = codpayment.getDetailList();
		int charge = 0;
		for (int i = 0; i < detailList.length; i++) {
			charge = charge
					+ Integer.parseInt(detailList[i].substring(120, 128));

		}

		assertEquals(charge, Integer.parseInt(codpayment.getTrailer()
				.substring(10, 20)));

	}

	public void testCheckingTrailerCreditSum() {
		String[] detailList = codpayment.getDetailList();
		int charge = 0;
		for (int i = 0; i < detailList.length; i++) {
			charge = charge
					+ Integer.parseInt(detailList[i].substring(147, 155));
		}
		assertEquals(charge, Integer.parseInt(codpayment.getTrailer()
				.substring(40, 50)));
	}

	public void testCheckingTrailerBalanceSum() {
		String[] detailList = codpayment.getDetailList();
		int charge = 0;
		for (int i = 0; i < detailList.length; i++) {
			charge = charge
					+ Integer.parseInt(detailList[i].substring(157, 165));
		}
		System.out.println("sum:"+charge);
		System.out.println("trailer:"+Integer.parseInt(codpayment.getTrailer()
				.substring(50, 60)));
		assertEquals(charge, Integer.parseInt(codpayment.getTrailer()
				.substring(50, 60)));
		
		
		
		assertEquals(Integer
				.parseInt(codpayment.getTrailer().substring(10, 20))
				- Integer.parseInt(codpayment.getTrailer().substring(40, 50)),
				Integer.parseInt(codpayment.getTrailer().substring(50, 60)));
	}

	public void testListSuccessTransactions() {
		String[] detailList = codpayment.getDetailList();
		System.out.println("Printing successful transactions:");
		for (int i = 0; i < detailList.length; i++) {
			if (detailList[i].charAt(155) == '1') {
				System.out.println(detailList[i].substring(9, 33).trim() + "|"
						+ detailList[i].substring(120, 128)+ "|order_date:"+detailList[i].substring(103,111)
						+ "|deliver_date:"+detailList[i].substring(172,180)
						+ "|deposit_date:"+detailList[i].substring(111,119));
			}
		}
	}

	public void testListFailureTransactions() {
		String[] detailList = codpayment.getDetailList();
		System.out.println("Printing failed transactions:");
		for (int i = 0; i < detailList.length; i++) {
			if (detailList[i].charAt(155) == '2') {
				System.out.println(detailList[i].substring(9, 33).trim() + "|"
						+ detailList[i].substring(120, 128)+ "|order_date:"+detailList[i].substring(103,111)
						+ "|deliver_date:"+detailList[i].substring(172,180)
						+ "|deposit_date:"+detailList[i].substring(111,119));
			}
		}
	}
}
