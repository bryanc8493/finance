package domain.beans;

import java.io.Serializable;

public class UpdatedRecord implements Serializable {

	private static final long serialVersionUID = -8766078026264736912L;

	public UpdatedRecord(){}

	private String ID;
	private String attribute;
	private String data;
	
	@Override
	public String toString(){
		return "ID: " + this.ID + "\tColumn: " + this.attribute + "\tUpdated Value: " + this.data;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
