import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageHandler extends JPanel {
    BufferedImage image;
   public ImageHandler() throws IOException {
        image = ImageIO.read(new File("C:\\Users\\Alex\\Downloads\\img1_low_contrast.jpg"));
    }

    public BufferedImage ge
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image,0,0,this);
    }
    public static void main(String[]args) throws IOException {
        JFrame jFrame = new JFrame("s");
        ImageHandler imageHandler = new ImageHandler();
        jFrame.add(imageHandler);
        jFrame.setSize(1000,1000);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
    }
}

