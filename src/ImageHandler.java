import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageHandler extends JPanel {
    BufferedImage image;
   public ImageHandler() throws IOException {
        image = ImageIO.read(new File(""));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
    public static void main(String[]args) throws IOException {
        JFrame jFrame = new JFrame("s");
        ImageHandler imageHandler = new ImageHandler();
        jFrame.add(imageHandler);
        jFrame.setSize(500,500);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
    }
}

