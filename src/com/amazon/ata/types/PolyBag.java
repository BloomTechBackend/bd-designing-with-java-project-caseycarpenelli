package com.amazon.ata.types;
import java.math.*;

import java.math.BigDecimal;
import java.util.Objects;

public class PolyBag extends Packaging {

    private Material material = Material.LAMINATED_PLASTIC;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;
    private BigDecimal volume;



    public PolyBag(Material material, BigDecimal volume) {
        this.material = material;
        this.volume = volume;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PolyBag polyBag = (PolyBag) o;
        return getVolume().equals(polyBag.getVolume());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getVolume());
    }
}
