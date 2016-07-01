package narl.hpclp.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ItemMeeting extends ItemOwner implements Serializable {

	private static final long serialVersionUID = 6586334759632561992L;

	public ArrayList<ItemTenur> lst = new ArrayList<ItemTenur>();
	
	public String day = "";
	
	/**
	 * This presents why today is restday
	 */
	public String memo = "";

	public ItemMeeting(){
		super();
	}
	
	/**
	 * this construct must be a restday
	 * @param day - like 1990/1/1
	 * @param memo - any reason, or the defaul is "假日"
	 * @param stmp - it is same as 'day', byt different type
	 */
	public ItemMeeting(String day,String memo,Date stmp){
		super();
		this.day = day;
		this.memo= (memo==null)?("假日"):(memo);
		stmp.setTime(stmp.getTime());
		last.setTime(last.getTime());
	}
	
	public ItemMeeting(
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
