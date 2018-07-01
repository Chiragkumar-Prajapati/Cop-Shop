package com.ctrlaltelite.copshop.persistence;

import com.ctrlaltelite.copshop.objects.BidObject;

import java.util.List;

public interface IBidModel {
    /**
     *
     * @param bid
     * @return
     */
    String createNew(BidObject bid);

    /**
     *
     * @param id
     * @return
     */
    boolean delete(String id);

    /**
     *
     * @param listingId
     * @return
     */
    List findByListing(String listingId);
}
