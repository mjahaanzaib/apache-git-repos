package org.acme.dto;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReleaseInformation {

	private int id;

	@JsonProperty("tag_name")
	private String tagName;

	private ArrayList<Asset> assets;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public ArrayList<Asset> getAssets() {
		return assets;
	}

	public void setAssets(ArrayList<Asset> assets) {
		this.assets = assets;
	}

	@Override
	public String toString() {
		String str = new String();
		if (assets != null && assets.size() > 0) {
			str = assets.get(0).getDownloadCount()+"";
		} else {
			str = "not Found";
		}
		return "ReleaseInformation [id=" + id + ", tagName=" + tagName + ", downlaodCount=" + str + "]";
	}
}
