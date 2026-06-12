package models;

import java.util.ArrayList;

public class User {
	private int id; // ID người dùng
	private String username; // Tên đăng nhập
	private String password; // Mật khẩu
	private String email; // Địa chỉ email
	private String address; // Địa chỉ
	private String phone; // Số điện thoại
	private String Img = "image/avatars/default-avatar.png";
	private boolean isAdmin;
	private ArrayList<Product> FavoriteProducts ;

	// Constructor
	public User() {}

	// Getter và Setter

	public void setImg (String path) {
		this.Img = path;
	}

	public String getImg () {
		return this.Img;
	}

	public boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	  public boolean isAdmin() {
	        return this.isAdmin; // Phương thức kiểm tra quyền admin
	  }
	  
	  public boolean isAdmin(User user) {
		  if(user.getEmail()=="admin@exmaple.com")
	        return true; // Phương thức kiểm tra quyền admin
		  return false;
	  }
	  
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setFavoriteProducts(ArrayList<Product> arrayList) {
		this.FavoriteProducts = arrayList;

	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email
				+ ", address=" + address + ", phone=" + phone + ", Img=" + Img + ", isAdmin=" + isAdmin
				+ ", FavoriteProducts=" + FavoriteProducts + "]";
	}





}
