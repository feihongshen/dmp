package daoTest;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.explink.core.bean.Criteria;
import cn.explink.core.dao.JdbcDao;
import cn.explink.core.interceptor.PageControl;
import cn.explink.core.pager.Pager;
import cn.explink.dao.CsPushSmsDao;
import cn.explink.entity.CsPushSms;
import cn.explink.enumutil.ComplaintStateEnum;
import cn.explink.enumutil.ComplaintTypeEnum;
import cn.explink.util.DateTimeUtil;

public class JdbcDaoImplTest extends BaseTest {

    @Autowired
    private JdbcDao jdbcDao;
    @Autowired
    private CsPushSmsDao csPushSmsDao;
    
    private User user;
    private CsPushSms csPushSms;
    @Before
    public void before() {
        user = new User();
        user.setUserName("简易");
        user.setUserAge(20);
        user.setGmtCreate(new Date());
        
//        csPushSms = new CsPushSms("cwbOrderNO", "workOrderNo", ComplaintTypeEnum.CuijianTousu.getValue(),ComplaintStateEnum.YiHeShi.getValue(), "操作人", DateTimeUtil.getNowDate(), "工单内容","123211121");
    }
    
    @Test
    public void insertTest(){
    	Long id = csPushSmsDao.createReId(csPushSms);
    	System.out.println(id);
    }
    
    @Test
    public void insert() {    
    	user.setUserName("简易insert");
        Long id = jdbcDao.insert(user);
        System.out.println(id);
    }

    @Test
    public void insert2() {
        Criteria criteria = Criteria
        		.create(User.class)
        		.set("userName", "详细insert")
        		.set("userAge", 22)
        		.set("gmtCreate", new Date())
        		.set("gmtModify", new Date());
        Long id = jdbcDao.insert(criteria);
        System.out.println(id);
    }

    @Test
    public void save() {
        user.setUserName("不处理主键保存");
        jdbcDao.save(user);
    }

    @Test
    public void save2() {
        Criteria criteria = Criteria
        		.create(User.class)
        		.set("userName", "不处理主键保存2")
        		.set("userAge", 22)
        		.set("gmtCreate", new Date())
        		.set("gmtModify", new Date());
        jdbcDao.save(criteria);
    }

    @Test
    public void update() {
        user.setId(13L);
        user.setUserName("根据主键修改1");
        user.setGmtCreate(null);
        user.setGmtModify(new Date());
        jdbcDao.update(user);
    }

    @Test
    public void update2() {
        Criteria criteria = Criteria
        		.create(User.class)
        		.set("userName", "根据主键修改2")
        		.set("userAge", "18")
        		.where("id", new Object[] { 5L, 6L, 7L });
        jdbcDao.update(criteria);
    }

    @Test
    public void delete() {
        User u = new User();
        u.setUserName("liyd");
        u.setUserAge(20);
        jdbcDao.delete(u);
    }

    @Test
    public void delete2() {
        Criteria criteria = Criteria
        		.create(User.class)
        		.where("userName", new Object[] { "liyd2" })
        		.or("userAge", new Object[]{64});
        jdbcDao.delete(criteria);
    }

    @Test
    public void delete3() {
        jdbcDao.delete(User.class, 25L);
    }

    @Test
    public void queryList() {
        User u = new User();
        u.setUserName("详细insert请问");
        List<User> users = jdbcDao.queryList(u);
        for (User us : users) {
            System.out.println(us.getId() + " " + us.getUserName() + " " + us.getUserAge());
        }
//        Date test = new java.sql.Timestamp(System.currentTimeMillis());
//        System.out.println(test);
    }

	@Test
	public void queryList1() {
	    User user = new User();
	    PageControl.performPage(user);
	    jdbcDao.queryList(user);
	    Pager pager = PageControl.getPager();
	    List<User> users = pager.getList(User.class);
	    System.out.println("总记录数：" + pager.getItemsTotal());
	    for (User us : users) {
	        System.out.println(us.getUserName() + " " + us.getUserAge());
	    }
	}
	
	@Test
	public void queryList2() {
	    PageControl.performPage(1, 1);
	    Criteria criteria = Criteria
	    		.create(User.class)
//	    		.include("userName", "id")
//	    		.where("userName", new Object[]{"简易insert"})
	    		.asc("id");
	    
	    
	    
	    for(int i=1;i<=3;i++){
	    	PageControl.performPage(i, 2);
	    	jdbcDao.queryList(criteria);
	    	List<User> users = PageControl.getPager().getList(User.class);
	    	if( null != users){
	    		for (User us : users) {
	    			System.out.println(us.getId() + " " + us.getUserName() + " " + us.getUserAge());
	    		}
	    	}
	    	System.out.println("====================第"+i+"页===================");
	    }
	}

    @Test
    public void queryList3() {
        Criteria criteria = Criteria
        		.create(User.class)
//        		.include(new String[]{"userId","userName","userAge"})
//        		.where("userName", new Object[]{"简易insert"})
//        		.and("userAge", "in",new Object[]{20,22})
//        		.where("orderNo", "in",new Object[]{"111","222","3333"} )
        		.asc("id")
        		.desc("userAge");
        List<User> users = jdbcDao.queryList(criteria);
        for (User us : users) {
            System.out.println(us.getId() + " " + us.getUserName() + " " + us.getUserAge());
        }
    }

    @Test
    public void queryList4() {
        Criteria criteria = Criteria
        		.create(User.class)
        		.where("userName", "like",new Object[] { "%简易%" });
        User user1 = new User();
        user1.setUserAge(20);
        List<User> users = jdbcDao.queryList(user1, criteria.include("userId"));
        for (User us : users) {
            System.out.println(us.getId() + " " + us.getUserName() + " " + us.getUserAge());
        }
    }

    @Test
    public void queryList5() {
        List<User> users = jdbcDao.queryList(Criteria.create(User.class));
        for (User us : users) {
            System.out.println(us.getId() + " " + us.getUserName() + " " + us.getUserAge());
        }
    }

    @Test
    public void queryCount() {
        User u = new User();
//      u.setUserName("简易");
        u.setUserAge(20);
        int count = jdbcDao.queryCount(u);
        System.out.println(count);
    }

    @Test
    public void queryCount2() {
        Criteria criteria = Criteria
        		.create(User.class)
        		.where("userName", new Object[] { "详细insert","简易insert" })
        		.or("userAge", new Object[]{20,22});
        int count = jdbcDao.queryCount(criteria);
        System.out.println(count);
    }

    @Test
    public void get() {

        User u = jdbcDao.get(User.class, 4L);
        System.out.println(u.getId() + " " + u.getUserName() + " " + u.getUserAge());

    }

    @Test
    public void get2() {
        Criteria criteria = Criteria
        		.create(User.class)
        		.include("userName");
        User u = jdbcDao.get(criteria, 4L);
        System.out.println(u.getId() + " " + u.getUserName() + " " + u.getUserAge());
    }

    @Test
    public void querySingleResult() {
        User u = new User();
        u.setUserName("简易insert");
        u.setUserAge(20);
//        u.setUserId(4L);
        u = jdbcDao.querySingleResult(u);
        System.out.println(u.getId() + " " + u.getUserName() + " " + u.getUserAge());
    }

    @Test
    public void querySingleResult2() {
    	//TODO 若查询结果又多个，返回队列第一个
        Criteria criteria = Criteria
        		.create(User.class)
        		.where("userName", new Object[] { "简易insert","详细insert" })
        		.and("userAge", new Object[]{20,22});
        User u = jdbcDao.querySingleResult(criteria);
        System.out.println(u.getId() + " " + u.getUserName() + " " + u.getUserAge());
    }
    

}