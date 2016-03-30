package cn.explink.print.template;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.explink.enumutil.PrintTemplateOpertatetypeEnum;

public class PrintTemplate {
	
	private static Logger logger = LoggerFactory.getLogger(PrintTemplate.class);
	
	long id;
	String name;
	String customname;
	String detail;
	long shownum;
	long templatetype;
	long opertatetype;
	List<PrintColumn> columns;

	ObjectMapper objectMapper = new ObjectMapper();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		try {
			this.columns = objectMapper.readValue(detail, new TypeReference<List<PrintColumn>>() {
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		this.detail = detail;
	}

	public List<PrintColumn> getColumns() {
		return columns;
	}

	public long getShownum() {
		return shownum;
	}

	public void setShownum(long shownum) {
		this.shownum = shownum;
	}

	public long getTemplatetype() {
		return templatetype;
	}

	public long getOpertatetype() {
		return opertatetype;
	}

	public void setOpertatetype(long opertatetype) {
		if (opertatetype == PrintTemplateOpertatetypeEnum.ChuKuAnDan.getValue() || opertatetype == PrintTemplateOpertatetypeEnum.LingHuoAnDan.getValue()
				|| opertatetype == PrintTemplateOpertatetypeEnum.TuiGongYingShangChuKuAnDan.getValue() || opertatetype == PrintTemplateOpertatetypeEnum.TuiHuoChuZhanAnDan.getValue()
				|| opertatetype == PrintTemplateOpertatetypeEnum.ZhongZhuanChuZhanAnDan.getValue() || opertatetype == PrintTemplateOpertatetypeEnum.ZhanDianChuZhanAnDan.getValue()) {
			this.templatetype = 1;
		} else if (opertatetype == PrintTemplateOpertatetypeEnum.ChuKuHuiZong.getValue() || opertatetype == PrintTemplateOpertatetypeEnum.LingHuoHuiZong.getValue()
				|| opertatetype == PrintTemplateOpertatetypeEnum.TuiGongYingShangChuKuHuiZong.getValue() || opertatetype == PrintTemplateOpertatetypeEnum.TuiHuoChuZhanHuiZong.getValue()
				|| opertatetype == PrintTemplateOpertatetypeEnum.ZhongZhuanChuZhanHuiZong.getValue() || opertatetype == PrintTemplateOpertatetypeEnum.ZhanDianChuZhanHuiZong.getValue()) {
			this.templatetype = 2;
		}else if (opertatetype == PrintTemplateOpertatetypeEnum.TongLuTuiHuoShangChuKu.getValue()) {
			this.templatetype = 5;
		}else if (opertatetype == PrintTemplateOpertatetypeEnum.ChuKuAnBao.getValue()) {
			this.templatetype = 4;
		}
		this.opertatetype = opertatetype;
	}

	public String getCustomname() {
		return customname;
	}

	public void setCustomname(String customname) {
		this.customname = customname;
	}

	public void setTemplatetype(long templatetype) {
		this.templatetype = templatetype;
	}

}
