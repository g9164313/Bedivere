package nthu.hpclp.server;

import java.util.ArrayList;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import nthu.hpclp.shared.ItemOwner;

public class DSrcOwner implements JRDataSource {
	
	public int idx = -1;
	
	public static ArrayList<ItemOwner> lst = new ArrayList<ItemOwner>();

	public DSrcOwner() {
	}

	@Override
	public Object getFieldValue(JRField arg0) throws JRException {
		
		ItemOwner item = lst.get(idx);
		
		String name = arg0.getName();
		
		if (name.equals("owner_key")) {
			
			return item.getKey();
			
		} else if (name.equals("owner_name")) {
			
			return item.getName();
			
		} else if (name.equals("owner_zipcode")) {
			
			return item.getZip();
			
		} else if (name.equals("owner_name_key")) {
			
			return item.getName() + " （" + item.getKey() + "）";
			
		} else if (name.equals("owner_address")) {
			
			return item.getAddress();
			
		} else if (name.equals("owner_contact")) {
			
			String p1 = item.getPerson() + "   啟";
			String p2 = item.getDepartment();
			if (p2 == null) {
				return p1;
			} else if (p2.length() == 0) {
				return p1;
			}
			return p2 + " " + p1;
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
}
