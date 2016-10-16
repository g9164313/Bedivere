package nthu.hpclp.client.product;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialListBox;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Option;
import narl.itrc.client.ExComposite;
import nthu.hpclp.client.Main;
import nthu.hpclp.shared.ItemParam;
import nthu.hpclp.shared.ItemProdx;
import nthu.hpclp.shared.ParmEmitter;

public class PartEmitter extends ExComposite {

	private static PartEmitterUiBinder uiBinder = GWT.create(PartEmitterUiBinder.class);

	interface PartEmitterUiBinder extends UiBinder<Widget, PartEmitter> {
	}
	
	@UiField
	MaterialPanel root;
	
	@UiField MaterialLabel 
		txtKindArea,txtStrength,txtSurface,
		txtFactorK,txtFactorP,txtUncertain,
		txtSerial,txtCriteron;
	
	@UiField
	MaterialListBox cmbInput;
	
	public PartEmitter() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void onEventShow() {
		cmbInput.clear();
		cmbInput.add(new Option("自訂"));//if we can't find target, just select this one~~~
		//Cs-gamma is 'speical'
		int year1=1, year2=1;
		for(ItemParam parm:Main.param.prodxEmitter){
			String val = parm.getVal();
			ParmEmitter e = new ParmEmitter(val);
			String name = e.getTitle();
			//check tail is number!!!
			if(name.matches(".*-\\p{Digit}{3}$")==false){
				continue;
			}
			try{
				int yr = Integer.valueOf(
					name.substring(name.length()-3)
				);
				if(yr>year1){
					year1 = yr;
					year2 = year1 - 1;
				}
			}catch(NumberFormatException err){
				continue;
			}
		}
		//create options~~~
		for(ItemParam parm:Main.param.prodxEmitter){
			String val = parm.getVal();
			ParmEmitter e = new ParmEmitter(val);
			String name = e.getTitle();
			Option opt = new Option();
			opt.setText(name);
			opt.setValue(val);
			if(
				name.endsWith("-"+year1)==true ||
				name.endsWith("-"+year2)==true
			){
				cmbInput.add(opt);
			}else if(name.contains("-gamma-")==false){
				cmbInput.add(opt);
			}
		}
	}

	@Override
	public void onEventHide() {
	}
	
	@UiHandler("cmbInput")
	void onChangeListBox(ValueChangeEvent<String> event) {
		String val = cmbInput.getSelectedValue();
		target.setEmitter(val);
		txtMapping();
	}
	//---------------------------------------//

	private ItemProdx target;
	
	public void setTarget(ItemProdx obj){
		target = obj;
		if(target!=null){
			txtMapping();
			cmbMapping();
		}else{
			txtClear();
		}
	}
	
	private void txtMapping(){
		ParmEmitter e = target.getEmitter();
		txtKindArea.setText(e.getKind()+" "+e.getArea());
		txtStrength.setText(e.getStrength());
		txtSurface.setText(e.getSurface());
		txtFactorK.setText(e.getFactorK());
		txtFactorP.setText(e.getFactorP());
		txtUncertain.setText(e.getUncertain());
		txtSerial.setText(e.getSerial());
		txtCriteron.setText(e.getCriterion());
	}
	
	private void cmbMapping(){
		ParmEmitter e = target.getEmitter();
		String name = e.getTitle();
		cmbInput.getOptionElement(0).setValue(e.toString());
		for(int i=1; i<cmbInput.getItemCount(); i++){
			String option = cmbInput.getItemText(i);
			if(name.equalsIgnoreCase(option)==true){
				cmbInput.setSelectedIndex(i);
				return;
			}
		}
		cmbInput.setSelectedIndex(0);
	}
	
	private void txtClear(){
		txtKindArea.setText("");
		txtStrength.setText("");
		txtSurface.setText("");
		txtFactorK.setText("");
		txtFactorP.setText("");
		txtUncertain.setText("");
		txtSerial.setText("");
		txtCriteron.setText("");
		cmbInput.setSelectedIndex(0);
	}
}
