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
import narl.hpclp.shared.ItemProdx;
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
		
		String appx = path.substring(pos+1);

		if(appx.equalsIgnoreCase("pdf")==true){
			
			String name = "";
			
			JRDataSource dsrc = mEmptyDSrc;
			
			if(path.equalsIgnoreCase(Const.PRINT_LETTER)==true){
				name = "report_letter.jasper";
				dsrc = new DSrcOwner();
			}else if(path.equalsIgnoreCase(Const.PRINT_SCHEDULE)==true){
				name = "report_sched.jasper";
				dsrc = new DSrcMeeting();
			}else if(path.equalsIgnoreCase(Const.PRINT_NOTIFY)==true){
				name = "report_notify.jasper";
				dsrc = new DSrcMeeting();								
			}else if(path.equalsIgnoreCase(Const.REPORT_PRODUCT)==true){
				//we can determine jist one format :-(
				ItemProdx itm = DSrcProduct.lst.get(0);
				switch(itm.format){
				default:
				case ItemProdx.FMT_F2:
					if(itm.tenur.isChamber()==true){
						name="prodx_chamber.jasper";
					}else{
						name="prodx_other.jasper";
					}
					break;
				case ItemProdx.FMT_F3:// γ反應報告
					name="prodx_gamma.jasper";
					break;
				case ItemProdx.FMT_F4:// 污染效率報告
					name="prodx_effect.jasper";
					break;
				case ItemProdx.FMT_F5:// 活度報告
					name="prodx_activity.jasper";
					break;
				}
				dsrc = new DSrcProduct();				
			}else if(path.equalsIgnoreCase(Const.REPORT_SERVICE)==true){
				name = "report_service.jasper";
				dsrc = new DSrcAccount();				
			}else if(path.equalsIgnoreCase(Const.REPORT_DEMAND)==true){
				name = "report_demand.jasper";
				dsrc = new DSrcAccount();
			}
			
			FillPdfReport(
				res,
				name,
				mEmptyParm,
				dsrc
			);
			
		}else if(path.equalsIgnoreCase("xls")==true){
			
			FillXlsReport(
				res, 
				"export_account.jasper", 
				mEmptyParm, 
				new DSrcAccount()
			);
		}
	}
	//----------------------------------------//

	public static String DOC_PATH = "./narl.hpclp.Jasper";	

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
				DOC_PATH+"/"+temp,
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
