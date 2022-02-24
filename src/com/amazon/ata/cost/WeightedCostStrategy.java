package com.amazon.ata.cost;

import com.amazon.ata.types.Material;
import com.amazon.ata.types.ShipmentCost;
import com.amazon.ata.types.ShipmentOption;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class WeightedCostStrategy implements CostStrategy {

    private final Map<Material, BigDecimal> materialCostPerGram;
    private MonetaryCostStrategy monetaryCostStrategy;
    private CarbonCostStrategy carbonCostStrategy;


    public WeightedCostStrategy(MonetaryCostStrategy monetaryCostStrategy, CarbonCostStrategy carbonCostStrategy) {
        materialCostPerGram = new HashMap<>();
        this.monetaryCostStrategy = monetaryCostStrategy;
        this.carbonCostStrategy = carbonCostStrategy;
        materialCostPerGram.put(Material.CORRUGATE, BigDecimal.valueOf(.8));
        materialCostPerGram.put(Material.LAMINATED_PLASTIC, BigDecimal.valueOf(.2));
    }


    @Override
    public ShipmentCost getCost(ShipmentOption shipmentOption) {

            BigDecimal monetaryCostTotal = monetaryCostStrategy.getCost(shipmentOption).getCost().multiply(BigDecimal.valueOf(0.8));

            BigDecimal carbonCostTotal = carbonCostStrategy.getCost(shipmentOption).getCost().multiply(BigDecimal.valueOf(0.2));

            BigDecimal cost = carbonCostTotal.add(monetaryCostTotal);

            return new ShipmentCost(shipmentOption, cost);
        }

}
