package narl.hpclp.server;

import narl.hpclp.shared.ItemAccnt;
import narl.hpclp.shared.ItemOwner;
import narl.hpclp.shared.ItemProdx;
import narl.hpclp.shared.ItemTenur;

/**
 * The prefix,'Modify' means 'insert', 'update' and 'delete'.<p>
 * @author qq
 *
 */
public class SqlDBase {

	public static ItemOwner modifyOwner(ItemOwner obj) throws IllegalArgumentException {
		if(obj.uuid.length()==0){
			//create a new one
		}else{
			//modify it!!!
			
		}
		return obj;
	}


	public static ItemTenur modifyTenur(ItemTenur obj) throws IllegalArgumentException {
		return obj;
	}

	public static ItemAccnt modifyAccnt(ItemAccnt obj) throws IllegalArgumentException {
		return obj;
	}

	
	public static ItemProdx modifyProdx(ItemProdx obj) throws IllegalArgumentException {
		return obj;
	}
}
