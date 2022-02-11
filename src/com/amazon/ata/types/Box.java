package com.amazon.ata.types;

import java.math.BigDecimal;

public class Box extends Packaging {

    private Material material = Material.CORRUGATE;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;

    public Box(Material material, BigDecimal length, BigDecimal width, BigDecimal height) {
        super(material, length, width, height);
    }

    public boolean canFitItem(Item item) {
        return this.length.compareTo(item.getLength()) > 0 &&
                this.width.compareTo(item.getWidth()) > 0 &&
                this.height.compareTo(item.getHeight()) > 0;
    }

    public BigDecimal getMass() {
        BigDecimal two = BigDecimal.valueOf(2);

        // For simplicity, we ignore overlapping flaps
        BigDecimal endsArea = length.multiply(width).multiply(two);
        BigDecimal shortSidesArea = length.multiply(height).multiply(two);
        BigDecimal longSidesArea = width.multiply(height).multiply(two);

        return endsArea.add(shortSidesArea).add(longSidesArea);
    }
}
