package org.mindinformatics.gwt.framework.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mindinformatics.gwt.framework.component.ui.lenses.ILensComponent;
import org.mindinformatics.gwt.framework.src.IApplication;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ComponentsManager {

	private Set<IInitializableComponent> initializableComponents;
	private Set<IRefreshableComponent> refreshableComponents;
	private HashMap<Object, List<ILensComponent>> lensesListeners;
	
	IApplication _application;
	
	public ComponentsManager(IApplication application) {
		_application = application;
		initialize();
	}
	
	public void initialize() {
		initializableComponents = new HashSet<IInitializableComponent>();
		refreshableComponents = new HashSet<IRefreshableComponent>();
		lensesListeners = new HashMap<Object, List<ILensComponent>>();
	}
	
	// ------------------
	// Lenses management
	// ------------------
	public void registerObjectLens(Object obj, ILensComponent lens) {
		if(lensesListeners.containsKey(obj)) {
			if(!lensesListeners.get(obj).contains(lens)) {
				lensesListeners.get(obj).add(lens);
			}
		} else {
			ArrayList<ILensComponent> list = new ArrayList<ILensComponent>();
			list.add(lens);
			lensesListeners.put(obj, list);
		}
	}
	
	public void updateObjectLenses(Object obj) {
		for(ILensComponent lens: lensesListeners.get(obj)) {
			lens.refreshLens();
		}
	}
	
	public void unregisterObjectLenses(Object obj) {
		lensesListeners.remove(obj);
	}
	
	public void unregisterObjectLens(Object obj, ILensComponent lens) {
		lensesListeners.get(obj).remove(lens);
	}
	
	// ----------------------
	// Components management
	// ----------------------
	public void addComponent(Object obj) {
		if(obj instanceof IInitializableComponent) 
			register((IInitializableComponent)obj);
		if(obj instanceof IRefreshableComponent) 
			register((IRefreshableComponent)obj);
	}
	
	public void removeComponent(Object obj) {
		if(obj instanceof IInitializableComponent) 
			unregister((IInitializableComponent)obj);
		if(obj instanceof IRefreshableComponent) 
			unregister((IRefreshableComponent)obj);
	}
	
	public int getNumberComponents() {
		return refreshableComponents.size();
	}
	
	/*
	 * Initializable components
	 */
	public boolean register(IInitializableComponent component) {
		if(!initializableComponents.contains(component)) {
			initializableComponents.add(component);
			return true;
		} else return false;
	} 
	
	public boolean unregister(IInitializableComponent component) {
		if(!initializableComponents.contains(component)) {
			return false;
		} else {
			initializableComponents.remove(component);
			return true;
		}
	} 
	
	public void initializeComponents() {
		_application.getLogger().debug(this.getClass().getName(), "Initializing components...");
		for(IInitializableComponent component: initializableComponents) {
			_application.getLogger().debug(this.getClass().getName(), "Initializing component... " + component.getClass().getName());
			component.init();
		}
		_application.getLogger().debug(this.getClass().getName(), "Initialization components completed");
	}
	
	/*
	 * Refreshable components
	 */
	public boolean register(IRefreshableComponent component) {
		if(!refreshableComponents.contains(component)) {
			refreshableComponents.add(component);
			return true;
		} else return false;
	}
	
	public boolean unregister(IRefreshableComponent component) {
		if(!refreshableComponents.contains(component)) {
			return false;
		} else {
			refreshableComponents.remove(component);
			return true;
		}
	}
	
	public void unregisterComponent(Object component) {
		if(component instanceof IInitializableComponent) unregister((IInitializableComponent)component);
		if(component instanceof IRefreshableComponent) unregister((IRefreshableComponent)component);
	}
	
	public void refreshAllComponents() {
		_application.getLogger().debug(this, "Refreshing components...");
		for(IRefreshableComponent component: refreshableComponents) {
			_application.getLogger().debug(this, "Refreshing component " + component.getClass().getName());
			component.refresh();
		}
		_application.getLogger().debug(this.getClass().getName(), "Refreshing components completed");
	}
	
	public void refreshAnnotationComponents() {
		_application.getLogger().debug(this, "Refreshing annotation components...");
		for(IRefreshableComponent component: refreshableComponents) {
			if(component instanceof IAnnotationRefreshableComponent) {
				_application.getLogger().debug(this, "Refreshing annotation component " + component.getClass().getName());
				component.refresh();
			}
		}
		_application.getLogger().debug(this.getClass().getName(), "Refreshing annotation components completed");
	}
	
	public void refreshClipboardComponents() {
		_application.getLogger().debug(this, "Refreshing clipboard components...");
		for(IRefreshableComponent component: refreshableComponents) {
			if(component instanceof IClipboardRefreshableComponent) {
				_application.getLogger().debug(this, "Refreshing clipboard component " + component.getClass().getName());
				component.refresh();
			}
		}
		_application.getLogger().debug(this.getClass().getName(), "Refreshing clipboard components completed");
	}
	
	public void refreshResourceComponents() {
		_application.getLogger().debug(this, "Refreshing resource components...");
		for(IRefreshableComponent component: refreshableComponents) {
			if(component instanceof IResourceRefreshableComponent) {
				_application.getLogger().debug(this, "Refreshing resource component " + component.getClass().getName());
				component.refresh();
			}
		}
		_application.getLogger().debug(this.getClass().getName(), "Refreshing annotation components completed");
	}
}
