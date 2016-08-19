package narl.hpclp.server;

import java.util.ArrayList;

import narl.hpclp.shared.ItemMeeting;
import narl.hpclp.shared.ItemTenur;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class DSrcMeeting implements JRDataSource {
	
	private int idx = -1;

	public static ArrayList<ItemMeeting> lst = new ArrayList<ItemMeeting>();
	
	public DSrcMeeting(){
	}
	
	@Override
	public Object getFieldValue(
		JRField arg0
	) throws JRException {

		ItemMeeting meet = lst.get(idx);
		
		String txt, name = arg0.getName();
		
		if(name.equals("owner_code")){
			
			return meet.getKey();
		
		}else if(name.equals("owner_name")){
		
			return meet.getName();
		
		}else if(name.equals("owner_zip")){
			
			txt =  meet.getZip();
			if(txt.length()!=0){
				txt = txt + "   ";
			}
			return txt;
			
		}else if(name.equals("owner_addr")){
			
			txt =  meet.getZip()+"  "+meet.getAddress();
			if(txt.length()!=0){
				txt = txt + "   ";
			}
			return txt;
			
		}else if(name.equals("owner_dept")){
			
			txt =  meet.getDepartment();
			if(txt.length()!=0){
				txt = txt + "   ";
			}
			return txt;
			
		}else if(name.equals("owner_contact")){	
		
			return meet.getPerson();
		
		}else if(name.equals("owner_phone")){	
			
			return meet.getPhone();
		
		}else if(name.equals("meet_day")){	
		
			return UtilsMisc.date2tw_d(meet.stmp);
			
		}else if(name.equals("meet_hour")){	
			
			return UtilsMisc.fmtTime.format(meet.stmp);
			
		}else if(name.equals("tenure_source")){	

			return new DrscTenure(meet.lst);
			
		}else if(name.equals("total_tenure")){
			
			return meet.lst.size();				
		}
		return null;
	}

	@Override
	public boolean next() throws JRException {
		if (lst == null) {
			return false;
		}
		idx++;
		if (idx < lst.size()) {
			return true;
		}
		return false;
	}
		
	public class DrscTenure implements JRDataSource {
		
		private int _idx = -1;
		
		private ArrayList<ItemTenur> _lst = null;
		
		private final int MAX_TOTAL = 20;
		private int mTotal = 0;
		
		public DrscTenure(ArrayList<ItemTenur> src){
			_lst = src;
			mTotal = _lst.size();
		}
		
		@Override
		public Object getFieldValue(JRField arg0) throws JRException {
			
			ItemTenur item = _lst.get(_idx);	
			String name = arg0.getName();
			
			if (name.equals("device_vendor")) {
				
				return item.getDeviceVendor();
				
			}else if (name.equals("device_serial")) {
				
				return item.getDeviceSerial();
				
			}else if (name.equals("device_number")) {
				
				return item.getDeviceNumber();
				
			}else if (name.equals("detect_type")) {
				
				return item.getDetectType();
				
			}else if (name.equals("detect_serial")) {
				
				return item.getDetectSerial();
				
			}else if (name.equals("detect_number")) {
				
				return item.getDetectNumber();
				
			}else if (name.equals("list_footer")) {
				
				if(mTotal>MAX_TOTAL){
					return "共"+mTotal+"部儀器，其餘未列出";
				}else{
					return "總共"+mTotal+"部儀器";
				}			
			}
			return null;
		}
		@Override
		public boolean next() throws JRException {
			if (_lst == null) {
				return false;
			}
			_idx++;
			if (_idx < _lst.size()) {
				return true;
			}
			return false;
		}
	}
}
