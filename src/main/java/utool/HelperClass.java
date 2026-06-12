package utool;

public class HelperClass {

	public static boolean isUserIdValid(String userId) {
		System.out.println(userId);
		return userId != null && !userId.trim().isEmpty();
	}
	
	public static int generateRandom() {
		int randomNumber = 1000 + (int)(Math.random() * 9000); // 9000 là khoảng cách từ 1000 đến 9999
		return randomNumber;
	}

}
