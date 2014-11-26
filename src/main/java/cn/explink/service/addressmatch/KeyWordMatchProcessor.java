package cn.explink.service.addressmatch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

@Component
public class KeyWordMatchProcessor implements AddressProcessStep {

	private HashMap<String, List<Long>> firstClassMap = new HashMap<String, List<Long>>();

	private HashMap<String, List<Long>> secondClassMap = new HashMap<String, List<Long>>();

	private HashMap<String, List<Long>> thirdClassMap = new HashMap<String, List<Long>>();

	private HashMap<String, List<String>> firstSecondMap = new HashMap<String, List<String>>();

	@Autowired
	JdbcTemplate jdbcTemplate;

	@PostConstruct
	public void init() {

		String sqlAdd = "SELECT f.branchid,f.branchaddresskeyword,s.branchaddresskeywordsecond,branchaddresskeywordthird " + " FROM express_set_address_key_word_branch f  "
				+ " left outer join express_set_address_key_word_branch_second s on f.branchkeyid=s.branchkeyid   "
				+ " left outer join express_set_address_key_word_branch_third t on s.branchkeysecondid=t.branchkeysecondid ";

		jdbcTemplate.query(sqlAdd, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				String firstkeyword = rs.getString("branchaddresskeyword");
				String secondkeyword = rs.getString("branchaddresskeywordsecond");
				String thridkeyword = rs.getString("branchaddresskeywordthird");
				long branchid = rs.getLong("branchid");
				if (firstClassMap.get(firstkeyword) == null) {
					firstClassMap.put(firstkeyword, new ArrayList<Long>());
				}
				if (!firstClassMap.get(firstkeyword).contains(branchid)) {
					firstClassMap.get(firstkeyword).add(branchid);
				}

				if (secondClassMap.get(secondkeyword) == null) {
					secondClassMap.put(secondkeyword, new ArrayList<Long>());
				}
				if (!secondClassMap.get(secondkeyword).contains(branchid)) {
					secondClassMap.get(secondkeyword).add(branchid);
				}

				if (thirdClassMap.get(thridkeyword) == null) {
					thirdClassMap.put(thridkeyword, new ArrayList<Long>());
				}
				if (!thirdClassMap.get(thridkeyword).contains(branchid)) {
					thirdClassMap.get(thridkeyword).add(branchid);
				}
			}
		});
	}

	@Override
	public List<MatchResult> getMatch(String address, List<MatchResult> branches) {
		List<String> firstMathcedKeys = matchAddress(address, firstClassMap.keySet(), true);
		List<MatchResult> firstMatchedBranchid = getFristMatchedBranchids(firstMathcedKeys);
		if (firstMatchedBranchid.size() == 0) {
			return new ArrayList<MatchResult>();
		}
		if (firstMatchedBranchid.size() == 1) {
			return firstMatchedBranchid;
		}
		List<String> secondKeys = getSecondMatchedKeysFromFirstKeys(address, firstMathcedKeys);
		List<MatchResult> secondMatchedBranchid = getSecondMatchedBranchids(secondKeys);
		if (secondMatchedBranchid.size() == 0) {
			return firstMatchedBranchid;
		}
		if (secondMatchedBranchid.size() == 1) {
			return secondMatchedBranchid;
		}
		List<String> thirdKeys = getThirdMatchedKeysFromSecondKeys(address, secondKeys);
		List<MatchResult> thirdMatchedBranchid = new ArrayList<MatchResult>();
		for (String thirdKey : thirdKeys) {
			for (Long branchid : thirdClassMap.get(thirdKey)) {
				thirdMatchedBranchid.add(new MatchResult(branchid, thirdKey));
			}
		}
		if (thirdMatchedBranchid.size() == 0) {
			return secondMatchedBranchid;
		}
		if (thirdMatchedBranchid.size() == 1) {
			return thirdMatchedBranchid;
		}
		return thirdMatchedBranchid;
	}

	private List<String> getThirdMatchedKeysFromSecondKeys(String address, List<String> secondKeys) {
		List<String> thirdKeys = new ArrayList<String>();
		for (String secondKey : secondKeys) {
			thirdKeys.addAll(matchAddress(address, firstSecondMap.get(secondKey), false));
		}
		return thirdKeys;
	}

	private List<MatchResult> getSecondMatchedBranchids(List<String> secondKeys) {
		List<MatchResult> secondMatchedBranchid = new ArrayList<MatchResult>();
		for (String secondky : secondKeys) {
			for (Long branchid : secondClassMap.get(secondky)) {
				secondMatchedBranchid.add(new MatchResult(branchid, secondky));
			}
		}
		return secondMatchedBranchid;
	}

	private List<MatchResult> getFristMatchedBranchids(List<String> firstMathcedKeys) {
		List<MatchResult> firstMatchedBranchid = new ArrayList<MatchResult>();
		for (String match : firstMathcedKeys) {
			for (Long branchid : firstClassMap.get(match)) {
				firstMatchedBranchid.add(new MatchResult(branchid, match));
			}
		}
		return firstMatchedBranchid;
	}

	private List<String> getSecondMatchedKeysFromFirstKeys(String address, List<String> firstMathcedKeys) {
		List<String> secondKeys = new ArrayList<String>();
		for (String firstKey : firstMathcedKeys) {
			List<String> keys = firstSecondMap.get(firstKey);
			if (keys != null && keys.size() > 0) {
				secondKeys.addAll(matchAddress(address, keys, false));
			}
		}
		return secondKeys;
	}

	private List<String> matchAddress(String address, Collection<String> keys, boolean breakOnFirst) {
		ArrayList<String> result = new ArrayList<String>();
		for (String key : keys) {
			if (address.contains(key)) {
				result.add(key);
				if (breakOnFirst) {
					break;
				}
			}
		}
		return result;
	}

}
