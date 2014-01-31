package org.mindinformatics.gwt.domeo.plugins.annotation.spls.model;

import java.io.Serializable;

import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.IPharmgxOntology;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
@SuppressWarnings("serial")
public class SPLType implements Serializable, IsSerializable {

	public static final String PHARMGX = IPharmgxOntology.BODY_TYPE; 
					
	public static final SPLType PHARMGX_TYPE = new SPLType("Pharmgx", PHARMGX);
	
	private  String _name;
	private  String _type;
	
	public static SPLType findByName(String theTypeName){
		if(theTypeName.equals(PHARMGX_TYPE.getName())){
			return PHARMGX_TYPE;
		}
		return null;
	}
	
	public static String typeByName(String theTypeName){
		if(theTypeName.equals(PHARMGX_TYPE.getName())){
			return PHARMGX;
		}
		return null;
	}

    public SPLType(){
        super();
    }
	private SPLType(String name, String type) {
		_name = name;
		_type = type;
	}
	public String getName(){
		return this._name;
	}
	
	public String toString() {
		return _type;
	}
}
