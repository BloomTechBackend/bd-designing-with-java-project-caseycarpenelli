package com.amazon.ata.dao;

import com.amazon.ata.datastore.PackagingDatastore;
import com.amazon.ata.exceptions.NoPackagingFitsItemException;
import com.amazon.ata.exceptions.UnknownFulfillmentCenterException;
import com.amazon.ata.types.FcPackagingOption;
import com.amazon.ata.types.FulfillmentCenter;
import com.amazon.ata.types.Item;
import com.amazon.ata.types.Packaging;
import com.amazon.ata.types.ShipmentOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Access data for which packaging is available at which fulfillment center.
 */
public class PackagingDAO {
    /**
     * A list of fulfillment centers with a packaging options they provide.
     */
//    private List<FcPackagingOption> fcPackagingOptions;
    private Map<FulfillmentCenter, Set<FcPackagingOption>> fcPackagingOptions;
    private FulfillmentCenter fulfillmentCenter;

//    public PackagingDAO(HashMap<FulfillmentCenter, HashSet<FcPackagingOption>> packageMap, FulfillmentCenter fulfillmentCenter) {
//        this.fulfillmentCenter = fulfillmentCenter;
//        for (FcPackagingOption fcPackagingOption : fcPackagingOptions) {
//            if (fcPackagingOption.getPackaging().hashCode() != packageMap.get(fulfillmentCenter.getFcCode()).hashCode()) {
//                packageMap.put(fulfillmentCenter, new HashSet<FcPackagingOption>());
//            } else {
//                System.out.println("Duplicate detected");
//            }



    /**
     * Instantiates a PackagingDAO object.
     * @param datastore Where to pull the data from for fulfillment center/packaging available mappings.
     */
    public PackagingDAO(PackagingDatastore datastore) {
        fcPackagingOptions =  new HashMap<FulfillmentCenter, Set<FcPackagingOption>>();

                for (FcPackagingOption  currentPackagingOption : datastore.getFcPackagingOptions()) {
                    //if the map doesn't have an entry for the fulfillment center
                    //are going to add a fulfilment center as a key and the packaging options for the set.
                    // if the map already has a fulfillment im going to get the current set and add to it.
                    //and put the set back into the map.
//                    System.out.println("this is the " + currentPackagingOption.getFulfillmentCenter() + " hashcode " + currentPackagingOption.hashCode());
                    if (!fcPackagingOptions.containsKey(currentPackagingOption.getFulfillmentCenter())) {
                        Set<FcPackagingOption> fcPackagingOptionSet = new HashSet<>();
                        fcPackagingOptionSet.add(currentPackagingOption);
                        fcPackagingOptions.put(currentPackagingOption.getFulfillmentCenter(), fcPackagingOptionSet);
                    } else {
                        Set<FcPackagingOption> fcPackagingOptionSet2 = fcPackagingOptions.get(currentPackagingOption.getFulfillmentCenter());
                        fcPackagingOptionSet2.add(currentPackagingOption);

                    }
    } // end of for loop

}
    /**
     * Returns the packaging options available for a given item at the specified fulfillment center. The API
     * used to call this method handles null inputs, so we don't have to.
     *
     * @param item the item to pack
     * @param fulfillmentCenter fulfillment center to fulfill the order from
     * @return the shipping options available for that item; this can never be empty, because if there is no
     * acceptable option an exception will be thrown
     * @throws UnknownFulfillmentCenterException if the fulfillmentCenter is not in the fcPackagingOptions list
     * @throws NoPackagingFitsItemException if the item doesn't fit in any packaging at the FC
     */
    public List<ShipmentOption> findShipmentOptions(Item item, FulfillmentCenter fulfillmentCenter)
            throws UnknownFulfillmentCenterException, NoPackagingFitsItemException {

        // Check all FcPackagingOptions for a suitable Packaging in the given FulfillmentCenter
        List<ShipmentOption> result = new ArrayList<>();
        boolean fcFound = false;
        try {
            for (FcPackagingOption fcPackagingOption : fcPackagingOptions.get(fulfillmentCenter)) {
                Packaging packaging = fcPackagingOption.getPackaging();
                String fcCode = fcPackagingOption.getFulfillmentCenter().getFcCode();

                if (fcCode.equals(fulfillmentCenter.getFcCode())) {
                    fcFound = true;
                    if (packaging.canFitItem(item)) {
                        result.add(ShipmentOption.builder()
                                .withItem(item)
                                .withPackaging(packaging)
                                .withFulfillmentCenter(fulfillmentCenter)
                                .build());
                    }
                }
            }
        } catch (NullPointerException e) {
            System.out.println("Null fulfillment center code found");
        }


        // Notify caller about unexpected results
        if (!fcFound) {
            throw new UnknownFulfillmentCenterException(
                    String.format("Unknown FC: %s!", fulfillmentCenter.getFcCode()));
        }

        if (result.isEmpty()) {
            throw new NoPackagingFitsItemException(
                    String.format("No packaging at %s fits %s!", fulfillmentCenter.getFcCode(), item));
        }

        return result;
    }
}
