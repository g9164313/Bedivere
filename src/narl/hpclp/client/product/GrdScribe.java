package narl.hpclp.client.product;

import narl.hpclp.client.GrdTemplate;

class GrdScribe extends GrdTemplate<String>{
	
	@Override
	public void initCol() {
		grd.addColumn(new ColScribe(0),"刻度");
		grd.addColumn(new ColScribe(1,ColScribe.MODE_AVGDEV),"參考/背景值");
		grd.addColumn(new ColScribe(2,ColScribe.MODE_AVGDEV),"器示值");
		grd.addColumn(new ColScribe(2,ColScribe.MODE_UNCX),"不確定度修剪");
		grd.addColumn(new ColScribe(2,ColScribe.MODE_FACT),"因子/效率");
	}
};
