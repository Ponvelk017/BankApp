package details;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CustomerDetails extends UserDetails implements Serializable {
	private String address;
	private String aadhar;
	private String pan;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAadhar() {
		return aadhar;
	}

	public void setAadhar(String aadhar) {
		this.aadhar = aadhar;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}
}
