package cn.explink.service.addressmatch;

import java.util.List;

public interface AddressProcessStep {
	public List<MatchResult> getMatch(String address, List<MatchResult> branches);
}
