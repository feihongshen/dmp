import junit.framework.Assert;

import org.junit.Test;


public class HttpClientTest {

	@Test
	public void testCrackEms() throws Exception{
		
		StringBuilder builder=new StringBuilder();
		builder.append("'").append("123").append("',");

		if(builder.length()>1){
			builder.deleteCharAt(builder.length()-1);
		}
		Assert.assertEquals("'123'",  builder.toString());
	}

	/*private String getCheckcode(HttpClient httpClient) throws IOException, HttpException, FileNotFoundException, Exception {
		GetMethod getMethod=new GetMethod("http://www.ems.com.cn/ems/rand?"+new Random().nextDouble());
		httpClient.executeMethod(getMethod);
		CookieSpec cookiespec = CookiePolicy.getDefaultSpec();
		 Cookie[] cookies = cookiespec.match("www.ems.com.cn", 80端口, "/" , false , httpClient.getState().getCookies());
         for (Cookie cookie : cookies) {
             System.out.println(cookie.getName() + "##" + cookie.getValue());
         }
         String filename=UUID.randomUUID().toString();
         File storeFile = new File(filename+".jpg");  
         FileOutputStream output = new FileOutputStream(storeFile);  
         //得到网络资源的字节数组,并写入文件  
         output.write(getMethod.getResponseBody());  
         output.close();
        String maybe = this.recognizeText(storeFile).replaceAll("\n", "").replaceAll("\\.", "");
		return maybe;
	}
	
	  public String recognizeText(File file) throws Exception {
	    	String filename=file.getName().split("\\.")[0];
			DefaultExecutor executor = new DefaultExecutor();
			executor.setWorkingDirectory(file.getParentFile());
	    	CommandLine convertCommandLine=CommandLine.parse("C:\\Program Files\\ImageMagick-6.7.9-Q16\\convert "+filename+".jpg -colors 2 -colorspace gray -normalize "+filename+".png");
	    	executor.execute(convertCommandLine);
	    	CommandLine tifCommandLine=CommandLine.parse("C:\\Program Files\\ImageMagick-6.7.9-Q16\\convert "+filename+".png "+filename+".tif");
	    	executor.execute(tifCommandLine);
	    	CommandLine commandLine=CommandLine.parse("tesseract "+filename+".tif "+filename+" nobatch digits");
	    	executor.execute(commandLine);
	    	String result=loadAFileToStringDE1(new File(filename+".txt"));
	    	CommandLine delcommandLine=CommandLine.parse("rm -f "+filename+".* ");
	    	executor.execute(delcommandLine);
	    	return result;
	    }

		 public String loadAFileToStringDE1(File f) throws IOException {  
		        InputStream is = null;
		        String ret = null;
		        try {
		            is = new BufferedInputStream( new FileInputStream(f) );
		            long contentLength = f.length();
		            ByteArrayOutputStream outstream = new ByteArrayOutputStream( contentLength > 0 ? (int) contentLength : 1024);
		            byte[] buffer = new byte[4096];
		            int len;
		            while ((len = is.read(buffer)) > 0) {
		                outstream.write(buffer, 0, len);
		            }            
		            outstream.close();
		            ret = outstream.toString();
		            //byte[] ba = outstream.toByteArray();
		            //ret = new String(ba);
		        } finally {
		            if(is!=null) {try{is.close();} catch(Exception e){} }
		        }
		        return ret;        
		    }*/
}
