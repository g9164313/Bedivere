package narl.hpclp.server;

import java.util.ArrayList;

import narl.hpclp.shared.ItemMeeting;
import narl.hpclp.shared.ItemTenur;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class DSrcNotify implements JRDataSource {
	
	private int idx = -1;

	public static ArrayList<ItemMeeting> lst = new ArrayList<ItemMeeting>();
	
	public DSrcNotify(){
	}
	
	@Override
	public Object getFieldValue(
		JRField arg0
	) throws JRException {

		ItemMeeting meet = lst.get(idx);
		
		String txt, name = arg0.getName();
		
		System.out.println("field="+name);
		
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
			
			txt =  meet.getAddress();
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
		
			return RpcBridge.date2tw_d(meet.stmp);
			
		}else if(name.equals("meet_hour")){	
			
			return RpcBridge.fmtTime.format(meet.stmp);
			
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
		
		public DrscTenure(ArrayList<ItemTenur> src){
			_lst = src;
		}
		
		@Override
		public Object getFieldValue(JRField arg0) throws JRException {
			
			ItemTenur item = _lst.get(_idx);	
			String name = arg0.getName();
			
			if (name.equals("tenure_vendor")) {	
				
				return item.getDeviceVendor();	
				
			}else if (name.equals("tenure_serial")) {
				
				return item.getDeviceSerial();
				
			}else if (name.equals("tenure_number")) {
				
				return item.getDeviceNumber();
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
