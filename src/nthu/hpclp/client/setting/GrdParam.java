package nthu.hpclp.client.setting;

import java.util.ArrayList;

import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;

import nthu.hpclp.shared.ItemParam;

public class GrdParam extends DataGrid<ItemParam> {

	public GrdParam(){
		init_grid();
	}
	
	public GrdParam(ArrayList<ItemParam> data){
		datum = data;
		init_grid();
		data2grid();
	}
	
	public ArrayList<ItemParam> datum = null;
	
	private ListDataProvider<ItemParam> lst = new ListDataProvider<ItemParam>();
	
	private SingleSelectionModel<ItemParam> mod = new SingleSelectionModel<ItemParam>();
	
	private final TextColumn<ItemParam> colKey = new TextColumn<ItemParam>(){
		@Override
		public String getValue(ItemParam obj) {
			return obj.getKey();
		}
	};
	
	private final TextColumn<ItemParam> colVal = new TextColumn<ItemParam>(){
		@Override
		public String getValue(ItemParam obj) {
			return obj.getVal();
		}
	};

	private void init_grid(){

		setSelectionModel(mod);
		setSize("100%","70vh");
		setEmptyTableWidget(new Label("無資料"));
		
		addColumn(colKey,"名稱");
		addColumn(colVal,"數值");
		addColumn(new ColCreate(this));
		addColumn(new ColModify(this));
		addColumn(new ColDelete(this));
		
		setColumnWidth(colKey,"10em");
		setColumnWidth(2,"5.9em");
		setColumnWidth(3,"5.9em");
		setColumnWidth(4,"5.9em");
		
		lst.addDataDisplay(this);

		SimplePager pager = new SimplePager();
		pager.setDisplay(this);
	}
	
	public void data2grid(ArrayList<ItemParam>  data){
		datum = data;
		data2grid();
	}
	
	public void data2grid(){
		lst.setList(datum);
		lst.refresh();
	}
}
