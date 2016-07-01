package narl.hpclp.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ItmMeeting extends ItmBase implements Serializable {

	public ArrayList<ItmTenur> lst = new ArrayList<ItmTenur>();
	
	public String day = "";
	
	public ItmMeeting(){
		super(ItmOwner.INFO_MAX_COL);
	}
	
	public ItmMeeting(
		String oid,
		String[] info,
		Date stmp
	){
		super(ItmOwner.INFO_MAX_COL);
		map(oid,info,stmp);
	}
	
	public String getKey(){
		return inf[ItmOwner.INFO_OKEY];
	}
}
