/**
 *
 */
package cn.explink.domain;


/**
 * @author Administrator
 *
 */
public class PenalizeType {
private int id;
private int type;
private String text;
private int parent;
private int state;
/**
 * @return the id
 */
public int getId() {
	return this.id;
}
/**
 * @param id the id to set
 */
public void setId(int id) {
	this.id = id;
}
/**
 * @return the type
 */
public int getType() {
	return this.type;
}
/**
 * @param type the type to set
 */
public void setType(int type) {
	this.type = type;
}
/**
 * @return the text
 */
public String getText() {
	return this.text;
}
/**
 * @param text the text to set
 */
public void setText(String text) {
	this.text = text;
}
/**
 * @return the parentId
 */
public int getParent() {
	return this.parent;
}
/**
 * @param parentId the parentId to set
 */
public void setParent(int parent) {
	this.parent = parent;
}
/**
 * @return the state
 */
public int getState() {
	return this.state;
}
/**
 * @param state the state to set
 */
public void setState(int state) {
	this.state = state;
}

}
