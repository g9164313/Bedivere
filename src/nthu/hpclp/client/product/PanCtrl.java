package nthu.hpclp.client.product;

import java.util.ArrayList;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialToast;
import narl.itrc.client.ExComposite;
import nthu.hpclp.client.Main;
import nthu.hpclp.shared.Const;
import nthu.hpclp.shared.ItemProdx;


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
		//listLast50();
	}
	
	@Override
	public void onEventHide() {
	}
	
	public abstract void updateBox(ItemProdx itm);
	//--------------------------//
	
    private ArrayList<ItemProdx> lstProdx = new ArrayList<ItemProdx>();
    
    private ListDataProvider<ItemProdx> lstProvd;
    
	private SingleSelectionModel<ItemProdx> lstModel;
	
	protected MaterialModal _dlgList = new MaterialModal();
	
	protected SimplePanel _anchorList1 = new SimplePanel();
	
	protected SimplePanel _anchorList2 = new SimplePanel();
	
	private class ColText extends Column<ItemProdx,String>{
		private int idx = 0;
		public ColText(int i) {
			super(new TextCell());
			idx = i;
		}
		@Override
		public String getValue(ItemProdx object) {
			switch(idx){
			case -1:
				if(object.uuid.length()==0){
					return "新增";
				}else if(object.uuid.charAt(0)=='$'){
					return "刪除";
				}
				return "＊";
			case ItemProdx.INFO_PKEY: 
				return object.getKey();
			}
			return "???";
		}
	};
	
	private class ColDelete extends Column<ItemProdx,String> implements 
		FieldUpdater<ItemProdx,String>
	{
		private ItemProdx target;
		public ColDelete() {
			super(new ButtonCell());
			setFieldUpdater(this);
		}
		@Override
		public String getValue(ItemProdx object) {
			return "刪除";
		}
		@Override
		public void update(int index, ItemProdx object, String value){
			target = object;
			dlgApprove.appear("確認刪除？",event);//we must get confirm!!!!
		}
		private ClickHandler event = new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				if(target.uuid.length()!=0){
					target.markDelete();
					listUpload();
				}else{
					lstProdx.remove(target);
			    	lstProvd.refresh();
					lstModel.setSelected(lstProdx.get(0),true);
				}
			}
		};
	};
	
    protected void initList(){
    	DataGrid<ItemProdx> grid = new DataGrid<ItemProdx>();    	
    	grid.setSize("100%","13em");
    	grid.setEmptyTableWidget(new Label("無資料"));
    	//set all columns~~~
    	grid.addColumn(new ColText(-1),"狀態");    	
    	grid.addColumn(new ColText(ItemProdx.INFO_PKEY),"報告編號");
    	grid.addColumn(new ColDelete(),"");
    	grid.setColumnWidth(0,"5em");
    	grid.setColumnWidth(2,"7em");
    	//set provider~~~
    	lstProvd = new ListDataProvider<ItemProdx>();
    	lstProvd.addDataDisplay(grid);
    	//set grid model~~~
    	lstModel = new SingleSelectionModel<ItemProdx>(lstProvd);
    	lstModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler(){
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				updateBox(lstModel.getSelectedObject());
			}
    	});
    	grid.setSelectionModel(lstModel);
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
	
	protected void listUpload(){
		if(lstProdx.isEmpty()==true){
    		MaterialToast.fireToast("清單內無報告");
    		return;
    	}
    	MaterialLoader.showLoading(true);
    	Main.rpc.cacheProduct(
    		lstProdx,
    		new AsyncCallback<ArrayList<ItemProdx>>(){
    		@Override
    		public void onFailure(Throwable caught) {
    			MaterialLoader.showLoading(false);
    			MaterialToast.fireToast("內部錯誤");
    		}
    		@Override
    		public void onSuccess(ArrayList<ItemProdx> result) {
    			MaterialLoader.showLoading(false);
    			MaterialToast.fireToast("清單已更新");
    			listRefresh(result);
    		}
    	}); 
	}
	
	protected void listAddItem(){
		ItemProdx itm = new ItemProdx();
		//TODO:curProdx.setEmitter(cmbEmitter.getSelectedValue());
		lstProdx.add(0,itm);
		lstProvd.refresh();
		lstModel.setSelected(lstProdx.get(0),true);
		updateBox(lstModel.getSelectedObject());
		MaterialToast.fireToast("新增報告",100);
	}
	
	protected void listClear(){
		lstProdx = new ArrayList<ItemProdx>();
		lstProvd.setList(lstProdx);
    	lstProvd.refresh();    	
    	updateBox(null);
	}
	
    protected void listLast50(){
    	listQuery("ORDER BY "+Const.PRODX+".last DESC LIMIT 50");
    }
    
    private void listQuery(final String postfix){
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
        		listRefresh(result);
        	}
        });
    }

    private void listRefresh(ArrayList<ItemProdx> lst){
    	lstProdx = lst;
		lstProvd.setList(lstProdx);
    	lstProvd.refresh();
		if(lstProdx.size()>1){
			_dlgList.openModal();
		}
		lstModel.setSelected(lstProdx.get(0),true);
		updateBox(lstModel.getSelectedObject());
    }
}


