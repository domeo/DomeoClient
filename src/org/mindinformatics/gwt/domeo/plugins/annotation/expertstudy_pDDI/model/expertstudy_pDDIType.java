package org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model;

import java.io.Serializable;

import org.mindinformatics.gwt.domeo.plugins.annotation.comment.model.MCommentAnnotation;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
@SuppressWarnings("serial")
public class expertstudy_pDDIType implements Serializable, IsSerializable {

	public static final String TAG = "ao:Tag";
	public static final String NOTE = "ao:Note";
	public static final String EXAMPLE = "ao:Example";
	public static final String ERRATUM = "ao:Erratum";
	public static final String COMMENT = MCommentAnnotation.TYPE;
	public static final String DESCRIPTION = "ao:Description";
	
	public static final expertstudy_pDDIType TAG_TYPE = new expertstudy_pDDIType("Tag", TAG);
	public static final expertstudy_pDDIType NOTE_TYPE = new expertstudy_pDDIType("Note", NOTE);
	public static final expertstudy_pDDIType ERRATUM_TYPE = new expertstudy_pDDIType("Erratum", ERRATUM);
	public static final expertstudy_pDDIType EXAMPLE_TYPE = new expertstudy_pDDIType("Example", EXAMPLE);
	public static final expertstudy_pDDIType COMMENT_TYPE = new expertstudy_pDDIType("Comment", COMMENT);
	public static final expertstudy_pDDIType DESCRIPTION_TYPE = new expertstudy_pDDIType("Description", DESCRIPTION);

	private  String _name;
	private  String _type;
	
	public static expertstudy_pDDIType findByName(String theTypeName){
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

    public expertstudy_pDDIType(){
        super();
    }
	private expertstudy_pDDIType(String name, String type) {
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
