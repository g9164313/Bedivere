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
	public String comment = "";

	public ItemMeeting(){
		super();
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
		if(comment.length()==0){
			return false;
		}
		return true;
	}
	
	public void setRestday(String txt){
		if(txt==null){
			comment = "假日";
		}else{
			comment = txt;
		}
	}
}
