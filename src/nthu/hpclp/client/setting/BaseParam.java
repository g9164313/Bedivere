package nthu.hpclp.client.setting;

import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;

import gwt.material.design.client.ui.MaterialToast;
import narl.itrc.client.ExComposite;
import nthu.hpclp.client.Main;
import nthu.hpclp.shared.ItemParam;
import nthu.hpclp.shared.RandUtil;

public abstract class BaseParam extends ExComposite {

	protected DataGrid<ItemParam> grid = new DataGrid<ItemParam>();
	protected ListDataProvider<ItemParam> provider = new ListDataProvider<ItemParam>();
	protected SingleSelectionModel<ItemParam> model = new SingleSelectionModel<ItemParam>();
	
	protected SimplePanel archGrid = new SimplePanel();
	protected SimplePanel archPage = new SimplePanel();
	
	/**
	 * the prefix of key
	 */
	private String k_prefix = "PARM_";
	
	private int toalColumn = 1;
	
	public class TxtCol extends Column<ItemParam,String>{
		/**
		 * the index of column in data
		 */
		private int cid = -1;
		public TxtCol(int i){
			super(new EditTextCell());
			cid = i;
			setFieldUpdater(eventUpdate);
		}		
		@Override
		public String getValue(ItemParam object) {
			return object.getVal(cid);
		}
		private FieldUpdater<ItemParam,String> eventUpdate = 
			new FieldUpdater<ItemParam,String>()
		{
			@Override
			public void update(int index, ItemParam object, String value) {
				String val = object.getVal(cid);
				if(value.equals(val)){
					return;//they are same~~~
				}				
				object.setVal(cid,value);
				object.setState(ItemParam.STA_UPDATE);
				
				final AsyncCallback<ItemParam> event = new AsyncCallback<ItemParam>(){
					@Override
					public void onFailure(Throwable caught) {
						String msg = caught.getMessage();
						MaterialToast.fireToast("內部錯誤："+msg,5000);
						System.out.println(msg);
					}
					@Override
					public void onSuccess(ItemParam result) {		
						if(result.error!=null){
							MaterialToast.fireToast("ERROR:"+result.error,5000);
						}else{
							MaterialToast.fireToast("已更新 "+result.getKey(),2000);
							provider.getList().add(result);
							provider.refresh();
						}						
					}
				};
				Main.rpc.modifyParam(
					object,
					event
				);
				provider.getList().remove(object);		
			}		
		};
	};

	protected void createParam(){

		ItemParam obj = new ItemParam(
			k_prefix+"_"+RandUtil.uuid(7,10),
			toalColumn
		);		
		ItemParam src = model.getSelectedObject();
		if(src!=null){
			obj.copyFrom(src);
			obj.setVal(0,"新增項目");
		}		
		obj.setState(ItemParam.STA_CREATE);
		
		GWT.log("obj{"+obj.getKey()+", "+obj.getVal()+"}");
		
		final AsyncCallback<ItemParam> event = new AsyncCallback<ItemParam>(){
			@Override
			public void onFailure(Throwable caught) {
				String msg = caught.getMessage();
				MaterialToast.fireToast("內部錯誤："+msg,5000);
				System.out.println(msg);
			}
			@Override
			public void onSuccess(ItemParam result) {
				if(result.error!=null){
					MaterialToast.fireToast(result.error,5000);
				}else{
					MaterialToast.fireToast("已新增 "+result.getKey(),2000);
					provider.getList().add(0,result);
					provider.refresh();
				}
			}
		};
		Main.rpc.modifyParam(obj,event);		
	}
	
	protected void deleteParam(){
		final ItemParam obj = model.getSelectedObject();
		if(obj==null){
			return;
		}
		obj.setState(ItemParam.STA_DELETE);
		final AsyncCallback<ItemParam> event = new AsyncCallback<ItemParam>(){
			@Override
			public void onFailure(Throwable caught) {
				String msg = caught.getMessage();
				MaterialToast.fireToast("內部錯誤："+msg,5000);
				System.out.println(msg);
			}
			@Override
			public void onSuccess(ItemParam result) {
				if(result.error!=null){
					MaterialToast.fireToast(result.error,5000);
				}else{
					MaterialToast.fireToast("已刪除 "+obj.getKey(),2000);
					provider.getList().remove(obj);
					provider.refresh();
				}
			}
		};
		Main.rpc.modifyParam(obj,event);
	}
	
	public BaseParam(){
	}
	
	/**
	 * Create columns for grid.<p>
	 * @param prefix - data prefix
	 * @param name - column name
	 */
	protected void initPartByName(String prefix,String... name){
		String[] style = new String[name.length*2];
		for(int i=0; i<name.length; i++){
			style[i*2+0] = name[i];
			style[i*2+1] = null;
		}
		init_part(prefix,style);
	}
	
	/**
	 * Create columns for grid.<p>
	 * Remember 'style' is 2-dimension array, first is name, second item is CSS width.<p>
	 * @param prefix - data prefix
	 * @param name - column style (name + width)
	 */
	protected void initPartByStyle(String prefix,String... style){
		init_part(prefix,style);
	}
	
	private void init_part(String prefix,String... style){
		
		k_prefix = prefix;
		
		toalColumn = (style.length/2);
		
		grid.setSelectionModel(model);
		grid.setSize("100%","43vh");
		grid.setEmptyTableWidget(new Label("無資料"));
		for(int i=0; i<toalColumn; i++){
			grid.addColumn(new TxtCol(i),style[i*2+0]);
			if(style[i*2+1]!=null){
				grid.setColumnWidth(i,style[i*2+1]);
			}
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
}
