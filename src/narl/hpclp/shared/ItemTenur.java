package narl.hpclp.shared;

import java.io.Serializable;
import java.util.Date;

public class ItemTenur extends ItemBase implements Serializable {

	private static final long serialVersionUID = -3588414162261404996L;

	public static final int INFO_TKEY = 0;	
	public static final int INFO_DEV_VENDOR = 1;
	public static final int INFO_DEV_SERIAL = 2;
	public static final int INFO_DEV_NUMBER = 3;
	public static final int INFO_DET_TYPE = 4;
	public static final int INFO_DET_SERIAL = 5;
	public static final int INFO_DET_NUMBER = 6;
	public static final int INFO_DET_AREA = 7;	
	public static final int INFO_FACTOR = 8;
	public static final int INFO_STEER = 9;	
	public static final int INFO_MEMO = 10;
	public static final int INFO_MAX_COL = 11;
	
	public Date meet = new Date();
	
	public ItemBase owner = null;
	
	public ItemTenur(){
		super(INFO_MAX_COL);
	}

	public ItemTenur(
		String id,
		String[] info,
		Date stmp,
		Date last
	){
		super(INFO_MAX_COL);
		map(id,info,stmp,last);
		this.meet.setTime(stmp.getTime());
	}
	
	public ItemTenur(
		String id,
		String[] info,
		Date stmp,
		Date last,
		Date meet
	){
		super(INFO_MAX_COL);
		map(id,info,stmp,last);
		this.meet.setTime(meet.getTime());
	}
}
