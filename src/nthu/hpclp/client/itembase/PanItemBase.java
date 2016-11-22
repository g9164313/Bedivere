package nthu.hpclp.client.itembase;

import java.util.ArrayList;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;

import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialNavBar;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSearch;
import narl.itrc.client.ExComposite;
import nthu.hpclp.client.Main;
import nthu.hpclp.shared.ItemBase;
import nthu.hpclp.shared.ItemOwner;
import nthu.hpclp.shared.ItemTenur;

public abstract class PanItemBase<T> extends ExComposite {
	
	public PanItemBase(String type){
		prepare_searchbar();
		prepare_pager_idx();
		prepare_data_grid(type);
		_root.add(dlgPageIdx);
		addShortcut(KeyCodes.KEY_PAGEUP, KeyCodes.KEY_PAGEDOWN);
	}
		
	protected MaterialPanel _root = new MaterialPanel();

	protected MaterialLink _lnk_start_search = new MaterialLink();
	protected MaterialNavBar _nav_appbar = new MaterialNavBar();
	protected MaterialNavBar _nav_search= new MaterialNavBar();
	protected MaterialSearch _box_search = new MaterialSearch();
    protected MaterialLink _page_hint = new MaterialLink();
    
	@Override
    public void eventShortcut(Integer keycode,Integer appx){
		switch(keycode){
		case KeyCodes.KEY_PAGEUP:
			pagePrev();
			break;
		case KeyCodes.KEY_PAGEDOWN:
			pageNext();			
			break;
		}
    }
    
	@Override
	public void onEventShow() {
		_nav_appbar.add(Main.funcPager);//???why
		pageUpdate();
	}

	@Override
	public void onEventHide() {
	}
	//--------------------------------//
    
	private final int PAGE_COUNT = 30;
	
	private int pageIdx = 1;//one-base!!!
	private int pageMax = 0;//total pager~~~
	
	protected void pageNext(){
		pageIdx++;
		if(pageIdx>pageMax){
			pageIdx = pageMax;
		}
		pageUpdate();
	}
	
	protected void pagePrev(){
		pageIdx--;
		if(pageIdx<1){
			pageIdx = 1;
		}
		pageUpdate();
	}
	
	protected void pageUpdate(){
		getDatum((pageIdx-1)*PAGE_COUNT,PAGE_COUNT);
	}
	
	protected DlgPageIndex dlgPageIdx = new DlgPageIndex();
	
    private void prepare_pager_idx(){		
		_page_hint.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				final ClickHandler eventScroll = new ClickHandler(){
					@Override
					public void onClick(ClickEvent event) {
						int idx = dlgPageIdx.getTarget();
						if(idx<1 || pageMax<idx){
							return;
						}
						pageIdx = idx;
						pageUpdate();
					}
				};
				dlgPageIdx.setTotal(pageMax);
				dlgPageIdx.appear(pageIdx,eventScroll);
			}
		});
    }
    
	public abstract void getDatum(int offset,int limit);
	
	protected void update_grid(ArrayList<T> datum){
		if(datum.size()!=0){
			//update total number~~~
			int cnt = Integer.valueOf(((ItemBase)datum.get(0)).appx[0]);
			pageMax = cnt/PAGE_COUNT;
			if((cnt%PAGE_COUNT)!=0){
				pageMax++;
			}
		}else{
			//reset pager!!!!
			pageIdx = pageMax = 0;
		}
		_page_hint.setText(""+pageIdx+"/"+pageMax);
		lst.setList(datum);
		lst.refresh();
	}
	//--------------------------------//
	
	public abstract void onSearching(String txt);
	
    private void prepare_searchbar(){
    	_lnk_start_search.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				_nav_appbar.setVisible(false);
				_nav_search.setVisible(true);  
				_box_search.setFocus(true);
			}
		});
		_box_search.addCloseHandler(new CloseHandler<String>(){
			@Override
			public void onClose(CloseEvent<String> event) {
				_nav_appbar.setVisible(true);
				_nav_search.setVisible(false);
			}
		});
		_box_search.addKeyDownHandler(new KeyDownHandler(){
			@Override
			public void onKeyDown(KeyDownEvent event) {
				int code = event.getNativeKeyCode();
		    	if(code!=KeyCodes.KEY_ENTER){
		    		return;
		    	}
		    	String txt = _box_search.getText().trim();
		    	onSearching(txt);
		    	_box_search.setText("");
		    	_box_search.setFocus(true);
			}
		});
    }
    //--------------------------------//

    protected DataGrid<T> grid = new DataGrid<T>();
	
	protected ListDataProvider<T> lst = new ListDataProvider<T>();
	
	protected MultiSelectionModel<T> mod = new MultiSelectionModel<T>();
	
	protected T getFirstItem(){
		Set<T> ss = mod.getSelectedSet();
		if(ss.isEmpty()==true){
			return null;
		}
		return ss.iterator().next();
	}
	
	protected ArrayList<T> getItems(){
		Set<T> ss = mod.getSelectedSet();
		if(ss.isEmpty()==true){
			return null;
		}
		ArrayList<T> lst = new ArrayList<T>();
		for(T itm:ss){
			lst.add(itm);
		}
		return lst;
	}
	
	private void prepare_data_grid(String type){
		grid.setSelectionModel(mod);
		grid.setSize("100%","83vh");
		grid.setEmptyTableWidget(new Label("無資料"));
		
		if(type.equalsIgnoreCase(ItemOwner.class.getName())==true){
			
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
			 
		}else if(type.equalsIgnoreCase(ItemTenur.class.getName())==true){

		}
		
		lst.addDataDisplay(grid);
	}

	private class ColInfo extends TextColumn<T> {
		private int idx;
		public ColInfo(int index){
			idx = index;
		}
		@Override
		public String getValue(T object) {
			ItemBase obj = (ItemBase)object;
			
			if(idx<0){
				int _idx = -(idx+1);
				if(_idx>=obj.appx.length){
					return "";
				}
				return obj.appx[-(idx+1)];
			}
			if(idx>=obj.info.length){
				return "";
			}		
			return obj.info[idx];
		}
	}
}
