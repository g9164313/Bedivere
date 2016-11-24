package nthu.hpclp.client.itembase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialNavBar;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSearch;
import nthu.hpclp.shared.Const;
import nthu.hpclp.shared.ItemOwner;
import nthu.hpclp.client.Main;

public class PanItemOwner extends PanItemBase<ItemOwner> {

	private static PanItemOwnerUiBinder uiBinder = GWT.create(PanItemOwnerUiBinder.class);

	interface PanItemOwnerUiBinder extends UiBinder<Widget, PanItemOwner> {
	}

	public PanItemOwner() {
		initWidget(uiBinder.createAndBindUi(this));
		anchorGrid.add(grid);
		root.add(Main.dlgModifyOwner);
		root.add(dlgApprove);
	}

	@UiField(provided=true)
	MaterialPanel root = _root;
	
	@UiField(provided=true)
	MaterialLink lnkStartSearch = _lnk_start_search;
    @UiField(provided=true)
    MaterialNavBar navAppBar = _nav_appbar;
    @UiField(provided=true)
    MaterialNavBar navSearch = _nav_search;
    @UiField(provided=true)
    MaterialSearch boxSearch = _box_search;
    
    @UiField(provided=true)
    MaterialLink lnkPageHint = _page_hint;
    
    @UiField
	SimplePanel anchorGrid;
	//--------------------------------//
    
    @UiHandler("lnkPageNext")
    void onPageNext(ClickEvent e) {
    	pageNext();
    }
    
    @UiHandler("lnkPagePrev")
    void onPagePrevious(ClickEvent e) {
    	pagePrev();
    }
    
    @UiHandler("lnkCreate")
    void onItemCreate(ClickEvent e) {
    	ItemOwner itm = getFirstItem();
    	if(itm==null){
    		return;
    	}
    	//just copy a new one and refresh data-grid again, trick!!!
    	itm.markNewone();
    	Main.dlgModifyOwner.appear(itm,null,eventPageUpdate);
    }
    
    @UiHandler("lnkModify")
    void onItemModify(ClickEvent e) {
    	ItemOwner itm = getFirstItem();
    	if(itm==null){
    		return;
    	}
    	itm.markModify();
    	Main.dlgModifyOwner.appear(itm,null,eventPageUpdate);
    }
    
    @UiHandler("lnkDelete")
    void onItemDelete(ClickEvent e) {
    	ItemOwner itm = getFirstItem();
    	if(itm==null){
    		return;
    	}    	
    	itm.markDelete();//how to delete multiple-items???
    	Main.dlgModifyOwner.appear(itm,null,eventPageUpdate);
    }
    //--------------------------------//
    
	@Override
	public void onSearching(String txt) {
		resetOnePage();//just one page~~~
		getDatum(0,PAGE_COUNT,txt);
	}

	@Override
	public void getDatum(int offset, int limit,String postfix) {
		if(postfix.length()==0){
			Main.rpc.listOwnerByRow(offset,limit,rpcPageUpdate);
		}else{
			postfix = "WHERE "+
				Const.OWNER+".info[1] SIMILAR TO '%"+postfix+"%' OR "+
				Const.OWNER+".info[2] SIMILAR TO '%"+postfix+"%' OR "+
				Const.OWNER+".info[4] SIMILAR TO '%"+postfix+"%' OR "+
				Const.OWNER+".info[5] SIMILAR TO '%"+postfix+"%' OR "+
				Const.OWNER+".info[6] SIMILAR TO '%"+postfix+"%' OR "+
				Const.OWNER+".info[7] SIMILAR TO '%"+postfix+"%' OR "+
				Const.OWNER+".info[8] SIMILAR TO '%"+postfix+"%' "+
				"ORDER BY "+Const.OWNER+".info[1]";
			Main.rpc.listOwner(postfix,rpcPageUpdate);
		}
	}

	@Override
	public void prepare_column(DataGrid<ItemOwner> grid) {
		
		grid.addColumn(new ColInfo(ItemOwner.INFO_OKEY),"代號");  // - 0 
		grid.addColumn(new ColInfo(ItemOwner.INFO_NAME),"名稱");  // - 1 
		grid.addColumn(new ColInfo(ItemOwner.INFO_DEPT),"部門");  // - 2   
		grid.addColumn(new ColInfo(ItemOwner.INFO_PRSN),"聯絡人");// - 3 
		grid.addColumn(new ColInfo(ItemOwner.INFO_ZIP) ,"區號");// - 4
		grid.addColumn(new ColInfo(ItemOwner.INFO_ADDR),"地址");// - 5 
		grid.addColumn(new ColInfo(ItemOwner.INFO_PHONE),"聯絡方式-1");// - 6  
		grid.addColumn(new ColInfo(ItemOwner.INFO_EMAIL),"聯絡方式-2");// - 7 
		grid.addColumn(new ColInfo(ItemOwner.INFO_MEMO),"備註");// - 8

		grid.setColumnWidth(0,"5em");
		//grid.setColumnWidth(1,"12em");
		grid.setColumnWidth(2,"11em");
		grid.setColumnWidth(3,"6em");
		grid.setColumnWidth(4,"5em");
		grid.setColumnWidth(6,"11em");
		grid.setColumnWidth(7,"11em");
		grid.setColumnWidth(8,"9em");
	}
}


