package org.mindinformatics.gwt.domeo.plugins.annotation.postit.model;

import java.io.Serializable;

import org.mindinformatics.gwt.domeo.plugins.annotation.comment.model.MCommentAnnotation;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class PostitType implements Serializable, IsSerializable {

	public static final String TAG = "ao:Tag";
	public static final String NOTE = "ao:Note";
	public static final String EXAMPLE = "ao:Example";
	public static final String ERRATUM = "ao:Erratum";
	public static final String COMMENT = MCommentAnnotation.TYPE;
	public static final String DESCRIPTION = "ao:Description";
	
	public static final PostitType TAG_TYPE = new PostitType("Tag", TAG);
	public static final PostitType NOTE_TYPE = new PostitType("Note", NOTE);
	public static final PostitType ERRATUM_TYPE = new PostitType("Erratum", ERRATUM);
	public static final PostitType EXAMPLE_TYPE = new PostitType("Example", EXAMPLE);
	public static final PostitType COMMENT_TYPE = new PostitType("Comment", COMMENT);
	public static final PostitType DESCRIPTION_TYPE = new PostitType("Description", DESCRIPTION);

	private  String _name;
	private  String _type;
	
	public static PostitType findByName(String theTypeName){
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

    public PostitType(){
        super();
    }
	private PostitType(String name, String type) {
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
