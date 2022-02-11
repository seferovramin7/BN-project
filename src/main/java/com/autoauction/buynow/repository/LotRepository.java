package com.autoauction.buynow.repository;

import com.autoauction.buynow.model.BnArchive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LotRepository extends JpaRepository<BnArchive, Long> {
    BnArchive findByLot(String lot);

    BnArchive findByLotAndAndBuyNow(String lot, Integer price);

}
