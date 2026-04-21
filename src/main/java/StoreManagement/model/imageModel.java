package StoreManagement.model;

import StoreManagement.Utility.ImageUtil;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class imageModel {
    private static final int DEFAULT_WIDTH = 250;
    private static final int DEFAULT_HEIGHT = 250;
    private static final String DEFAULT_IMAGE_PATH = "src/main/resources/assets/img/";
    private String Img;
    private String path;
    private String fileExtension;
    private int width;
    private int height;

    public imageModel(String path, int width, int height) {
        this.path = path;
        this.fileExtension = getFileExtension(path);
        this.width = width;
        this.height = height;
    }

    public imageModel(String Img, String path) {
        this.Img = Img;
        this.path = path;
        this.fileExtension = getFileExtension(Img);
        this.width = DEFAULT_WIDTH;
        this.height = DEFAULT_HEIGHT;
    }

    public static String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        // Check for edge cases: no dot, or dot is the first character
        if (lastIndexOfDot == -1 || lastIndexOfDot == 0 || lastIndexOfDot == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(lastIndexOfDot + 1);
    }

    public byte[] getImageData() {

        File image = new File(path);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage originalImg = ImageIO.read(image);
            if (originalImg == null) {
                System.err.println("Failed to read image: " + path);
                return null;
            }
            ImageIO.write(originalImg, fileExtension, baos);
            return baos.toByteArray(); // Return the image data bytes Array
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ImageIcon loadImageFromPath() {
        try {
            ImageIcon loadedIcon = new ImageIcon(path != null ? path : DEFAULT_IMAGE_PATH);
            if (loadedIcon.getImage() == null || loadedIcon.getIconWidth() <= 0) {
                return null;
            }
            Image img = loadedIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ImageIcon loadImageFromDatabase(byte[] imageData) {
        if (imageData == null || imageData.length == 0) {
            return null;
        }
        ImageIcon icon = ImageUtil.bytesToImageIcon(imageData, width, height);
        if (icon == null || icon.getImage() == null) {
            return null;
        }
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    // Getters and Setters
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String Img) {
        this.Img = Img;
    }
}