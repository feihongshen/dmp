package cn.explink.filter;


import java.util.regex.Matcher;
import java.util.regex.Pattern;


import cn.explink.core.utils.StringUtils;

public class AntiSqlInjectFilter extends HttpRequestWordFilter {

	@Override
	protected String filterParamValue(String content) {
		// 如果为空字符串，则直接返回
		if (StringUtils.isEmpty(content)) {
			return content;
		}
		// 过滤information_schema
		String tempContent = content.replaceAll("(?i)information_schema", "ＩＮＦＯＲＭＡＴＩＯＮ＿ＳＣＨＥＭＡ").replace("'", "\\'"); // 过滤单引号'
		// 下面是正则替换
		StringBuffer regxpSb = new StringBuffer();
		// 过滤select from
		// ([\\s\t\n\r]{1,})表示至少含1个空格或回车或tab,([^\\s]{1,})表示至少含1个非空格或回车或tab
		regxpSb.append("select([\\s\t\n\r]{1,})([^\\s\t\n\r]{1,})([\\s\t\n\r]{1,})from");
		regxpSb.append("|");
		// 过滤union select
		regxpSb.append("union([\\s\t\n\r]{1,})select");
		regxpSb.append("|");
		// 过滤union all select
		regxpSb.append("union([\\s\t\n\r]{1,})all([\\s\t\n\r]{1,})select");
		regxpSb.append("|");
		// 过滤or
		regxpSb.append("or([\\s\t\n\r]{1,})([^\\s\t\n\r]{1,})");
		regxpSb.append("|");
		// 过滤SYSTEM_USER()
		regxpSb.append("SYSTEM_USER(\\s*)");

		Pattern pattern = Pattern.compile(regxpSb.toString(), Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(tempContent);
		boolean hasNext = matcher.find();
		if (!hasNext) {
			return tempContent;
		}
		StringBuffer encodeSb = new StringBuffer();
		do {
			// 获取字符串里对应的tag
			String tagIncontent = matcher.group();
			// (?i)表示不区分大小写
			tagIncontent = tagIncontent.replaceAll("(?i)select", "ＳＥＬＥＣＴ").replaceAll("(?i)from", "ＦＲＯＭ")
					.replaceAll("(?i)union", "ＵＮＩＯＮ").replaceAll("(?i)all", "ＡＬＬ").replaceAll("(?i)or", "ＯＲ")
					.replaceAll("(?i)SYSTEM_USER", "ＳＹＳＴＥＭ＿ＵＳＥＲ");
			matcher.appendReplacement(encodeSb, tagIncontent);
		} while (matcher.find());

		matcher.appendTail(encodeSb);
		return encodeSb.toString();
	}

}
