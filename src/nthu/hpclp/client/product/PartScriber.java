package nthu.hpclp.client.product;

import java.util.ArrayList;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;

import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSwitch;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import nthu.hpclp.shared.Const;
import nthu.hpclp.shared.ItemProdx;

public class PartScriber extends Composite {

	private static PartScriberUiBinder uiBinder = GWT.create(PartScriberUiBinder.class);

	interface PartScriberUiBinder extends UiBinder<Widget, PartScriber> {
	}

	public PartScriber() {
		initWidget(uiBinder.createAndBindUi(this));
		init_grid();
	}

	@UiField
	MaterialPanel root;

	@UiField
	MaterialTextBox boxInput;

	@UiField
	MaterialSwitch modSwitch;
	
	@UiField 
	SimplePanel anchor1, anchor2;
	
	private ItemProdx target;
	
	private DataGrid<String> gridCom = new DataGrid<String>();
	private ListDataProvider<String> provCom = new ListDataProvider<String>();
	
	private DataGrid<String> gridAdv = new DataGrid<String>();
	private ListDataProvider<String> provAdv = new ListDataProvider<String>();
	
	private static ArrayList<String> dummy = new ArrayList<String>();
	
	public void setTarget(ItemProdx obj){
		target = obj;
		if(target!=null){
			provCom.setList(target.scribble);
			provAdv.setList(target.scribble);
		}else{
			provCom.setList(dummy);
			provAdv.setList(dummy);
		}
	}
	//---------------------------------------//
	
	private void init_grid(){
		prepare_grid(anchor1,gridCom,provCom);
		prepare_grid(anchor2,gridAdv,provAdv);
		prepare_col_common();
		prepare_col_advance();
		if(modSwitch.getValue()==false){
			anchor1.setVisible(true);
			anchor2.setVisible(false);
		}else{
			anchor1.setVisible(false);
			anchor2.setVisible(true);
		}
	}
	
	private void prepare_grid(
		SimplePanel anchor,
		DataGrid<String> grid,
		ListDataProvider<String> prov
	){	
		SingleSelectionModel<String> model = new SingleSelectionModel<String>();
		
		grid.setSelectionModel(model);
		grid.setSize("100%","30vh");
		grid.setEmptyTableWidget(new Label("無資料"));
		prov.addDataDisplay(grid);
		
		SimplePager pager = new SimplePager();
		pager.setDisplay(grid);
		anchor.setWidget(grid);
	}
	
	private class ColText extends Column<String,String>{
		private int idx = 0;
		public ColText(int i) {
			super(new TextCell());
			idx = i;
		}
		@Override
		public String getValue(String object) {
			String[] txt = object.split("@");
			if(idx>=txt.length){
				return "";
			}
			return txt[idx];
		}
	};
	
	private void prepare_col_common(){
		gridCom.addColumn(new ColText(0),"刻度");
		gridCom.addColumn(new ColText(1),"參考值");
		gridCom.addColumn(new ColText(2),"器示值");
		gridCom.setColumnWidth(0,"7em");
		gridCom.setColumnWidth(1,"8em");
	}
	//---------------------------------------//
	
	private void prepare_col_advance(){
		gridAdv.addColumn(new ColText(0),"刻度");
		gridAdv.addColumn(new ColText(1),"參考");
		gridAdv.addColumn(new ColText(2),"器示");
		gridAdv.addColumn(new ColText(3),"不確定度修剪");
		gridAdv.addColumn(new ColText(4),"因子/效率");		
		gridAdv.setColumnWidth(0,"3em");
		gridAdv.setColumnWidth(1,"9em");
		gridAdv.setColumnWidth(3,"4em");
		gridAdv.setColumnWidth(4,"4em");
	}
	//---------------------------------------//
	
	@UiHandler("modSwitch")
    void onStartSearch(ClickEvent e) {
		if(modSwitch.getValue()==false){
			anchor1.setVisible(true);
			anchor2.setVisible(false);
		}else{
			anchor1.setVisible(false);
			anchor2.setVisible(true);
		}
	}
	
	@UiHandler("boxInput")
	void onChangeScribe(ValueChangeEvent<String> event) {
		// replace space and translate format
		String val = event.getValue().trim();
		if (val.length() == 0) {
			return;
		}
		if (val.charAt(0) == '*') {
			val = "×" + val.substring(1);// star symbol can presents scale one
		}
		String abbrev = val.replaceAll("\\s", "")
				.replace('/', '@')
				.replace(';', '@')
				.replace('+', ',')
				.replace('x', '×')
				.replace('X', '×');// trick to fix all typo!!!
		if (abbrev.startsWith(Const.SPECIAL_PREFIX) == true) {
			// It is the special keyword!!!
			abbrev = abbrev + "@0@0";
		} else {
			String[] arg = abbrev.split("@");
			if (arg.length <= 2) {
				// we should have 3 items
				MaterialToast.fireToast("格式錯誤");
				return;
			}
			String[] vvv = arg[2].split(",");
			for (int i = 0; i < vvv.length; i++) {
				// check format is valid~~~~~
				try {
					Double.valueOf(vvv[i]);
				} catch (NumberFormatException e) {
					MaterialToast.fireToast("格式錯誤 -" + vvv[i]);
					return;
				}
			}
			// combine all arguments~~~~
			abbrev = arg[0];
			for (int i = 1; i < arg.length; i++) {
				abbrev = abbrev + "@" + arg[i];
			}
		}
		//update data~~~~
		target.scribble.add(abbrev);
		provCom.setList(target.scribble);
		provAdv.setList(target.scribble);
		boxInput.setText("");// for next turn~~~~
	}

}
