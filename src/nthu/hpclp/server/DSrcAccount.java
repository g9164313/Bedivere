package nthu.hpclp.server;

import java.util.ArrayList;
import java.util.Date;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import nthu.hpclp.shared.Const;
import nthu.hpclp.shared.ItemAccnt;

public class DSrcAccount implements JRDataSource {
	
	private int idx = -1;
	
	public static ArrayList<ItemAccnt> lst = new ArrayList<ItemAccnt>();

	public DSrcAccount(){
	}

	@Override
	public Object getFieldValue(JRField arg0) throws JRException {			
		
		ItemAccnt item = lst.get(idx);
		
		String name = arg0.getName();
		
		if (name.equals("owner_key")) {
			if(item.owner==null){ return "無此資訊"; }
			return item.owner.getKey();
			
		}else if (name.equals("owner_name")) {
			if(item.owner==null){ return "無此資訊"; }
			return item.owner.getName();
			
		}else if (name.equals("owner_name_key")) {
			if(item.owner==null){ return "無此資訊"; }
			return item.owner.getName()+" （"+item.owner.getKey()+"）";
			
		}else if (name.equals("owner_zipcode")) {
			if(item.owner==null){ return "無此資訊"; }
			return item.owner.getZip();
			
		}else if (name.equals("owner_address")) {
			if(item.owner==null){ return "無此資訊"; }
			return item.owner.getAddress();
			
		}else if (name.equals("owner_full_address")) {
			if(item.owner==null){ return "無此資訊"; }
			return item.owner.getZip()+" "+item.owner.getAddress();
			
		}else if (name.equals("owner_contact")) {
			if(item.owner==null){ return "無此資訊"; }			
			String dep = item.owner.getDepartment();
			String tie = item.owner.getPerson()+"   啟";
			if(dep.length()==0){
				return tie;
			}
			return dep+" "+tie;
			
		}else if (name.equals("owner_contact1")) {
			if(item.owner==null){ return "無此資訊"; }
			String dep = item.owner.getDepartment();
			String tie = item.owner.getPerson();
			if(dep.length()==0){
				return tie;
			}
			return dep+" "+tie;
			
		}else if (name.equals("account_key")) {
			return item.getKey();
			
		}else if (name.equals("account_stamp")) {
			return UtilsMisc.date2tw_ch(item.stmp);
			
		}else if (name.equals("deadline_stamp")) {	
			//used for demand report~~
			Date today = new Date();
			UtilsCalendar.addDaysToDate(today,15);
			return UtilsMisc.date2tw_ch(today);
			
		}else if (name.equals("account_season")) {
			return UtilsMisc.date2tw_ch(item.stmp);

		}else if (name.equals("fare_set")) {
			return new DsrcFare(item);
			
		}else if (name.equals("fare_total")) {
			//used for demand report~~
			return Const.int2note(item.getTotal());
			
		}else if (name.equals("fare_set1_title")) {				
			return item.getFareTitle(0);
		}else if (name.equals("fare_set1_total")) {				
			return item.getFareCost(0);
		}else if (name.equals("fare_set1_num")) {				
			return item.getFareCount(0);
			
		}else if (name.equals("fare_set2_title")) {				
			return item.getFareTitle(1);
		}else if (name.equals("fare_set2_total")) {				
			return item.getFareCost(1);
		}else if (name.equals("fare_set2_num")) {				
			return item.getFareCount(1);
			
		}else if (name.equals("fare_set3_title")) {				
			return item.getFareTitle(2);
		}else if (name.equals("fare_set3_total")) {				
			return item.getFareCost(2);
		}else if (name.equals("fare_set3_num")) {				
			return item.getFareCount(2);
			
		}else if (name.equals("fare_set4_title")) {				
			return item.getFareTitle(3);
		}else if (name.equals("fare_set4_total")) {				
			return item.getFareCost(3);
		}else if (name.equals("fare_set4_num")) {				
			return item.getFareCount(3);
			
		}else if (name.equals("fare_set5_title")) {				
			return item.getFareTitle(4);
		}else if (name.equals("fare_set5_total")) {				
			return item.getFareCost(4);
		}else if (name.equals("fare_set5_num")) {				
			return item.getFareCount(4);
			
		}else if (name.equals("fare_set6_title")) {				
			return item.getFareTitle(5);
		}else if (name.equals("fare_set6_total")) {				
			return item.getFareCost(5);
		}else if (name.equals("fare_set6_num")) {				
			return item.getFareCount(5);		
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
	
	class DsrcFare implements JRDataSource {
		
		private int _idx = -1;		
		private ItemAccnt _itm;
		
		public DsrcFare(ItemAccnt item){
			_itm = item;
		}
		
		@Override
		public Object getFieldValue(JRField arg0) throws JRException {
			
			String name = arg0.getName();
	
			if( name.equals("fare_name") ){
				
				return _itm.getFareTitle(_idx);
				
			} else if (name.equals("fare_fee")) {
				
				return _itm.getFarePrice(_idx);
				
			} else if (name.equals("fare_count")) {
				
				return _itm.getFareCount(_idx);
				
			} else if (name.equals("fare_cost")) {
			
				return _itm.getFareCost(_idx);
				
			} else if (name.equals("fare_memo")) {
							
				return _itm.getFareMemo(_idx);
				
			} else if (name.equals("fare_total")) {
				
				return Const.int2note(_itm.getTotal());
			}
			return null;
		}

		@Override
		public boolean next() throws JRException {
			_idx++;
			if(_idx<_itm.fare.size()){			
				return true;
			}
			return false;
		}
	}
}
