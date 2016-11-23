package nthu.hpclp.client.itembase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialNavBar;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSearch;
import nthu.hpclp.client.Main;
import nthu.hpclp.shared.ItemOwner;
import nthu.hpclp.shared.ItemTenur;

public class PanItemTenur extends PanItemBase<ItemTenur>{

	private static PanItemTenurUiBinder uiBinder = GWT.create(PanItemTenurUiBinder.class);

	interface PanItemTenurUiBinder extends UiBinder<Widget, PanItemTenur> {
	}

	public PanItemTenur() {
		initWidget(uiBinder.createAndBindUi(this));
		anchorGrid.add(grid);		
		root.add(Main.dlgModifyTenur);
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
    	ItemTenur itm = getFirstItem();
    	if(itm==null){
    		return;
    	}
    	//just copy a new one and refresh data-grid again, trick!!!
    	itm.markNewone();
    	Main.dlgModifyTenur.appear(itm,null,eventPageUpdate);
    }
    
    @UiHandler("lnkModify")
    void onItemModify(ClickEvent e) {
    	ItemTenur itm = getFirstItem();
    	if(itm==null){
    		return;
    	}
    	itm.markModify();
    	Main.dlgModifyTenur.appear(itm,null,eventPageUpdate);
    }
    
    @UiHandler("lnkDelete")
    void onItemDelete(ClickEvent e) {
    	ItemTenur itm = getFirstItem();
    	if(itm==null){
    		return;
    	}    	
    	itm.markDelete();//how to delete multiple-items???
    	Main.dlgModifyTenur.appear(itm,null,eventPageUpdate);
    }
    //--------------------------------//
    
	@Override
	public void onSearching(String txt) {
		//searching???
	}

	@Override
	public void getDatum(int offset, int limit) {
		//MaterialLoader.showLoading(true);
		Main.rpc.listTenurByRow(offset,limit,rpcPageUpdate);
	}

	@Override
	public void prepare_column(DataGrid<ItemTenur> grid) {
		
		grid.addColumn(new ColInfo(ItemTenur.INFO_TKEY),"代號");  // - 0 
		grid.addColumn(new ColInfo(ItemTenur.INFO_DEV_VENDOR),"儀器廠商");// - 1 
		grid.addColumn(new ColInfo(ItemTenur.INFO_DEV_SERIAL),"儀器型號");// - 2   
		grid.addColumn(new ColInfo(ItemTenur.INFO_DEV_NUMBER),"儀器序號");// - 3 
		grid.addColumn(new ColInfo(ItemTenur.INFO_DET_TYPE  ),"偵檢種類");// - 4
		grid.addColumn(new ColInfo(ItemTenur.INFO_DET_SERIAL),"偵檢型號");// - 5 
		grid.addColumn(new ColInfo(ItemTenur.INFO_DET_NUMBER),"偵檢序號");// - 6
		grid.addColumn(new ColOKey(),"委託單位");// - 7
		grid.addColumn(new ColInfo(ItemTenur.INFO_MEMO),"備註");// - 8

		grid.setColumnWidth(0,"9em");
		grid.setColumnWidth(7,"5.5em");
	}
	
	protected class ColOKey extends TextColumn<ItemTenur>{
		@Override
		public String getValue(ItemTenur object) {
			ItemOwner own = object.getOwner();
			if(own==null){
				return "???";
			}
			return own.getKey();
		}
	};
}
