package narl.hpclp.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ItemMeeting extends ItemOwner implements Serializable {

	private static final long serialVersionUID = 6586334759632561992L;

	/**
	 * Format is 'yyyy/M/d'.<p>
	 * Making schedule must depend on this data.<p> 
	 */
	private String sday = "";

	/**
	 * keep how many tenures must be analysised.<p> 
	 */
	public ArrayList<ItemTenur> lst = new ArrayList<ItemTenur>();
	
	/**
	 * This presents why today is restday
	 */
	public String memo = "";

	public ItemMeeting(){
		super();
	}
	
	/**
	 * Just copy information, but no tenure list
	 * @param src
	 */
	public ItemMeeting(ItemMeeting src,ItemTenur... lst){
		super();
		this.sday = src.sday;
		for(int i=0; i<lst.length; i++){
			this.lst.add(lst[i]);
		}
		this.memo= src.memo;		
		src.copyTo(this);		
	}
	
	/**
	 * this construct must be a restday
	 * @param day - like 1990/1/1
	 * @param memo - any reason, or the defaul is "假日"
	 * @param stmp - it is same as 'day', byt different type
	 */
	public ItemMeeting(String day,String memo,Date stmp){
		super();
		this.sday = day;
		this.memo= (memo==null)?("假日"):(memo);
		stmp.setTime(stmp.getTime());
		last.setTime(last.getTime());
	}
	
	/*public ItemMeeting(
		String oid,
		String[] info,
		Date stmp
	){
		super(oid,info,stmp);
	}
	
	public ItemMeeting(
		String oid,
		String[] info,
		String day,
		Date stmp		
	){
		super(oid,info,stmp);
		this.day = day;
	}*/
	
	public ItemMeeting(
		String oid,
		String[] info,
		String day,
		Date stmp,
		Date last
	){
		super(oid,info,stmp,last);
		this.sday = day;//it should be same as stamp, but we don't have formater
	}
	
	public void setSDay(String s_txt,Date s_stmp){
		stmp.setTime(s_stmp.getTime());
		sday = s_txt;
	}
	
	public String getSDay(){
		return sday;
	}
	
	public boolean isRestday(){
		if(memo.length()==0){
			return false;
		}
		return true;
	}
	
	public void setRestday(String txt){
		if(txt==null){
			memo = "假日";
		}else{
			memo = txt;
		}
	}	
}
