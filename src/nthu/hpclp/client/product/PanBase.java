package nthu.hpclp.client.product;

import java.util.ArrayList;

import narl.itrc.client.ExComposite;
import nthu.hpclp.shared.Const;
import nthu.hpclp.shared.ItemProdx;
import nthu.hpclp.shared.ParmEmitter;

public class PanBase extends ExComposite {

	public PanBase(){		
	}
		
	@Override
	public void onEventShow() {
		//At this time, we can prepare enviroment paramters
		//Main.initCombo(cmbFormat, ItemProdx.USED_TXT_FMT);
		//TODO:Main.initCombo(cmbUnitRef, Main.param.prodxUnit);
		//TODO:Main.initCombo(cmbUnitMea, Main.param.prodxUnit);
		//Main.initComboEmitter(cmbEmitter);
		//emitt = new ParmEmitter(cmbEmitter.getSelectedValue());
		//emitt2box();
		//onCreateProdx(null);//create the first item!!!
	}

	@Override
	public void onEventHide() {
	}
	//--------------------------//
	
    /**
     * this variable keeps the user data in local computer~~~
     */
    protected ArrayList<ItemProdx> lstProdx = new ArrayList<ItemProdx>();
    
    protected ItemProdx curProdx = null;
    
    protected ParmEmitter emitt = null;
    
    
    
	//--------------------------//
	
	protected String query(String txt){
		//identify whether this is 2D-label
		if(isQRCode(txt)==true){
			String[] key = qrcode[1].split("_");			
			return "WHERE "+
			    Const.TENUR+".info[1] SIMILAR TO '%"+key[0]+"%"+key[1]+"%' OR "+
			    Const.TENUR+".info[1] SIMILAR TO '%"+key[1]+"%"+key[0]+"%' "+
			    "ORDER BY "+Const.PRODX+".last DESC LIMIT 1";
		}		
		//No, it is just plain-text
		return "WHERE "+
	    	Const.PRODX+".info[1] SIMILAR TO '%"+txt+"%' OR "+
	    	Const.PRODX+".info[8] SIMILAR TO '%"+txt+"%' OR "+
	    	Const.OWNER+".info[2] SIMILAR TO '%"+txt+"%' OR "+
	    	Const.OWNER+".info[6] SIMILAR TO '%"+txt+"%' OR "+
	    	Const.TENUR+".info[1] SIMILAR TO '%"+txt+"%' "+
	    	"ORDER BY "+Const.PRODX+".last DESC";
	}
    
	/**
	 * This variable keep the QRcode data.<p>
	 * date @ tenure-key @ operator @ memo
	 */
	protected String[] qrcode={"","","","",""};
	
	private boolean isQRCode(String txt){
		for(int i=0; i<qrcode.length; i++){
			qrcode[i] = "";//clear old data
		}
		//first column - date
		int pos = txt.indexOf('@');
		if(pos<0){
			return false;
		}
		qrcode[0] = txt.substring(0,pos);
		
		//second column - tenure-key
		txt = txt.substring(pos+1);
		pos = txt.indexOf('@');
		if(pos<0){
			return false;
		}
		qrcode[1] = txt.substring(0,pos);
		
		//third and forth column - operator and memo
		txt = txt.substring(pos+1);
		pos = txt.indexOf('@');
		if(pos>=0){
			qrcode[2] = txt.substring(0,pos);
			txt = txt.substring(pos+1);
			qrcode[3] = txt;
		}else{
			qrcode[2] = txt;
			qrcode[3] = "";
		}
		return true;
	}
}
