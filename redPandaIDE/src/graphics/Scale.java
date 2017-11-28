package graphics;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Scanner;

public class Scale {

    private double scale;
    private final int screenWidth; 
    private final int screenHeight; 
    private int windowWidth; 
    private int windowHeight; 
    
    /* 
    Initialize the scale according to the size of the screen
    */
    public Scale() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.screenWidth = (int) screenSize.getWidth();
        this.screenHeight = (int) screenSize.getHeight();
        this.scale = 0.5;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public double getScale() {
        return scale;
    }

    public double getScreenWidth() {
        return screenWidth;
    }

    public double getScreenHeight() {
        return screenHeight;
    }

    public int getWindowWidth(int windowWidthPercent) {
        this.windowWidth = this.screenWidth * windowWidthPercent;
        return windowWidth /100;
    }

    public int getWindowHeight(int windowHeightPercent) {
        this.windowHeight = this.screenHeight * windowHeightPercent;
        return windowHeight / 100;
    }
    
    public static void main(String[] args) {
        Scale scale = new Scale(); 
        System.out.println("Ancho: "+scale.getScreenWidth());
        System.out.println("Alto:" +scale.getScreenHeight());
        
        Scanner scanner = new Scanner(System.in);
        int windowWidthPercent = scanner.nextInt();
        int windowHeightPercent = scanner.nextInt();
        
        System.out.println("Ancho ventana: "+scale.getWindowWidth(windowWidthPercent));
        System.out.println("Alto ventana: "+scale.getWindowHeight(windowHeightPercent));
    }
}
