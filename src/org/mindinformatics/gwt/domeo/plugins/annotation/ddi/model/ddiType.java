package org.mindinformatics.gwt.domeo.plugins.annotation.ddi.model;

import java.io.Serializable;

import org.mindinformatics.gwt.domeo.plugins.annotation.comment.model.MCommentAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.IPharmgxOntology;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.SPLType;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
@SuppressWarnings("serial")
public class ddiType implements Serializable, IsSerializable {

	public static final String TAG = "ao:Tag";
	public static final String NOTE = "ao:Note";
	public static final String EXAMPLE = "ao:Example";
	public static final String ERRATUM = "ao:Erratum";
	public static final String COMMENT = MCommentAnnotation.TYPE;
	public static final String DESCRIPTION = "ao:Description";
	
	public static final ddiType TAG_TYPE = new ddiType("Tag", TAG);
	public static final ddiType NOTE_TYPE = new ddiType("Note", NOTE);
	public static final ddiType ERRATUM_TYPE = new ddiType("Erratum", ERRATUM);
	public static final ddiType EXAMPLE_TYPE = new ddiType("Example", EXAMPLE);
	public static final ddiType COMMENT_TYPE = new ddiType("Comment", COMMENT);
	public static final ddiType DESCRIPTION_TYPE = new ddiType("Description", DESCRIPTION);

	public static final String DDI = Iddi.BODY_TYPE; 
	
	public static final ddiType DDI_TYPE = new ddiType("ddi", DDI);
	
	
	private  String _name;
	private  String _type;
	
	public static ddiType findByName(String theTypeName){
		if(theTypeName.equals(TAG_TYPE.getName())){
			return TAG_TYPE;
		}
		if(theTypeName.equals(NOTE_TYPE.getName())){
			return NOTE_TYPE;
		}
		if(theTypeName.equals(ERRATUM_TYPE.getName())){
			return ERRATUM_TYPE;
		}
		if(theTypeName.equals(EXAMPLE_TYPE.getName())){
			return EXAMPLE_TYPE;
		}
		if(theTypeName.equals(COMMENT_TYPE.getName())){
			return COMMENT_TYPE;
		}
		if(theTypeName.equals(DESCRIPTION_TYPE.getName())){
			return DESCRIPTION_TYPE;
		}
		return null;
	}
	
	public static String typeByName(String theTypeName){
		if(theTypeName.equals(TAG_TYPE.getName())){
			return TAG;
		}
		if(theTypeName.equals(NOTE_TYPE.getName())){
			return NOTE;
		}
		if(theTypeName.equals(ERRATUM_TYPE.getName())){
			return ERRATUM;
		}
		if(theTypeName.equals(EXAMPLE_TYPE.getName())){
			return EXAMPLE;
		}
		if(theTypeName.equals(COMMENT_TYPE.getName())){
			return COMMENT;
		}
		if(theTypeName.equals(DESCRIPTION_TYPE.getName())){
			return DESCRIPTION;
		}
		return null;
	}

    public ddiType(){
        super();
    }
	private ddiType(String name, String type) {
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
