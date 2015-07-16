/**
 *
 */
package cn.explink.domain;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author Administrator
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaifeiRuleJson {
private PaifeiRulePS ps;
private PaifeiRuleTH th;
private PaifeiRuleZZ zz;
/**
 * @return the ps
 */
public PaifeiRulePS getPs() {
	return this.ps;
}
/**
 * @param ps the ps to set
 */
public void setPs(PaifeiRulePS ps) {
	this.ps = ps;
}
/**
 * @return the th
 */
public PaifeiRuleTH getTh() {
	return this.th;
}
/**
 * @param th the th to set
 */
public void setTh(PaifeiRuleTH th) {
	this.th = th;
}
/**
 * @return the zz
 */
public PaifeiRuleZZ getZz() {
	return this.zz;
}
/**
 * @param zz the zz to set
 */
public void setZz(PaifeiRuleZZ zz) {
	this.zz = zz;
}
}
