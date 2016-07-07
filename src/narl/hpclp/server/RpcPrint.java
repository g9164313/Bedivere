package narl.hpclp.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import narl.hpclp.shared.Const;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

@SuppressWarnings("serial")
public class RpcPrint extends HttpServlet {

	@Override
	public void doGet(
		HttpServletRequest req, 
		HttpServletResponse res
	) throws ServletException, IOException {

		String path = req.getServletPath().replace("/","");		
		int pos = path.lastIndexOf(".");
		if(pos<0){
			return;
		}
		
		String name = path.substring(0,pos);
		String appx = path.substring(pos+1);

		if(appx.equalsIgnoreCase("pdf")==true){
			
			Map<String,Object> parm = mEmptyParm;
			
			JRDataSource dsrc = mEmptyDSrc;
			
			if(path.equalsIgnoreCase(Const.REPORT_LETTER)==true){
					
				dsrc = new DSrcLetter();
				
			}else if(path.equalsIgnoreCase(Const.REPORT_NOTIFY)==true){
				
				dsrc = new DSrcNotify();
				
			}else if(
				path.equalsIgnoreCase(Const.REPORT_SERVICE)==true ||
				path.equalsIgnoreCase(Const.REPORT_DEMAND)==true 
			){
				//dsrc = new DsrcServAndDemand(uuid.split(","));//the filed are overlap~~~
				
			}else if(path.startsWith("prodx_")==true){
				
				//dsrc = new DsrcProduct(uuid.split(","));//they must be same~~~			
			}
			
			FillPdfReport(res,name+".jasper",parm,dsrc);
			
		}else if(path.equalsIgnoreCase("xls")==true){
			
		}
	}
	//----------------------------------------//
	
	
	//----------------------------------------//
	
	public static String DOC_PATH = "./narl.hpclp.Jasper/";	

	private static Map<String,Object> mEmptyParm = new HashMap<String,Object>();
	
	private static JRDataSource mEmptyDSrc = new JRDataSource(){
		@Override
		public Object getFieldValue(JRField arg0) throws JRException {
			return null;
		}
		@Override
		public boolean next() throws JRException {
			return false;
		}
	};	
	//----------------------------------------//
	
	protected void FillPdfReport(			
		HttpServletResponse resp,
		String temp,
		Map<String, Object> parm,
		JRDataSource dsrc
	) throws IOException {
		resp.setContentType("application/pdf");
		ServletOutputStream stream = resp.getOutputStream();
		try {
			JRPdfExporter export = new JRPdfExporter();
			export.setParameter(JRExporterParameter.OUTPUT_STREAM,stream);
			JasperPrint print = JasperFillManager.fillReport(
				DOC_PATH+temp,
				parm,
				dsrc
			);
			export.setParameter(JRExporterParameter.JASPER_PRINT,print);
			export.exportReport();
		} catch (JRException e) {
			e.printStackTrace();
		} finally {
			stream.flush();
			stream.close();
		}
	}

	protected void FillXlsReport(
		HttpServletResponse resp,
		String temp, 
		Map<String, Object> parm,
		JRDataSource dsrc
	) throws IOException {
		resp.setContentType("application/xls");
		ServletOutputStream stream = resp.getOutputStream();
		try {
			JRXlsExporter export = new JRXlsExporter();
			export.setParameter(JRExporterParameter.OUTPUT_STREAM, stream);
			JasperPrint print = JasperFillManager.fillReport(
				DOC_PATH+temp,
				parm,
				dsrc
			);
			export.setParameter(JRExporterParameter.JASPER_PRINT, print);
			export.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.FALSE);
			export.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);
			export.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);
			export.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,Boolean.TRUE);
			export.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,Boolean.TRUE);
			export.exportReport();
		} catch (JRException e) {
			e.printStackTrace();
		} finally {
			stream.flush();
			stream.close();
		}
	}
}
