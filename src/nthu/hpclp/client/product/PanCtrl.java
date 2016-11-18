package nthu.hpclp.client.product;

import java.util.ArrayList;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Window;
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
import nthu.hpclp.shared.ItemOwner;
import nthu.hpclp.shared.ItemProdx;
import nthu.hpclp.shared.ItemTenur;

public abstract class PanCtrl extends ExComposite {

	protected boolean isOnList = false;
	
	public PanCtrl(){
		_dlgList.addCloseHandler(new CloseHandler<MaterialModal>(){
			@Override
			public void onClose(CloseEvent<MaterialModal> event) {
				isOnList = false;
			}
		});
	}
	
	protected PartOwner owner = new PartOwner();
	protected PartTenur tenur = new PartTenur();
	protected PartInfo info = new PartInfo();
	protected PartAppx appx = new PartAppx();
	protected PartScriber scriber = new PartScriber();
	protected DlgGenReport genTenuReport = new DlgGenReport();

	@Override
	public void onEventHide() {
	}
	
	public abstract void updateBox(ItemProdx itm);
	//--------------------------//
	
    private ArrayList<ItemProdx> lstProdx = new ArrayList<ItemProdx>();
    
    protected ListDataProvider<ItemProdx> lstProvd;
    
	protected SingleSelectionModel<ItemProdx> lstModel;
	
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
		public String getValue(ItemProdx obj) {
			switch(idx){
			case -1:
				if(obj.uuid.length()==0){
					return "新增";
				}else if(obj.isDelete()==true){
					return "刪除";
				}else if(obj.isModify()==true){
					return "修改";
				}
				return "---";
			case 0x000+ItemProdx.INFO_PKEY:
				return obj.getKey();
			case 0x100+ItemOwner.INFO_OKEY:
				return obj.getOwner().getKey();
			case 0x100+ItemOwner.INFO_NAME:
				return obj.getOwner().getName();
			case 0x200+ItemTenur.INFO_DEV_VENDOR:
				return obj.getTenur().getDeviceVendor();
			case 0x200+ItemTenur.INFO_DEV_SERIAL:
				return obj.getTenur().getDeviceSerial();
			case 0x200+ItemTenur.INFO_DEV_NUMBER:
				return obj.getTenur().getDeviceNumber();
			}
			return "？？？";
		}
	};

    protected void initList(){
    	DataGrid<ItemProdx> grid = new DataGrid<ItemProdx>();    	
    	grid.setSize("100%","13em");
    	grid.setEmptyTableWidget(new Label("無資料"));
    	//set all columns~~~
    	grid.addColumn(new ColText(-1),"狀態");    	
    	grid.addColumn(new ColText(0x000+ItemProdx.INFO_PKEY),"報告編號");
    	grid.addColumn(new ColText(0x100+ItemOwner.INFO_OKEY),"委託代號");
    	grid.addColumn(new ColText(0x100+ItemOwner.INFO_NAME),"委託廠商");
    	grid.addColumn(new ColText(0x200+ItemTenur.INFO_DEV_VENDOR),"廠商");
    	grid.addColumn(new ColText(0x200+ItemTenur.INFO_DEV_SERIAL),"型號");
    	grid.addColumn(new ColText(0x200+ItemTenur.INFO_DEV_NUMBER),"序號");
    	//grid.addColumn(new ColDelete(),"");
    	grid.setColumnWidth(0,"5em");
    	grid.setColumnWidth(1,"7em");
    	grid.setColumnWidth(2,"7em");
    	grid.setColumnWidth(4,"10em");
    	grid.setColumnWidth(5,"13em");
    	grid.setColumnWidth(6,"13em");
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
    
	protected void listUploadItem(){
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
    			MaterialToast.fireToast("更新資料庫");
    			listRefresh(result);
    		}
    	}); 
	}
	
	protected void listAddItem(){
		ItemProdx itm = new ItemProdx();
		itm.setUnitRef(info.cmbUnitRef.getSelectedValue());
		itm.setUnitMea(info.cmbUnitMea.getSelectedValue());
		itm.setEmitter(PartInfo.DEFAT_GAMMA_EMITTER);
		lstProdx.add(0,itm);
		updateBox(itm);
		listRefresh(lstProdx);
		MaterialToast.fireToast("新增報告",500);
	}
	
	protected void ListDeleteItem(){
		final ItemProdx itm = lstModel.getSelectedObject();
		if(itm==null){
			MaterialToast.fireToast("請選擇目標",500);
			return;
		}
		final ClickHandler eventDelete = new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				if(itm.uuid.length()!=0){
					itm.markDelete();
					listUploadItem();
				}else{
					lstProdx.remove(itm);
					listRefresh(lstProdx);
				}				
			}
		};
		dlgApprove.appear("確認刪除？",eventDelete);//we must get confirm!!!!
	}
	
	protected void listClearItem(){
		final ClickHandler eventCheck = new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				lstProdx.clear();
				lstProvd.setList(lstProdx);
		    	lstProvd.refresh();    	
		    	updateBox(null);
			}
		};
    	boolean flagClean = true;
    	for(ItemProdx itm:lstProdx){
    		if(itm.isClean()==false){
    			flagClean = false;
    			break;
    		}
    	}
    	if(flagClean==false){
        	dlgApprove.appear(
            	"有未上傳的項目，確認清除",
            	eventCheck
            );
    	}else{
    		eventCheck.onClick(null);//dummy event!!!
    	}
	}
	
    protected void listShowLast50(){
    	listQuery("ORDER BY "+Const.PRODX+".last DESC LIMIT 50");
    }

    private void listRefresh(ArrayList<ItemProdx> lst){
    	lstProdx = lst;
		lstProvd.setList(lstProdx);
    	lstProvd.refresh();
		lstModel.setSelected(lstProdx.get(0),true);
		updateBox(lstModel.getSelectedObject());
    }
    
    protected void listNextItem(int sign){
    	ItemProdx itm = lstModel.getSelectedObject();
    	int idx = lstProdx.indexOf(itm);
    	idx = idx + sign;
    	int cnt = lstProdx.size();
    	if(idx<0){
    		idx = 0;
    	}else if(cnt<=idx){
    		idx = cnt - 1;
    	}
    	lstModel.setSelected(lstProdx.get(idx),true);
    }
    //---------------------//  

	protected String txt2sql(String txt){
		txt = txt.replace("委託","owner")
			.replace("儀器","tenure")
			.replace("編號","key")
			.replace("：",":")
			.trim();
		
		final String TAG_OWNER = "owner:";
		final String TAG_TENUR = "tenure:";
		final String TAG_PKEY = "key:";
		
		if(isQRCode(txt)==true){
			//identify whether this is 2D-label
			String[] key = qrcode[1].split("_");			
			return "WHERE "+
			    Const.TENUR+".info[1] SIMILAR TO '%"+key[0]+"%"+key[1]+"%' OR "+
			    Const.TENUR+".info[1] SIMILAR TO '%"+key[1]+"%"+key[0]+"%' "+
			    "ORDER BY "+Const.PRODX+".last DESC LIMIT 1";
		}else if(txt.startsWith(TAG_OWNER)==true){
			txt = txt.substring(TAG_OWNER.length());
			return "WHERE "+
				Const.OWNER+".info[1] SIMILAR TO '%"+txt+"%' OR "+
				Const.OWNER+".info[2] SIMILAR TO '%"+txt+"%' OR "+
				Const.OWNER+".info[4] SIMILAR TO '%"+txt+"%' OR "+
				Const.OWNER+".info[5] SIMILAR TO '%"+txt+"%' OR "+
				Const.OWNER+".info[6] SIMILAR TO '%"+txt+"%' OR "+
				Const.OWNER+".info[7] SIMILAR TO '%"+txt+"%' OR "+
				Const.OWNER+".info[8] SIMILAR TO '%"+txt+"%' "+
				"ORDER BY "+Const.TENUR+".info[1] ASC,"+Const.PRODX+".last DESC";
		}else if(txt.startsWith(TAG_TENUR)==true){
			txt = txt.substring(TAG_TENUR.length()).toLowerCase();
			return "WHERE "+
				"lower("+Const.TENUR+".info[2]) SIMILAR TO '%"+txt+"%' OR "+
				"lower("+Const.TENUR+".info[3]) SIMILAR TO '%"+txt+"%' OR "+
				"lower("+Const.TENUR+".info[4]) SIMILAR TO '%"+txt+"%' OR "+
				Const.TENUR+".info[5] SIMILAR TO '"+txt+"' OR "+
				"lower("+Const.TENUR+".info[6]) SIMILAR TO '%"+txt+"%' OR "+
				"lower("+Const.TENUR+".info[7]) SIMILAR TO '%"+txt+"%' OR "+
				Const.TENUR+".info[1] SIMILAR TO '%"+txt+"%' "+
				"ORDER BY "+Const.OWNER+".info[2] ASC,"+Const.PRODX+".last DESC";
		}else if(txt.startsWith(TAG_PKEY)==true){
			txt = txt.substring(TAG_PKEY.length());
			return "WHERE "+
				Const.PRODX+".info[1] SIMILAR TO '%"+txt+"%' "+
				"ORDER BY "+Const.PRODX+".last DESC,"+Const.OWNER+".info[2] ASC,"+Const.TENUR+".info[1] ASC";
		}
		//No, it is just plain-text
		return "WHERE "+
	    	Const.PRODX+".info[1] SIMILAR TO '%"+txt+"%' OR "+
	    	Const.PRODX+".info[8] SIMILAR TO '%"+txt+"%' OR "+
	    	Const.OWNER+".info[2] SIMILAR TO '%"+txt+"%' OR "+
	    	Const.OWNER+".info[6] SIMILAR TO '%"+txt+"%' OR "+
	    	Const.TENUR+".info[1] SIMILAR TO '%"+txt.toUpperCase()+"%' "+
	    	"ORDER BY "+Const.PRODX+".last DESC,"+Const.OWNER+".info[2] ASC,"+Const.TENUR+".info[1] ASC";
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
	
    protected void listQuery(final String postfix){
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
        			//how to clear box???
        			return;
        		}
        		listRefresh(result);
        	}
        });
    }
	//---------------------//
	
    private String validChar(String src){
    	String dst = "";
    	char[] cc = src.toCharArray();
    	for(int i=0; i<cc.length; i++){
			if(cc[i]>'\200'){
				continue;//locale word
			}
    		dst = dst + cc[i];
    	}
    	return dst;
    }
    
    protected void print2DTag(
    	ItemTenur item,
    	String date,
    	String name,
    	String memo
    ){
    	String[] info = new String[6];
    	info[0] = date;//測試日期
    	info[1] = item.getKey();//代號(t_key)
    	info[2] = validChar(item.getDeviceSerial());//型號
    	info[3] = validChar(item.getDeviceNumber());//序號
    	info[4] = name;//操作員
    	info[5] = memo;//註解
    	Main.rpc.printTag(info,new AsyncCallback<String>(){
			@Override
			public void onFailure(Throwable caught) {
				MaterialToast.fireToast("內部錯誤!!");
			}
			@Override
			public void onSuccess(String result) {
				if(result.length()==0){					
					return;
				}
				MaterialToast.fireToast(result);
			} 
    	});
    }
    
    protected void printProduct(){
    	if(lstProdx.isEmpty()==true){
    		MaterialToast.fireToast("無報告");
    		return;
    	}
    	ItemProdx target = lstModel.getSelectedObject();
    	if(target==null){
    		MaterialToast.fireToast("無報告");
    		return;
    	}
    	ArrayList<ItemProdx> lstTarget = new ArrayList<ItemProdx>();
    	lstTarget.add(target);
    	Main.rpc.cacheProduct(lstTarget, new AsyncCallback<ArrayList<ItemProdx>>(){
			@Override
			public void onFailure(Throwable caught) {
				MaterialToast.fireToast("內部錯誤");
			}
			@Override
			public void onSuccess(ArrayList<ItemProdx> result) {				
		    	Window.open(
		    		GWT.getHostPageBaseURL()+Const.REPORT_PRODUCT,
		    		"_blank",""
		    	);
			}
		});
    }
}


