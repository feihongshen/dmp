package cn.explink.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import cn.explink.util.exception.ValidationExeception;

/**
 * @Desc 对execel文件的校验并且解析
 *
 */
public class ExecelImportAndValidateUtil {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private String description = "";// 如果校验失败，将会给出详细提示信息
	private Sheet sheet;// execel 对象
	private List<String> fieldList;// 从xml读取到的execel表格信息
	private int rowIndex = 0;// 当前操作行
	private Object objectBean;// 每一行数据封装
	private Cell cellStart;// 数据的开始单元格
	@SuppressWarnings("rawtypes")
	private Class clazz; // 需要封装的类
	private Validator validator; // hibernate 的校验器
	private String[] fieldVals; // 从execel读到的某一行的数据
	private int fieldSize = 0; // 有效数据的列数
	DateFormat format = new SimpleDateFormat("yyyy-MM-dd"); // 针对日期的默认转换形式
	private Expression exp;// EL 解析器
	private ExpressionParser parser;

	public String getDescription() {
		return this.description;
	}

	public Object getObjectBean() {
		return this.objectBean;
	}

	/**
	 *
	 * @param execelFilename
	 *            execel文件名
	 * @param xmlFilename
	 *            execel文件所对应的校验文件
	 * @param calzz
	 *            需要封装的类
	 */
	public ExecelImportAndValidateUtil(InputStream execelIS, String xmlFilename, Class clazz) throws ValidationExeception {
		// 打开execel工作簿
		Workbook wb = null;
		try {
			wb = new HSSFWorkbook(execelIS);
		} catch (IOException e) {
			this.logger.error(e.getMessage());
			throw new ValidationExeception("", "加载文件失败,请确保是否是Execel表格");
		}
		this.sheet = wb.getSheetAt(0);// 默认取第一个工作簿
		// 读配置文件，获取所有的属性列描述
		this.fieldList = this.readFieldsFromXML(this.getAbsolutePath(xmlFilename));
		// 个数
		this.fieldSize = this.fieldList.size();

		// 找到有效数据的开始单元格
		this.cellStart = this.findStartCell();
		if (this.cellStart == null) {
			throw new ValidationExeception("", this.description);
		}

		// 每次读取一行execel数据，rowIndex每次增1
		this.rowIndex = this.cellStart.getRowIndex() + 2;

		// 需要封装的对象类
		this.clazz = clazz;
		// 初始化校验器
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		this.validator = factory.getValidator();
		// 初始化EL解析器
		this.parser = new SpelExpressionParser();
		this.exp = this.parser.parseExpression("values");
	}

	// 是否还有数据
	public boolean hasNext() {
		Row row = this.sheet.getRow(this.rowIndex++);
		if (row == null) {
			return false;
		}
		this.fieldVals = this.getRowValues(row, this.cellStart.getColumnIndex());
		// if (Arrays.asList(this.fieldVals).indexOf("") != -1) {
		// for (String s : this.fieldVals) {
		// // 如果每个字段都是空的，则返回false 否则true
		// if (!s.equals("")) {
		// return true;
		// }
		// }
		// return false;
		// }
		if (this.rowIndex <= (this.sheet.getLastRowNum() + 1)) {
			return true;
		}
		return false;
	}

	// 校验
	public boolean validate() {
		try {
			this.objectBean = Class.forName(this.clazz.getName()).newInstance();
		} catch (Exception e) {
			this.logger.error(e.getMessage());
		}

		try {
			this.exp.setValue(this.objectBean, this.fieldVals);// 给objectBean的属性赋值
		} catch (EvaluationException e) {// 由于所有的数据类型转换都有objectBean里面来处理，故可能有异常，需要进行相应的处理
			List exList = Arrays.asList("ParseException", "NumberFormatException");// 一般可能发生的异常
			Throwable t = e.getCause();
			while (t != null) {
				String causeClazz = t.getClass().getSimpleName();
				if (exList.contains(causeClazz)) {
					this.description = "第" + this.rowIndex + "行，类型转换失败：" + t.getMessage();
					return false;
				} else if (causeClazz.equals("ValidationExeception")) {// 自定义异常
					this.description = "第" + this.rowIndex + "行," + t.getMessage();
					return false;
				} else {
					t = t.getCause();
				}
			}
			this.description = this.parser.parseExpression("message").getValue(this.objectBean, String.class);
			return false;
		}
		// 校验，校验规则是配置在objectBean对象里面
		Set<ConstraintViolation<Object>> constraintViolations = this.validator.validate(this.objectBean);
		if (constraintViolations.size() > 0) {// 校验失败时，提示相应信息
			for (ConstraintViolation<Object> vl : constraintViolations) {
				this.description = "第" + this.rowIndex + "行，校验出错：" + vl.getPropertyPath() + "=" + vl.getInvalidValue() + ":" + vl.getMessage();
				return false;
			}
		}
		return true;
	}

	private String[] getRowValues(Row row, int columnStartIndex) {
		String[] values = new String[this.fieldSize];
		for (int j = columnStartIndex, t = 0; t < this.fieldSize; j++, t++) {
			Cell c = row.getCell(j);
			if (c == null) {
				values[t] = "";
				continue;
			}
			switch (c.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				values[t] = "";
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				values[t] = String.valueOf(c.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(c)) {
					values[t] = this.format.format(c.getDateCellValue());
				} else {
					values[t] = String.valueOf(c.getNumericCellValue());
				}
				break;
			case Cell.CELL_TYPE_STRING:
				values[t] = String.valueOf(c.getStringCellValue());
				break;
			default:
				values[t] = "";
				break;
			}
		}
		return values;
	}

	// 根据某一个单元格，得到更人性化的显示,例如“A4”
	private String getCellRef(Cell cell) {
		return org.apache.poi.ss.util.CellReference.convertNumToColString(cell.getColumnIndex()) + (cell.getRowIndex() + 1);
	}

	private String getAbsolutePath(String file) throws ValidationExeception {
		try {
			file = this.getClass().getClassLoader().getResource(file).getFile();

		} catch (NullPointerException e) {
			throw new ValidationExeception(file, "文件不存在");
		}
		try {
			// 解决当出现中文路径时不能解析的bug
			file = URLDecoder.decode(file, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new ValidationExeception(file, "解码失败");
		}
		return file;
	}

	private List<String> readFieldsFromXML(String xmlFilename) throws ValidationExeception {
		SAXReader reader = new SAXReader();
		Document document = null;

		try {
			document = reader.read(new File(xmlFilename));// 加载配置文件
		} catch (DocumentException e) {
			e.printStackTrace();
			this.description = "IO 异常，读取配置文件失败";
			throw new ValidationExeception(xmlFilename, "IO 异常，读取配置文件失败");
		}

		Element root = document.getRootElement();
		List<String> fields = new ArrayList<String>();
		for (Iterator iter = root.elementIterator("field"); iter.hasNext();) {
			Element field = (Element) iter.next();
			fields.add(field.getTextTrim());
		}

		return fields;
	}

	/**
	 * 从execel表中找到数据开始的单元格
	 *
	 * @return
	 */
	private Cell findStartCell() {
		String firstFieldDesc = this.fieldList.get(0);
		int endRow = this.sheet.getLastRowNum() > 100 ? 100 : this.sheet.getLastRowNum();
		for (int i = 0; i <= endRow; i++) {
			Row r = this.sheet.getRow(i);
			if (r == null) {
				continue;
			}
			for (int j = 0; j < r.getLastCellNum(); j++) {
				Cell c = r.getCell(j);
				if (c == null) {
					continue;
				}
				if (c.getCellType() == Cell.CELL_TYPE_STRING) {
					if (c.getStringCellValue().trim().equals(firstFieldDesc)) {// 找到第一个符合要求的字段，接下来判断它相邻的字段是否都符合要求
						if (this.fieldList.size() > (r.getLastCellNum() - j)) {
							this.description = "execel表格与所给配置描述不符，请下载模板文件";
							return null;
						}
						for (int k = j + 1, t = 1; k <= ((j + this.fieldList.size()) - 1); k++, t++) {
							Cell c2 = r.getCell(k);
							if (c2 == null) {
								this.description = "请确保单元格" + this.getCellRef(c2) + "内容是\"" + this.fieldList.get(t) + "\"";
								return null;
							}
							if (c2.getCellType() == Cell.CELL_TYPE_STRING) {
								if (c2.getStringCellValue().contains(this.fieldList.get(t))) {
									continue;
								} else {
									this.description = "请确保单元格" + this.getCellRef(c2) + "内容是\"" + this.fieldList.get(t) + "\"";
									return null;
								}
							}

						}
						return c;
					} else {
						continue;
					}
				} else {
					continue;
				}
			}
		}
		this.description = "找不到\"" + this.fieldList.get(0) + "\"这一列";
		return null;
	}
}
