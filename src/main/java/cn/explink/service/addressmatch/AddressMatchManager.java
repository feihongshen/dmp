package cn.explink.service.addressmatch;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressMatchManager {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private List<AddressProcessStep> addressProcessSteps = new ArrayList<AddressProcessStep>();

	public List<MatchResult> getMatch(String address) {
		List<MatchResult> list = null;
		for (AddressProcessStep addressProcessStep : addressProcessSteps) {
			list = addressProcessStep.getMatch(address, list);
			if (list.size() <= 1) {
				break;
			}
		}
		logger.info("find {} match(es)", list.size());
		return list;
	}

}
