package narl.hpclp.client;

import java.util.List;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

public abstract class GrdTemplate<T> {
	
	public ListDataProvider<T> lst = new ListDataProvider<T>();
	public DataGrid<T> grd = new DataGrid<T>();
	public Widget modModify = null;
	
	public GrdTemplate(){
	}

	private Column<T,String> colDelete = 
		new Column<T,String>(new ButtonCell())
	{
		@Override
		public String getValue(T object) {
			return "刪除";
		}
	};
	private FieldUpdater<T,String> fldDelete = 
		new FieldUpdater<T,String>()
	{
		@Override
		public void update(
			int index, 
			T object, 
			String value
		) {
			lst.getList().remove(index);
			grd.redraw();
		}
	};

	private Column<T,String> colModify = 
		new Column<T,String>(new ButtonCell())
	{
		@Override
		public String getValue(T object) {
			return "修改";
		}
	};
	private FieldUpdater<T,String> fldModify =
		new FieldUpdater<T,String>()
	{	
		@Override
		public void update(
			int index, 
			T object, 
			String value
		) {
			modIndex =index;
			modTarget=object;
			if(modModify==null){
				return;
			}
			//TODO: how to modify~~~
		}
	};
	
	public int modIndex;
	public T modTarget = null;
	
	public void init(SimplePanel arch){
		
		grd.setEmptyTableWidget(new InlineLabel("無資料"));
		grd.setSize("100%","30em");
		grd.setStyleName("striped responsive-table");
		lst.addDataDisplay(grd);
		arch.add(grd);

		colDelete.setFieldUpdater(fldDelete);
		grd.addColumn(colDelete,"");		
		grd.setColumnWidth(0,"5em");
		
		colModify.setFieldUpdater(fldModify);
		grd.addColumn(colModify,"");		
		grd.setColumnWidth(1,"5em");
		
		initCol();
	}
	
	public abstract void initCol();
	
	/**
	 * update data-grid.Therefore，fill data into grid table<p>
	 * @param src - list data to show
	 */
	public void reload(List<T> src){
		lst.setList(src);
		grd.redraw();
	}
	
	/**
	 * Refresh data in item ou update item.<p>
	 * @param dst - the list is replaced
	 */
	public void refresh(List<T> dst){
		dst.clear();
		for(T obj:lst.getList()){
			dst.add(obj);
		}
	}
}
