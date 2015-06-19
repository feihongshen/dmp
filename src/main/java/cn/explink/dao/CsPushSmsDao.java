package cn.explink.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import cn.explink.core.pager.PropertyFilter.MatchType;

import cn.explink.core.bean.Criteria;
import cn.explink.core.dao.impl.JdbcDaoImpl;
import cn.explink.core.interceptor.PageControl;
import cn.explink.core.pager.Pager;
import cn.explink.core.pager.PropertyFilter;
import cn.explink.entity.CsPushSms;

@Component
public class CsPushSmsDao extends JdbcDaoImpl {

	//C
	/**
	 * 新增一条记录，并返回新增记录的自增id
	 * @param csPushSms
	 * @return
	 */
	public Long createReId(CsPushSms csPushSms){
		return super.insert(csPushSms);
	}
	//R
	/**
	 * 按id获取对象
	 * @param id
	 * @return
	 */
	public CsPushSms find(Long id){
		return super.get(CsPushSms.class, id);
	}
	/**
	 * 获取所有对象
	 * @return
	 */
	public List<CsPushSms> findAll(){
		return super.queryList(Criteria.create(CsPushSms.class));
	}
	/**
	 * 根据条件获取对象（属性条件）
	 * @param csPushSms
	 * @return
	 */
	public List<CsPushSms> findListByCondition(CsPushSms csPushSms){
		return super.queryList(csPushSms);
	}
	//U
	/**
	 * 根据id更新（对象id不可为空）
	 * @param csPushSms
	 */
	public void update(CsPushSms csPushSms){
		super.update(csPushSms);
	}
	//D
	/**
	 * 根据条件删除对象（若有id，id为唯一条件；无则根据属性条件）
	 * @param csPushSms
	 */
	public void delete(CsPushSms csPushSms){
		super.delete(csPushSms);
	}
	//P
	/**
	 * 根据条件查询分页数据
	 * @param page
	 * @param criteria
	 * @return
	 */
	public Pager findPage(Pager page,Criteria criteria){
		PageControl.performPage(page);
		super.queryList(criteria);
    	return page;
	}
	
	/**
	 * 
	 * @param pager
	 * @param filters
	 */
	public void queryData(Pager pager, List<PropertyFilter> filters) {
		
		Criteria criteria = this.buildCriterionByPropertyFilter(filters);
		PageControl.performPage(pager);
		super.queryList(criteria);
	}
	
	/**
	 * 构建sql基类
	 * @param filters
	 * @return
	 */
	private Criteria buildCriterionByPropertyFilter(List<PropertyFilter> filters) {

		Criteria criteria = Criteria.create(CsPushSms.class);
		for (PropertyFilter filter : filters) {
			//只有一个属性需要比较的情况.
			if (!filter.hasMultiProperties()) { 
				criteria = this.buildCriterion(filter.getPropertyName(), filter.getMatchValue(), filter.getMatchType(), criteria);
			}
			//包含多个属性需要比较的情况,进行or处理.
			else {
				for (String propertyName : filter.getPropertyNames()) {
					criteria = this.buildCriterion(propertyName, filter.getMatchValue(), filter.getMatchType(), criteria);
				}
			}
		}
		return criteria;
	}
	
	/**
	 * 按属性条件参数初始化criteria
	 */
	protected Criteria buildCriterion(final String propertyName, final Object propertyValue, final MatchType matchType, Criteria criteria) {
		Assert.hasText(propertyName, "propertyName不能为空");
		//根据MatchType初始化criteria
		switch (matchType) {
		case EQ:
			criteria.and(propertyName, "=", new Object[]{propertyValue});
			break;
		case LIKE:
			//TODO like 添加百分号
			criteria.and(propertyName, "like", new Object[]{propertyValue});
			break;
		case LE:
			criteria.and(propertyName, "<=", new Object[]{propertyValue});
			break;
		case LT:
			criteria.and(propertyName, "<", new Object[]{propertyValue});
			break;
		case GE:
			criteria.and(propertyName, ">=", new Object[]{propertyValue});
			break;
		case GT:
			criteria.and(propertyName, ">", new Object[]{propertyValue});
		}
		return criteria;
	}

}
