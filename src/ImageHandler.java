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

    public BufferedImage getGrayImage(BufferedImage b) {
        BufferedImage grayImage = new BufferedImage(b.getWidth(), b.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < grayImage.getWidth(); x++) {
            for (int y = 0; y < grayImage.getHeight(); y++) {
                Color c = new Color(b.getRGB(x, y));
                int gray = calculateBrightness(c);
                Color grayColor = new Color(gray, gray, gray);
                grayImage.setRGB(x, y, grayColor.getRGB());
            }
        }
        return grayImage;
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        Image tempImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resizedBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedBufferedImage.createGraphics();
        g2d.drawImage(tempImage, 0, 0, null);
        g2d.dispose();
        return resizedBufferedImage;
    }

    public static int calculateBrightness(Color color) {
        return (int) (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue());
    }

    public static int applySawtoothContrast(int value, int period, double phase) {
        double newValue = 0.5 * (1 + Math.sin((2 * Math.PI / period) * value + phase));
        return (int) (newValue * 255);
    }

    public static BufferedImage applySawtoothContrast(BufferedImage img, int period, double phase) {
        int width = img.getWidth();
        int height = img.getHeight();

        BufferedImage newImage = new BufferedImage(width, height, img.getType());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color originalColor = new Color(img.getRGB(x, y));

                int newRed = applySawtoothContrast(originalColor.getRed(), period, phase);
                int newGreen = applySawtoothContrast(originalColor.getGreen(), period, phase);
                int newBlue = applySawtoothContrast(originalColor.getBlue(), period, phase);

                newRed = Math.min(255, Math.max(0, newRed));
                newGreen = Math.min(255, Math.max(0, newGreen));
                newBlue = Math.min(255, Math.max(0, newBlue));

                Color newColor = new Color(newRed, newGreen, newBlue);
                newImage.setRGB(x, y, newColor.getRGB());
            }
        }

        return newImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(resizeImage(image, 200, 200), 300, 0, this);
        g.drawImage(getGrayImage(resizeImage(image, 200, 200)), 0, 0, this);
        g.drawImage(applySawtoothContrast(resizeImage(image, 200, 200), 50, 1), 600, 0, this);
    }

    public static void main(String[] args) throws IOException {
        JFrame jFrame = new JFrame("Image Processing");
        ImageHandler imageHandler = new ImageHandler();
        jFrame.add(imageHandler);
        jFrame.setSize(1000, 1000);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
    }
}
