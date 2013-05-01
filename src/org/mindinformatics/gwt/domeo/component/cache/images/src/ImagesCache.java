package org.mindinformatics.gwt.domeo.component.cache.images.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.component.cache.images.model.ImageProxy;
import org.mindinformatics.gwt.framework.component.IInitializableComponent;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ImagesCache implements IInitializableComponent {

	private HashMap<String, ArrayList<ImageProxy>> cache;

	private IDomeo _domeo;
	
	public ImagesCache(IDomeo domeo) {
		_domeo = domeo;
		init();
	}
	
	public void cacheImage(ImageProxy image) {
		_domeo.getLogger().debug(this, "Caching image " + image.getSrc());
		if(cache.containsKey(image.getSrc())) {
			cache.get(image.getSrc()).add(image);
		} else {
			ArrayList<ImageProxy> images = new ArrayList<ImageProxy>();
			images.add(image);
			cache.put(image.getSrc(), images);
		}
	}
	
	
	public Set<String> getKeys() {
		return cache.keySet();
	}
	
	public ArrayList<ImageProxy> getValue(String key) {
		return cache.get(key);
	}
	
	public int getSize() {
		return cache.size();
	}

	@Override
	public void init() {
		cache = new HashMap<String, ArrayList<ImageProxy>>();
	}
}
