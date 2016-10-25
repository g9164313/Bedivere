package nthu.hpclp.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ItemAccnt extends ItemBase implements Serializable {

	private static final long serialVersionUID = 670273031513225578L;

	//the entry format~~
	public static final int INFO_AKEY = 0;		
	public static final int INFO_RECEIPT_NUM = 1;
	public static final int INFO_RECEIPT_DAY = 2;
	public static final int INFO_MEMO = 3;	
	public static final int INFO_MAX_COL = 4;
	
	public boolean isFinal = false;
	
	private ItemOwner owner = null;
	
	/**
	 * This record an account item, there are three columns for one string.<p>
	 * They presents 'title','price' and 'count'.<p>
	 */
	public ArrayList<String> fare = new ArrayList<String>();
	
	public ItemAccnt() {
		super(INFO_MAX_COL);
	}
	
	public ItemAccnt(
		String oid,
		String[] info,
		Date stmp,
		Date last
	){
		super(INFO_MAX_COL);
		map(oid,info,stmp,last);
	}
	
	public ItemAccnt(
		String oid,
		String[] info
	){
		super(INFO_MAX_COL);
		map(oid,info);
	}
	
	public ItemOwner getOwner(){
		return owner;
	}
	public void setOwner(ItemOwner itm){
		owner = itm;
	}
	
	public String getKey(){
		return info[INFO_AKEY];
	}	
	public void setKey(String val){
		info[INFO_AKEY]=val;
	}
		
	public String getReceiptNum(){
		return info[INFO_RECEIPT_NUM];
	}	
	public void setReceiptNum(String val){
		info[INFO_RECEIPT_NUM]=val;
		if(val.length()==0){
			isFinal = false;
		}else{
			isFinal = true;
		}		
	}
		
	public String getReceiptDay(){
		return info[INFO_RECEIPT_DAY];
	}	
	public void setReceiptDay(String val){
		info[INFO_RECEIPT_DAY]=val;
		if(val.length()==0){
			isFinal = false;			
		}else{
			isFinal = true;
		}
		//finally, change the last day!!!!		
		/*try{
		 * Why do we change this ???
			last.setTime(Main.fmtDay1.parse(val).getTime()+(1000L*3600L)*12);
		}catch(IllegalArgumentException e){
			return;
		}*/		
	}
		
	public String getMemo(){
		return info[INFO_MEMO];
	}	
	public void setMemo(String val){
		info[INFO_MEMO]=val;
	}
		
	public int getFare(int idx){
		if(idx>fare.size()){
			return 0;
		}
		return 0;
	}
	
	public String getFareTitle(int idx){
		return get_fare(idx,0);
	}
	
	public String getFarePrice(int idx){
		int val=0;
		try{
			val = Integer.valueOf(get_fare(idx,1));
		}catch(NumberFormatException e){
			return "";
		}
		return Const.int2note(val);
	}
	
	public String getFareCount(int idx){
		int cnt=0;
		try{
			cnt = Integer.valueOf(get_fare(idx,2));
		}catch(NumberFormatException e){
			return "";
		}
		return Const.int2note(cnt);
	}
	
	public String getFareMemo(int idx){
		return get_fare(idx,3);
	}
	
	public String getFareCost(int idx){
		int val=0,cnt=0;
		try{
			val = Integer.valueOf(get_fare(idx,1));
			cnt = Integer.valueOf(get_fare(idx,2));
		}catch(NumberFormatException e){
			return "";
		}
		return Const.int2note(val*cnt);
	}
	
	private String get_fare(int row,int col){
		if(row>=fare.size()){
			return "";
		}
		String[] arg = fare.get(row).split("@");
		if(col>=arg.length){
			return "";
		}
		return arg[col];
	}
	
	public void addFare(String title,int price,int count){
		fare.add(title+"@"+price+"@"+count);
	}
	
	public void delFare(String title){
		for(String txt:fare){
			if(txt.startsWith(title)==true){
				fare.remove(txt);
				return;
			}
		}		
	}

	public int getTotal(){
		int total = 0;
		for(int i=0; i<fare.size(); i++){
			String service = (String)fare.get(i);
			String[] item = service.split("@");
			int price = 0;
			int count = 1;
			try{
				if(item.length>=2){			
					price = Integer.valueOf(item[1]);
				}
				if(item.length>=3){
					count = Integer.valueOf(item[2]);
				}
			}catch(NumberFormatException e){
				System.err.println("[ERROR]: wrong format!! --> "+service);
			}
			total = total + price*count;
		}
		return total;
	}
}
