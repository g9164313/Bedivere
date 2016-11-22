package nthu.hpclp.shared;

import java.io.Serializable;
import java.util.Date;

public class ItemBase implements Serializable {

	private static final long serialVersionUID = 7096858623078746344L;

	/**
	 * When UUID is started with star(*) sign, delete this item!!!<p>
	 * When UUID is a empty string, create a new one and insert it to database.<p>
	 * When UUID is valid, modify record in database.<p>
	 */
	public String uuid = "";
	
	public Date stmp = new Date();
		
	public Date last = new Date();
	
	public String[] info=null,appx=null;

	public String error = "";
	
	public ItemBase(){
	}
	
	public ItemBase(int size){
		prepareInfo(size);
	}
	
	public ItemBase(int size1,int size2){
		prepareInfo(size1);
		prepareAppx(size2);
	}
	
	public void prepareInfo(int size){
		info = new String[size];
		for(int i=0; i<size; i++){
			info[i] = "";
		}
	}
	
	public void prepareAppx(int size){
		appx = new String[size];
		for(int i=0; i<size; i++){
			appx[i] = "";
		}
	}
	
	public void copyTo(ItemBase dst){
		if(dst==null){
			return;
		}
		dst.uuid = this.uuid;
		dst.stmp.setTime(this.stmp.getTime());
		dst.last.setTime(this.last.getTime());
		if(info!=null){
			for(int i=0; i<info.length; i++){
				dst.info[i] = this.info[i];
			}
		}
		if(appx!=null){
			for(int i=0; i<info.length; i++){
				dst.appx[i] = this.appx[i];
			}
		}
	}
	
	public void map(String[] val){
		if(val==null){
			return;
		}
		for(int i=0; i<info.length; i++){
			if(i>=val.length){
				break;
			}
			if(val[i]==null){
				info[i] = "";//some old data still keep the value null :-( 
			}else{
				info[i] = val[i];
			}
		}
	}
	
	public void map(
		String id,
		String[] val
	){
		this.uuid = id;
		map(val);
	}
	
	public void map(
		String id,
		String[] val,
		Date stmp
	){
		this.uuid = id;
		long tick = stmp.getTime();
		this.stmp.setTime(tick);
		this.last.setTime(tick);
		map(val);		
	}
	
	public void map(
		String id,
		String[] val,
		Date stmp,
		Date last
	){
		this.uuid = id;
		this.stmp.setTime(stmp.getTime());
		this.last.setTime(last.getTime());
		map(val);		
	}
	
	public void setInfo(int idx,String val){
		info[idx] = val;
		markModify();
	}
	
	public void setStmp(Date d){
		stmp.setTime(d.getTime());
		markModify();
	}
	
	public void setLast(Date d){
		stmp.setTime(d.getTime());
		markModify();
	}
	
	public void setDate(Date d){
		setStmp(d);
		setLast(d);
	}
	
	static final char TOKEN_DELETE = '*';
	static final char TOKEN_MODIFY = '$';
	
	public ItemBase purge(){
		if(uuid.length()!=0){
			char cc = uuid.charAt(0);
			if(cc==TOKEN_DELETE || cc==TOKEN_MODIFY){
				uuid = uuid.substring(1);
			}
		}
		return this;
	}
	
	public boolean isDelete(){
		if(uuid.length()!=0){
			if(uuid.charAt(0)==TOKEN_DELETE){
				return true;
			}
		}
		return false;
	}
	
	public boolean isModify(){
		if(uuid.length()!=0){
			if(uuid.charAt(0)==TOKEN_MODIFY){
				return true;
			}
		}
		return false;
	}
	
	public boolean isClean(){
		if(uuid.length()==0){
			return false;
		}
		char tkn = uuid.charAt(0);
		if(tkn==TOKEN_DELETE||tkn==TOKEN_MODIFY){
			return false;
		}
		return true;
	}
	
	public ItemBase markNewone(){
		uuid = "";
		return this;
	}
	
	public ItemBase markDelete(){
		return mark_token(TOKEN_DELETE);
	}
	
	public ItemBase markModify(){
		return mark_token(TOKEN_MODIFY);
	}
	
	private ItemBase mark_token(final char tkn){
		if(uuid.length()==0){
			return this;
		}
		if(uuid.charAt(0)==tkn){
			return this;
		}
		uuid = tkn + uuid;
		return this;
	}
}
