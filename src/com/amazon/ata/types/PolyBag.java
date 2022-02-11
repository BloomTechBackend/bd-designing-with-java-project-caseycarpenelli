package com.amazon.ata.types;
import java.math.*;

import java.math.BigDecimal;

public class PolyBag extends Packaging {

    private Material material = Material.LAMINATED_PLASTIC;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;
    private BigDecimal volume = length.multiply(width.multiply(height));



    public PolyBag(Material material, BigDecimal length, BigDecimal width, BigDecimal height) {
        super(material, length, width, height);
    }

    public BigDecimal getMass() {
        double mass = Math.ceil(Math.sqrt(volume.doubleValue()) * 0.6);
        return BigDecimal.valueOf(mass);
    }
    public boolean canFitItem(Item item) {
    return this.volume.compareTo(item.getVolume()) > 0;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    public BigDecimal getVolume() {
        return volume;
    }
}
