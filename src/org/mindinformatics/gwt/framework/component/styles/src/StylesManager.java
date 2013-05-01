package org.mindinformatics.gwt.framework.component.styles.src;

import java.util.HashMap;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 * 
 * This is collecting a list of stylesheet rules that can
 * be translated into proper CSS format.
 */
public class StylesManager {

	public static final String NEUTRAL = "domeo-neutral";
	public static final String EMPHASIZED = "domeo-emphasized";
	public static final String HIGHLIGHT = "domeo-highlight";
	public static final String JUST_SELECTED = "domeo-just-selected";
	public static final String IMG_ANNOTATED = "domeo-image-annotated";
	public static final String IMG_ANNOTATED_BORDER = "domeo-image-annotated-border";
	public static final String IMG_EMPHASIZED = "domeo-image-emphasized";
	public static final String IMG_EMPHASIZED_BORDER = "domeo-image-emphasized-border";
	
	public static final String LIGHTBLUE_HIGHLIGHT = "domeo-highlight-lightblue";
	public static final String LIGHTRED_HIGHLIGHT = "domeo-curated-incorrect";
	public static final String LIGHTGREEN_HIGHLIGHT = "domeo-curated-correct";
	
	
	private HashMap<String, String> styles = new HashMap<String, String>();
	
	public StylesManager() {
		// Initialization
		styles.put(NEUTRAL, "");
		styles.put("af-annotated", "border-bottom: 3px double black; z-index: 10;");
		styles.put("af-discourse", "border-bottom: 2px dotted #333333");
		styles.put("af-postit", "background-color: #66FFAA;");
		
		styles.put(EMPHASIZED, "z-index:200; background-color: red; color: white;");
		styles.put(IMG_EMPHASIZED_BORDER, "3px solid red");
		styles.put(IMG_EMPHASIZED, "border: "+ styles.get(IMG_EMPHASIZED_BORDER) + ";");
		styles.put(IMG_ANNOTATED_BORDER, "3px solid yellow");
		styles.put(IMG_ANNOTATED, "border: "+ styles.get(IMG_EMPHASIZED_BORDER) + ";");
		
		// http://robertnyman.com/2010/01/11/css-background-transparency-without-affecting-child-elements-through-rgba-and-filters/
		
		styles.put(JUST_SELECTED, "background-color: blue; color: yellow;");
		styles.put(HIGHLIGHT, "background: rgba(255, 243, 128, 0.4); padding: 0px;"); // background-color: #FFF380;
		styles.put(LIGHTBLUE_HIGHLIGHT, "background: rgba(153, 221, 255, 0.4); padding-top: 5px;padding-bottom: 5px;padding-left: 3px;padding-right: 3px;"); // background-color: #99DDFF; 
		styles.put("domeo-annotation", "background: rgba(176, 196, 222, 0.4); padding-top: 5px;padding-bottom: 5px;padding-left: 3px;padding-right: 3px;"); // background-color: #B0C4DE;
		
		styles.put(LIGHTGREEN_HIGHLIGHT, "background: rgba(153, 255, 153, 0.4); padding: 2px;"); // background-color: #99FF99;
		styles.put(LIGHTRED_HIGHLIGHT, "background: rgba(255, 102, 102, 0.4); padding: 2px;"); // background-color: #FF6666;
		
		styles.put("domeo-table-normal", "border: 2px solid transparent;");
		styles.put("domeo-table-highlight", "border: 2px dotted #ddd;");
	}
	
	/**
	 * Adding a new rule
	 * @param className	The class name of the CSS rule
	 * @param value		The rules. As for now the value has to include all the rules.
	 */
	public void addClassStyle(String className, String value) {
		if(styles.containsKey(className)) {
			styles.remove(className);
		} 
		styles.put(className, value);
	}
	
	public String getStyle(String styleName) {
		return styles.get(styleName);
	}
	
	/**
	 * Returns the textual representation of all the rules
	 * @return	The CSS text
	 */
	public String createStylesheet() {
		
		StringBuffer sb = new StringBuffer();
		for(String key: styles.keySet()) {
			sb.append(".");
			sb.append(key);
			sb.append("{");
			sb.append(styles.get(key));
			sb.append("}");
			sb.append("\n");
		}
		
		return sb.toString();
	}
}
