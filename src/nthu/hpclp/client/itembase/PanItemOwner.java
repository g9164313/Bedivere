package nthu.hpclp.client.itembase;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialNavBar;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSearch;
import gwt.material.design.client.ui.MaterialToast;
import nthu.hpclp.shared.ItemOwner;
import nthu.hpclp.client.Main;

public class PanItemOwner extends PanItemBase<ItemOwner> {

	private static PanItemOwnerUiBinder uiBinder = GWT.create(PanItemOwnerUiBinder.class);

	interface PanItemOwnerUiBinder extends UiBinder<Widget, PanItemOwner> {
	}

	public PanItemOwner() {
		super(ItemOwner.class.getName());
		initWidget(uiBinder.createAndBindUi(this));
		anchorGrid.add(grid);
		root.add(Main.dlgEditOwner);
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
    	final ClickHandler eventDone = new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				 pageUpdate();
			}
    	};
    	Main.dlgEditOwner.appear(itm,null,eventDone);
    }
    
    @UiHandler("lnkModify")
    void onItemModify(ClickEvent e) {
    	ItemOwner itm = getFirstItem();
    	if(itm==null){
    		return;
    	}
    	final ClickHandler eventDone = new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				 pageUpdate();
			}
    	};
    	Main.dlgEditOwner.appear(itm,null,eventDone);
    }
    
    @UiHandler("lnkDelete")
    void onItemDelete(ClickEvent e) {
    	final ItemOwner itm = getFirstItem();
    	if(itm==null){
    		return;
    	}
    	itm.markDelete();
    	//how to delete multi-items???
		final ClickHandler eventDelete = new ClickHandler(){
			private final AsyncCallback<ItemOwner> eventDone = new AsyncCallback<ItemOwner>() {
				@Override
				public void onFailure(Throwable caught) {
				}
				@Override
				public void onSuccess(ItemOwner result) {
					pageUpdate();
				}
			};
			@Override
			public void onClick(ClickEvent event) {
				Main.rpc.modifyOwner(itm,eventDone);
			}
		};
    	dlgApprove.appear("確認刪除？",eventDelete);
    }
    //--------------------------------//
    
	@Override
	public void onSearching(String txt) {
		//searching???
	}

	@Override
	public void getDatum(int offset, int limit) {
		//MaterialLoader.showLoading(true);
		final AsyncCallback<ArrayList<ItemOwner>> event = new AsyncCallback<ArrayList<ItemOwner>>(){
			@Override
			public void onFailure(Throwable caught) {
				//MaterialLoader.showLoading(false);
				MaterialToast.fireToast("內部錯誤\n"+caught.getMessage());
			}
			@Override
			public void onSuccess(ArrayList<ItemOwner> result) {
				//MaterialLoader.showLoading(false);
				update_grid(result);
			}
		};
		Main.rpc.listOwnerByRow(offset,limit,event);
	}
}


