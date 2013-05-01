package org.mindinformatics.gwt.domeo.model.accesscontrol;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.framework.component.IInitializableComponent;
import org.mindinformatics.gwt.framework.model.users.IUserGroup;

public class AnnotationAccessManager implements IInitializableComponent {

	public static final String PUBLIC = "urn:domeo:access:public";
	public static final String GROUPS = "urn:domeo:access:groups";

	private IDomeo _domeo;
	
	private HashMap<MAnnotationSet, String> annotationsSetsAccess =
		new  HashMap<MAnnotationSet, String>();
	private HashMap<MAnnotationSet, Set<IUserGroup>> annotationsSetsGroupsAccess =
		new  HashMap<MAnnotationSet, Set<IUserGroup>>();

	public AnnotationAccessManager(IDomeo domeo) {
		_domeo = domeo;
	}
	
	@Override
	public void init() {
		annotationsSetsAccess = new  HashMap<MAnnotationSet, String>();
		annotationsSetsGroupsAccess = new HashMap<MAnnotationSet, Set<IUserGroup>>();
	}
	
	public boolean setAnnotationSetAccess(MAnnotationSet set, String value) {
		if(annotationsSetsAccess.containsKey(set)) {
			annotationsSetsAccess.put(set, value);
			return true;
		} else {
			annotationsSetsAccess.put(set, value);
			return false;
		}
	}
	
	public void clearAnnotationSet(MAnnotationSet set) {
		annotationsSetsAccess.remove(set);
		annotationsSetsGroupsAccess.remove(set);
	}
	
	public Collection<IUserGroup> getAnnotationSetAccessGroups(MAnnotationSet set) {
		return annotationsSetsGroupsAccess.get(set);
	}
	
	public String getAnnotationSetAccessType(MAnnotationSet set) {
		return annotationsSetsAccess.get(set);
	}
	
	private void resetAnnotationSetGroups(MAnnotationSet set) {
		annotationsSetsGroupsAccess.remove(set);
	}
	
	private void setAnnotationSetsGroups(MAnnotationSet set, Set<IUserGroup> groups) {
		annotationsSetsGroupsAccess.put(set, groups);
	}
	
	public void setAnnotationSetPublic(MAnnotationSet set) {
		annotationsSetsAccess.put(set, PUBLIC);
		resetAnnotationSetGroups(set);
	}
	
	public void setAnnotationSetGroups(MAnnotationSet set,  Set<IUserGroup> groups) {
		annotationsSetsAccess.put(set, GROUPS);
		setAnnotationSetsGroups(set, groups);
	}
	
	public void clearAnnotaitonSetGroups(MAnnotationSet set) {
		annotationsSetsGroupsAccess.remove(set);
	}
	
	public void setAnnotationSetPrivate(MAnnotationSet set) {
		annotationsSetsAccess.put(set, _domeo.getAgentManager().getUserPerson().getUri());
	}
	
	public boolean isAnnotationSetPrivate(MAnnotationSet set) {
		return getAnnotationSetAccess(set).equals(_domeo.getAgentManager().getUserPerson().getUri());
	}
	
	public boolean isAnnotationSetPublic(MAnnotationSet set) {
		return getAnnotationSetAccess(set).equals(PUBLIC);
	}
	
	public boolean isAnnotationSetGroups(MAnnotationSet set) {
		return getAnnotationSetAccess(set).equals(GROUPS);
	}
	
	public String getAnnotationSetAccess(MAnnotationSet set) {
		return annotationsSetsAccess.get(set);
	}
	
	public Set<IUserGroup> getAnnotationSetGroups(MAnnotationSet set) {
		return annotationsSetsGroupsAccess.get(set);
	}
	
}
