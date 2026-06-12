package zzzz;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class NewFile {
    public static void main(String[] args) {
        // Đường dẫn tới file ảnh
        File file = new File("src\\main\\webapp\\image\\banner.png");

        // Kiểm tra xem file có tồn tại không
        if (file.exists()) {
            try {
                // Sử dụng Desktop để mở file
                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);
            } catch (IOException e) {
                System.out.println("Không thể mở file: " + e.getMessage());
            }
        } else {
            System.out.println("File không tồn tại: " + file.getAbsolutePath());
        }
    }
}
