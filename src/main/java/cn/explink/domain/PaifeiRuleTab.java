/**
 *
 */
package cn.explink.domain;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Administrator
 *
 */
public abstract class PaifeiRuleTab {
	 Collectionjson collection;
	 Basicjson basic;
	 List<PFinsertion> insertion;
	 BigDecimal subsidyfee;
	/**
	 * @return the collection
	 */
	public Collectionjson getCollection() {
		return this.collection;
	}
	/**
	 * @param collection the collection to set
	 */
	public void setCollection(Collectionjson collection) {
		this.collection = collection;
	}
	/**
	 * @return the basic
	 */
	public Basicjson getBasic() {
		return this.basic;
	}
	/**
	 * @param basic the basic to set
	 */
	public void setBasic(Basicjson basic) {
		this.basic = basic;
	}
	/**
	 * @return the insertion
	 */
	public List<PFinsertion> getInsertion() {
		return this.insertion;
	}
	/**
	 * @param insertion the insertion to set
	 */
	public void setInsertion(List<PFinsertion> insertion) {
		this.insertion = insertion;
	}
	/**
	 * @return the subsidyfee
	 */
	public BigDecimal getSubsidyfee() {
		return this.subsidyfee;
	}
	/**
	 * @param subsidyfee the subsidyfee to set
	 */
	public void setSubsidyfee(BigDecimal subsidyfee) {
		this.subsidyfee = subsidyfee;
	}


}
