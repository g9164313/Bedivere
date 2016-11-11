package nthu.hpclp.client.setting;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialModal;
import gwt.material.design.client.ui.MaterialTextBox;
import narl.itrc.client.DlgBase;
import nthu.hpclp.shared.Const;
import nthu.hpclp.shared.ItemParam;
import nthu.hpclp.shared.ParmEmitter;

public class DlgModifyEmitter extends DlgBase<ItemParam> {

	private static DlgModifyEmitterUiBinder uiBinder = GWT.create(DlgModifyEmitterUiBinder.class);

	interface DlgModifyEmitterUiBinder extends UiBinder<Widget, DlgModifyEmitter> {
	}

	public DlgModifyEmitter() {
		initWidget(uiBinder.createAndBindUi(this));
		chainBox(
			txtTitle,txtKind,txtArea,
			txtSerial,txtCriteron,
			txtStrength,txtSurface,
			txtFactorK,txtFactorP,txtUncertain
		);//sequence is important
	}
	
	@UiField(provided=true) 
	MaterialModal root = _dlg_root;
	@UiField(provided=true)
	MaterialButton btnAction = _btn_action;
	@UiField(provided=true)
	MaterialButton btnCancel = _btn_cancel;
	
	@UiField
	MaterialTextBox txtTitle,txtKind,txtArea;
	@UiField
	MaterialTextBox txtSurface,txtStrength;
	@UiField
	MaterialTextBox txtSerial,txtCriteron;
	@UiField
	MaterialTextBox txtUncertain,txtFactorK,txtFactorP;
	
	private ParmEmitter emitter;
	
	@Override
	public String toString(){
		emitter.setTitle(txtTitle.getValue());
		emitter.setKind(txtKind.getValue());
		emitter.setArea(txtArea.getText());
		emitter.setSurface(txtSurface.getText());
		emitter.setStrength(txtStrength.getText());
		emitter.setSerial(txtSerial.getText());
		emitter.setCriterion(txtCriteron.getText());
		emitter.setUncertain(txtUncertain.getText());
		emitter.setFactorK(txtFactorK.getText());
		emitter.setFactorP(txtFactorP.getText());
		return emitter.toString();
	}

	@Override
	public void eventAppear(ItemParam item) {
		emitter = new ParmEmitter(item.getVal());
		txtTitle.setText(emitter.getTitle());
		txtKind.setText(emitter.getKind());
		txtArea.setText(emitter.getArea());
		txtSurface.setText(emitter.getSurface());
		txtStrength.setText(emitter.getStrength());
		txtSerial.setText(emitter.getSerial());
		txtCriteron.setText(emitter.getCriterion());
		txtUncertain.setText(emitter.getUncertain());
		txtFactorK.setText(emitter.getFactorK());
		txtFactorP.setText(emitter.getFactorP());
	}
	
	@UiHandler("txtTitle")
	public void onChangeBox1(ValueChangeEvent<String> event) {
		Const.makeSpecialCharacter(txtTitle);
	}
	
	@UiHandler("txtKind")
	public void onChangeBox2(ValueChangeEvent<String> event) {
		Const.makeSpecialCharacter(txtKind);
	}
	
	@UiHandler("txtArea")
	public void onChangeBox3(ValueChangeEvent<String> event) {
		Const.makeSpecialCharacter(txtArea);
	}
	
	@UiHandler("txtSurface")
	public void onChangeBox4(ValueChangeEvent<String> event) {
		Const.makeSpecialCharacter(txtSurface);
	}
	
	@UiHandler("txtStrength")
	public void onChangeBox5(ValueChangeEvent<String> event) {
		Const.makeSpecialCharacter(txtStrength);
	}
	
	@UiHandler("txtSerial")
	public void onChangeBox6(ValueChangeEvent<String> event) {
		Const.makeSpecialCharacter(txtSerial);
	}
	
	@UiHandler("txtCriteron")
	public void onChangeBox7(ValueChangeEvent<String> event) {
		Const.makeSpecialCharacter(txtCriteron);
	}
	
	@UiHandler("txtUncertain")
	public void onChangeBox8(ValueChangeEvent<String> event) {
		Const.makeSpecialCharacter(txtUncertain);
	}
	
	@UiHandler("txtFactorK")
	public void onChangeBox9(ValueChangeEvent<String> event) {
		Const.makeSpecialCharacter(txtFactorK);
	}
	
	@UiHandler("txtFactorP")
	public void onChangeBox10(ValueChangeEvent<String> event) {
		Const.makeSpecialCharacter(txtFactorP);
	}
	
	
	@Override
	public void takeAction(ClickEvent event) {
	}
	
	@Override
	public void takeCancel(ClickEvent event) {

	}
}
