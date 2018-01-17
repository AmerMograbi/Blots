package amoor.blots;

/**
 * Created by Amoor on 12/22/2016.
 */

public class BlotViewData {
    private String shape;
    private String color;
    private int width;
    private int length;

    public BlotViewData(String color, String shape) {
        this.color = color;
        this.shape = shape;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
