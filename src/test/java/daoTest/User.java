package daoTest;

import java.util.Date;

import cn.explink.core.pager.Pageable;

/**
 * 测试类
 * @author gaoll
 *
 */
/*
 * CREATE TABLE `epmanagercenter_test`.`user`( `user_id` BIGINT AUTO_INCREMENT, `user_name` VARCHAR(256), `user_age` INT, `gmt_create` DATE, `gmt_modify` DATE, KEY(`user_id`) ); 
 */
public class User extends Pageable {

    private Long id;

    private String  userName;

    private Integer userAge;

    private Date gmtCreate;

    private Date gmtModify;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserAge() {
        return userAge;
    }

    public void setUserAge(Integer userAge) {
        this.userAge = userAge;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }
    
}
