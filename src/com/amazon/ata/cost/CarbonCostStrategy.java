package com.amazon.ata.cost;

import com.amazon.ata.types.Box;
import com.amazon.ata.types.Material;
import com.amazon.ata.types.Packaging;
import com.amazon.ata.types.ShipmentCost;
import com.amazon.ata.types.ShipmentOption;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CarbonCostStrategy implements CostStrategy {

    private static final BigDecimal CARBON_UNITS_PER_GRAM_CORRUGATE = new BigDecimal("0.017");
    private static final BigDecimal CARBON_UNITS_PER_GRAM_LAMINATED_PLASTIC = new BigDecimal("0.012");
    private final Map<Material, BigDecimal> materialCostPerGram;

    public CarbonCostStrategy() {
        materialCostPerGram = new HashMap<>();
        materialCostPerGram.put(Material.CORRUGATE, CARBON_UNITS_PER_GRAM_CORRUGATE);
        materialCostPerGram.put(Material.LAMINATED_PLASTIC, CARBON_UNITS_PER_GRAM_LAMINATED_PLASTIC);
    }





    @Override
    public ShipmentCost getCost(ShipmentOption shipmentOption) {
        Packaging packaging = shipmentOption.getPackaging();
        BigDecimal materialCost = this.materialCostPerGram.get(packaging.getMaterial());

        BigDecimal cost = packaging.getMass().multiply(materialCost);

        return new ShipmentCost(shipmentOption, cost);
    }
}
