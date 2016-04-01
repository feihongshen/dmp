package cn.explink.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManifestUtil {
	
	private static Logger logger = LoggerFactory.getLogger(ManifestUtil.class);
	
	public static String getSvnRevision(HttpServletRequest request) {

		try {
			String appServerHome = request.getSession().getServletContext().getRealPath("/");
			File manifestFile = new File(appServerHome, "META-INF/MANIFEST.MF");
			Manifest mf = new Manifest();
			mf.read(new FileInputStream(manifestFile));
			Attributes atts = mf.getMainAttributes();
			return atts.getValue("SVN-Revision");
		} catch (FileNotFoundException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}
		return "";

	}
}
