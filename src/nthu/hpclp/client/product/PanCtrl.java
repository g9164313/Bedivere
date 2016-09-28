package nthu.hpclp.client.product;

import java.util.ArrayList;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import gwt.material.design.client.MaterialDesign;
import gwt.material.design.client.MaterialDesignDebug;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialPushpin;
import gwt.material.design.client.ui.MaterialToast;
import narl.itrc.client.ExComposite;
import nthu.hpclp.client.Main;
import nthu.hpclp.client.setting.BaseParam.TxtCol;
import nthu.hpclp.shared.Const;
import nthu.hpclp.shared.ItemParam;
import nthu.hpclp.shared.ItemProdx;
import nthu.hpclp.shared.ParmEmitter;

public abstract class PanCtrl extends ExComposite {

	public PanCtrl(){
	}

	//@Override
	//public void onEventShow() {
		//At this time, we can prepare enviroment paramters
		//Main.initCombo(cmbFormat, ItemProdx.USED_TXT_FMT);
		//TODO:Main.initCombo(cmbUnitRef, Main.param.prodxUnit);
		//TODO:Main.initCombo(cmbUnitMea, Main.param.prodxUnit);
		//Main.initComboEmitter(cmbEmitter);
		//emitt = new ParmEmitter(cmbEmitter.getSelectedValue());
		//emitt2box();
		//onCreateProdx(null);//create the first item!!!
	//}
	
	@Override
	public void onEventShow() {
		//updateLastProdx();
		//dlgList.openModal();
	}
	
	@Override
	public void onEventHide() {
	}
	
	public abstract void updateBox(ItemProdx itm);
	//--------------------------//
	
    private ArrayList<ItemProdx> lstProdx = new ArrayList<ItemProdx>();
    
    private ListDataProvider<ItemProdx> lstProvider;
    
	private SingleSelectionModel<ItemProdx> lstModel;
	
	protected MaterialModal _dlgList = new MaterialModal();
	
	protected SimplePanel _anchorList1 = new SimplePanel();
	
	protected SimplePanel _anchorList2 = new SimplePanel();
	
	private class ColText extends Column<ItemProdx,String>{
		private int idx = 0;
		public ColText(int i) {
			super(new EditTextCell());
			idx = i;
		}
		@Override
		public String getValue(ItemProdx object) {
			switch(idx){
			case ItemProdx.INFO_PKEY: return object.getKey();
			}
			return "???";
		}
	};
	
    protected void initList(){
    	DataGrid<ItemProdx> grid = new DataGrid<ItemProdx>();
    	grid.setSelectionModel(lstModel);
    	grid.setSize("100%","13em");
    	grid.setEmptyTableWidget(new Label("無資料"));
    	//set all columns~~~
    	grid.addColumn(new ColText(ItemProdx.INFO_PKEY),"報告編號");
    	//set provider~~~
    	lstProvider = new ListDataProvider<ItemProdx>();
    	lstProvider.addDataDisplay(grid);
    	//set grid model~~~
    	lstModel = new SingleSelectionModel<ItemProdx>(lstProvider);
    	lstModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler(){
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				updateBox(lstModel.getSelectedObject());
			}
    	});    	
		//control part
		SimplePager pager = new SimplePager();
		pager.setDisplay(grid);
		pager.setHeight("1em");
		//anchor widget
		_anchorList1.setWidget(grid);
		_anchorList2.setWidget(pager);
    }
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
	//---------------------//
	
    protected void updateLastProdx(){
    	updateList("ORDER BY "+Const.PRODX+".last DESC LIMIT 50");
    }
    
    private void updateList(final String postfix){
    	MaterialLoader.showLoading(true);
    	Main.rpc.listProduct(
        	postfix,
        	new AsyncCallback<ArrayList<ItemProdx>>(){
        	@Override
        	public void onFailure(Throwable caught) {
        		MaterialToast.fireToast("內部錯誤!!");
        	}
        	@Override
        	public void onSuccess(ArrayList<ItemProdx> result) {
        		MaterialLoader.showLoading(false);
        		if(result.isEmpty()==true){
        			MaterialToast.fireToast("無資料!!");
        			return;
        		}
        		updateList(result);
        	}
        });
    }
    
    private void updateList(ArrayList<ItemProdx> lst){
    	lstProdx = lst;
		lstProvider.setList(lstProdx);
    	lstProvider.refresh();
		if(lstProdx.size()>1){
			_dlgList.openModal();
		}
		lstModel.setSelected(lstProdx.get(0),true);
		updateBox(lstModel.getSelectedObject());
    }
}


