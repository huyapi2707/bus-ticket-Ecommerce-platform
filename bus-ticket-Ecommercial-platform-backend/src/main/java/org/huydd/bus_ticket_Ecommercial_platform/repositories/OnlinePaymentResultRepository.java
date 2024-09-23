package org.huydd.bus_ticket_Ecommercial_platform.repositories;

import org.huydd.bus_ticket_Ecommercial_platform.pojo.OnlinePaymentResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OnlinePaymentResultRepository extends JpaRepository<OnlinePaymentResult, Long> {
    Optional<OnlinePaymentResult> findByPaymentCode(String paymentCode);
}
