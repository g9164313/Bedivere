package narl.hpclp.client.storage;

import java.util.ArrayList;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;

import narl.hpclp.shared.ItemParam.TxtPair;
import narl.itrc.client.ExComposite;

public abstract class BaseParam extends ExComposite {

	protected DataGrid<TxtPair> grid = new DataGrid<TxtPair>();
	
	protected ListDataProvider<TxtPair> provider = new ListDataProvider<TxtPair>();
	
	protected SingleSelectionModel<TxtPair> model = new SingleSelectionModel<TxtPair>();
	
	protected ArrayList<TxtPair> list = null;
	
	protected SimplePanel archGrid = new SimplePanel();
	protected SimplePanel archPage = new SimplePanel();
	
	public BaseParam(ArrayList<TxtPair> lst){
		list = lst;
	}
	
	protected void initPart(){
		grid.setSelectionModel(model);
		grid.setSize("100%","50vh");
		for(TxtCol col:lstTxtCol){
			grid.addColumn(col,col.title);
		}
		//model part
		provider.addDataDisplay(grid);
		//control part
		SimplePager pager = new SimplePager();
		pager.setDisplay(grid);
		//finally, attach to panel
		archGrid.setWidget(grid);		
		archPage.setWidget(pager);
	}
	
	public static class TxtCol extends Column<TxtPair,String>{
		private int idx = -1;
		private String title = "???";
		public TxtCol(String title){
			this(title,-1);
		}
		public TxtCol(String title,int i){
			super(new TextCell());
			idx = i;
		}
		@Override
		public String getValue(TxtPair object) {
			if(idx==-1){
				return object.val;
			}
			String[] v = object.val.split("@");
			if(idx>=v.length){
				return "???";
			}
			return v[idx];
		}
	};
	
	private ArrayList<TxtCol> lstTxtCol = new ArrayList<TxtCol>();
	
	protected TxtCol addColumn(String title){
		return addColumn(title,-1);
	}
	
	protected TxtCol addColumn(String title,int idx){
		TxtCol box = new TxtCol(title,idx);
		lstTxtCol.add(box);
		return box;
	}
	
	@Override
	public void onEventShow() {
	}

	@Override
	public void onEventHide() {
	}
}
