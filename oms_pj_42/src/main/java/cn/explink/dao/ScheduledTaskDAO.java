package cn.explink.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.explink.dao.support.BasicJdbcTemplateDaoSupport;
import cn.explink.domain.ScheduledTask;

@Repository("scheduledTaskDAO")
public class ScheduledTaskDAO extends BasicJdbcTemplateDaoSupport<ScheduledTask, Long> {

	public ScheduledTaskDAO() {
		super(ScheduledTask.class);
	}

	public ScheduledTask lock(Long id) {
		return super.lock(id);
	}

	public List<Long> listAllTasksByType(String taskType) {
		String sql = "select ID from SCHEDULED_TASKS where FIRE_TIME < now() and STATUS = 0 and TASK_TYPE = ?";
		return getJdbcTemplate().queryForList(sql, Long.class, taskType);
	}

	public int countTimeOutTasks(String[] taskTypes, Date time) {
		StringBuilder sql = new StringBuilder("select count(1) from SCHEDULED_TASKS where STATUS = 0 and task_type in (");
		for (int i = 0; i < taskTypes.length; i++) {
			if (i != 0) {
				sql.append(", ");
			}
			sql.append("'").append(taskTypes[i]).append("'");
		}
		sql.append(") and fire_time < ?");
		return getJdbcTemplate().queryForInt(sql.toString(), time);
	}

}
