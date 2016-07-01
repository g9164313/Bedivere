package narl.hpclp.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ItemMeeting extends ItemOwner implements Serializable {

	private static final long serialVersionUID = 6586334759632561992L;

	public ArrayList<ItemTenur> lst = new ArrayList<ItemTenur>();
	
	public String day = "";
	
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
}
