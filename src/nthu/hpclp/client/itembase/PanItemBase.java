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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;

import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialNavBar;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSearch;
import gwt.material.design.client.ui.MaterialToast;
import narl.itrc.client.ExComposite;
import nthu.hpclp.client.Main;
import nthu.hpclp.shared.ItemBase;

public abstract class PanItemBase<T> extends ExComposite {
	
	public PanItemBase(){
		prepare_searchbar();
		prepare_pager_idx();
		prepare_data_grid();
		_root.add(dlgPageIdx);
		addShortcut(
			KeyCodes.KEY_PAGEUP, 
			KeyCodes.KEY_PAGEDOWN
		);
		addAltShortcut(
			KeyCodes.KEY_N,
			KeyCodes.KEY_E,
			KeyCodes.KEY_D
		);
	}
		
	protected MaterialPanel _root = new MaterialPanel();

	protected MaterialLink _lnk_start_search = new MaterialLink();
	protected MaterialNavBar _nav_appbar = new MaterialNavBar();
	protected MaterialNavBar _nav_search= new MaterialNavBar();
	protected MaterialSearch _box_search = new MaterialSearch();
    protected MaterialLink _page_hint = new MaterialLink();
    
    abstract void onItemCreate(ClickEvent e);
    abstract void onItemModify(ClickEvent e);
    abstract void onItemDelete(ClickEvent e);
    
	@Override
    public void eventShortcut(Integer keycode,Integer appx){
		if(isAlive()==false){
			return;
		}
		switch(keycode){
		case KeyCodes.KEY_N:
			onItemCreate(null);
			break;
		case KeyCodes.KEY_E:
			onItemModify(null);
			break;
		case KeyCodes.KEY_D:
			onItemDelete(null);
			break;
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
    
	protected final int PAGE_COUNT = 30;
	
	private int pageIdx = 1;//one-base!!!
	private int pageMax = 1;//total pager~~~
	
	protected void pageNext(){
		pageIdx++;
		if(pageIdx>pageMax){
			pageIdx = pageMax;
			return;//we don't need to refresh grid again~~~~
		}
		pageUpdate();
	}
	
	protected void pagePrev(){
		pageIdx--;
		if(pageIdx<=1){
			pageIdx = 1;
			return;//we don't need to refresh grid again~~~~
		}
		pageUpdate();
	}
	
	protected void resetOnePage(){
		pageIdx = pageMax = 1;
	}
	
	protected void pageUpdate(){
		getDatum(
			(pageIdx-1)*PAGE_COUNT,
			PAGE_COUNT,
			_box_search.getText().trim()
		);
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
    
	public abstract void getDatum(int offset,int limit,String postfix);
	
	protected void update_grid(ArrayList<T> datum){
		if(datum.size()!=0){			
			ItemBase itm = (ItemBase)datum.get(0);
			if(itm.appx!=null){
				//we have it, update total page number~~~
				int cnt = Integer.valueOf(itm.appx[0]);
				pageMax = cnt/PAGE_COUNT;
				if((cnt%PAGE_COUNT)!=0){
					pageMax++;
				}
			}else{
				//we don't have this information, so just reset this variable~~~~
				pageMax = 1;
			}
		}else{
			//reset pager!!!!
			resetOnePage();
		}
		_page_hint.setText(""+pageIdx+"/"+pageMax);
		lst.setList(datum);
		lst.refresh();
	}
	
	protected final ClickHandler eventPageUpdate = new ClickHandler(){
		@Override
		public void onClick(ClickEvent event) {
			 pageUpdate();
		}
	};
	
	protected final AsyncCallback<ArrayList<T>> rpcPageUpdate = new AsyncCallback<ArrayList<T>>(){
		@Override
		public void onFailure(Throwable caught) {
			MaterialToast.fireToast("內部錯誤\n"+caught.getMessage());
		}
		@Override
		public void onSuccess(ArrayList<T> result) {
			update_grid(result);
		}
	};
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
		    	onSearching(_box_search.getText().trim());
		    	//we don't need to clear search text
		    	//let user list table again~~
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
	
	public abstract void prepare_column(DataGrid<T> grid);
	
	private void prepare_data_grid(){
		grid.setSelectionModel(mod);
		grid.setSize("100%","83vh");
		grid.setEmptyTableWidget(new Label("無資料"));		
		prepare_column(grid);
		lst.addDataDisplay(grid);
	}

	protected class ColInfo extends TextColumn<T> {
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
