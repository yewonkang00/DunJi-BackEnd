package com.dungzi.backend.domain.review.dao;

import com.dungzi.backend.domain.review.domain.ReviewDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewDetailDao extends JpaRepository<ReviewDetail, UUID> {
    int countByBuildingId(UUID buildingId);

}
