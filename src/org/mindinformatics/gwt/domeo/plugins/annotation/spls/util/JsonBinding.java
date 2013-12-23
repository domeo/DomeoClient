package org.mindinformatics.gwt.domeo.plugins.annotation.spls.util;

public class JsonBinding {

	private String name;
	private String value;

	public JsonBinding(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public boolean equals(Object binding) {
		if (!(binding instanceof JsonBinding))
			return false;

		final JsonBinding jb = (JsonBinding) binding;

		if (!jb.getName().equals(this.getName()))
			return false;

		if (!jb.getValue().equals(this.getValue()))
			return false;

		return true;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
