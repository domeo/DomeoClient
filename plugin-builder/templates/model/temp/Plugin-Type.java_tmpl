package org.mindinformatics.gwt.domeo.plugins.annotation.spls.model;

import java.io.Serializable;

import org.mindinformatics.gwt.domeo.plugins.annotation.comment.model.MCommentAnnotation;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
@SuppressWarnings("serial")
public class SPLType implements Serializable, IsSerializable {

	public static final String TAG = "ao:Tag";
	public static final String NOTE = "ao:Note";
	public static final String EXAMPLE = "ao:Example";
	public static final String ERRATUM = "ao:Erratum";
	public static final String COMMENT = MCommentAnnotation.TYPE;
	public static final String DESCRIPTION = "ao:Description";
	
	public static final SPLType TAG_TYPE = new SPLType("Tag", TAG);
	public static final SPLType NOTE_TYPE = new SPLType("Note", NOTE);
	public static final SPLType ERRATUM_TYPE = new SPLType("Erratum", ERRATUM);
	public static final SPLType EXAMPLE_TYPE = new SPLType("Example", EXAMPLE);
	public static final SPLType COMMENT_TYPE = new SPLType("Comment", COMMENT);
	public static final SPLType DESCRIPTION_TYPE = new SPLType("Description", DESCRIPTION);

	private  String _name;
	private  String _type;
	
	public static SPLType findByName(String theTypeName){
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
