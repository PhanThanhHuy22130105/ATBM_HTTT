package models;

public class NGK {
	String MaNGK;
	String tenNGK;
	String QuyCach;
	String MaLoaiNGK;
	
	public NGK(String maNGK, String tenNGK, String quyCach, String maLoaiNGK) {
		super();
		MaNGK = maNGK;
		this.tenNGK = tenNGK;
		QuyCach = quyCach;
		MaLoaiNGK = maLoaiNGK;
	}
	public String getMaNGK() {
		return MaNGK;
	}
	public void setMaNGK(String maNGK) {
		MaNGK = maNGK;
	}
	public String getTenNGK() {
		return tenNGK;
	}
	public void setTenNGK(String tenNGK) {
		this.tenNGK = tenNGK;
	}
	public String getQuyCach() {
		return QuyCach;
	}
	public void setQuyCach(String quyCach) {
		QuyCach = quyCach;
	}
	public String getMaLoaiNGK() {
		return MaLoaiNGK;
	}
	public void setMaLoaiNGK(String maLoaiNGK) {
		MaLoaiNGK = maLoaiNGK;
	}
	
	
	
}
