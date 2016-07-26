package narl.hpclp.client.product;

import java.util.ArrayList;

import narl.hpclp.shared.FormulaSet;
import narl.hpclp.shared.ItemProdx;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialCollectionItem;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialRow;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class CitScribe extends MaterialCollectionItem implements ClickHandler {

	/**
	 * the index of scribe list, it is 0-base.<p>
	 */
	private int idx;
	
	private int fmt;
	
	private double suf,stn;
	
	private ArrayList<String> lst;
	
	public CitScribe(ItemProdx prodx,int index){
		fmt = prodx.format;
		if(prodx.tenur!=null){
			String unit = prodx.getUnitMea();
			String area = prodx.tenur.getArea();
			suf = prodx.getEmitter()._prepare_surf_unit(unit,area);
			stn = prodx.getEmitter()._prepare_strn_unit(unit,area);
		}
		lst = prodx.scribble;		
		idx = (index<0)?(lst.size()-1):(index);
		setWaves(WavesType.DEFAULT);
		initLayout();
		addClickHandler(this);
	}
	
	public CitScribe(ItemProdx prodx){		
		this(prodx,-1);
	}
	
	private MaterialLabel info1 = new MaterialLabel();
	private MaterialLabel info2 = new MaterialLabel();
	private MaterialLabel info3 = new MaterialLabel();
	private MaterialLabel info4 = new MaterialLabel();
	private MaterialLabel info5 = new MaterialLabel();
	
	private void initLayout(){
		MaterialRow row = new MaterialRow();
		row.setMargin(0.);		
		String txt = lst.get(idx);
		if(txt.contains("調整")==true){
			add_col(row,info1,"");
		}else{
			add_col(row,info1,"2em");//刻度
			add_col(row,info2,"8em");//參考or背景值
			add_col(row,info3,"8em");//器示值
			add_col(row,info4,"4em");//不確定度修剪
			add_col(row,info5,"4em");//因子or效率
		}
		add(row);
	
		refresh_data();
	}
	
	private void refresh_data(){
		String txt = lst.get(idx);
		if(txt.contains("調整")==true){
			info1.setText("----調整----");
		}else{
			String[] val = txt.split("@");
			info1.setText(val[0]);//刻度
			info2.setText(FormulaSet.AvgDev(val[1]));//參考or背景值
			info3.setText(FormulaSet.AvgDev(val[2]));//器示值
			info4.setText(FormulaSet.xxx(val[2],0,0.));//不確定度修剪
			switch(fmt){
			case ItemProdx.FMT_F1V:
			case ItemProdx.FMT_F1W:
			case ItemProdx.FMT_F2:
				info5.setText(FormulaSet.getFactor(val[1],val[2]));//因子
				break;
			case ItemProdx.FMT_F3:
				info5.setText(FormulaSet.getResponse(val[1],val[2]));//反應
				break;
			case ItemProdx.FMT_F5:
				info5.setText(FormulaSet.getEffect(val[1],val[2],suf));//效率
			case ItemProdx.FMT_F4:
				info5.setText(FormulaSet.getEffect(val[1],val[2],stn));//效率
				break;
			}			
		}		
	}
	
	private void add_col(MaterialRow row,MaterialLabel info,String width){
		MaterialColumn col = new MaterialColumn();
		if(width.length()!=0){
			info.setWidth(width);
		}
		col.add(info);
		row.add(col);
	}
	
	@Override
	public void onClick(ClickEvent event) {
		target = CitScribe.this;//current target~~~
		String[] val = lst.get(idx).split("@");		
		PanMain.boxScribe0.setText(val[0]);
		PanMain.boxScribe1.setText(val[1]);
		PanMain.boxScribe2.setText(val[2]);
		switch(fmt){
		case ItemProdx.FMT_F1V:
		case ItemProdx.FMT_F1W:
		case ItemProdx.FMT_F2:
		case ItemProdx.FMT_F5:
		case ItemProdx.FMT_F4:
			PanMain.boxScribe1.setPlaceholder("參考值");
			break;
		case ItemProdx.FMT_F3:
			PanMain.boxScribe1.setPlaceholder("背景值");
			break;
		}		
		PanMain.dlgScribe.openModal();
	}
	

	public static CitScribe target = null; 
	
	public static final ClickHandler eventEdit = new ClickHandler(){
		@Override
		public void onClick(ClickEvent event) {
			if(target==null){
				return;
			}			
			String t1 = PanMain.boxScribe0.getText();
			String t2 = PanMain.boxScribe1.getText();
			String t3 = PanMain.boxScribe2.getText();
			GWT.log("change text --> "+t1+"@"+t2+"@"+t3);
			//how to check ???
			target.lst.set(target.idx,t1+"@"+t2+"@"+t3);
			target.refresh_data();
			PanMain.dlgScribe.closeModal();
		}
	};

	public static final ClickHandler eventDelete = new ClickHandler(){
		@Override
		public void onClick(ClickEvent event) {
			target.lst.remove(target.idx);
			PanMain.refresh_scribe();
			PanMain.dlgScribe.closeModal();
		}
	};
	
	public static final ClickHandler eventCancel = new ClickHandler(){
		@Override
		public void onClick(ClickEvent event) {
			PanMain.dlgScribe.closeModal();
		}
	};
}
