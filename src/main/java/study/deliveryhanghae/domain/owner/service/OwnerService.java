package study.deliveryhanghae.domain.owner.service;

import org.springframework.stereotype.Service;
import study.deliveryhanghae.domain.order.repository.OrderRepository;
import study.deliveryhanghae.domain.owner.repository.OwnerRepository;

@Service
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final OrderRepository orderRepository;

    public OwnerService(OwnerRepository ownerRepository, OrderRepository orderRepository) {
        this.ownerRepository = ownerRepository;
        this.orderRepository = orderRepository;
    }

    public int getOwnerPoint(Long ownerID){
        return ownerRepository.findPointById(ownerID);
    }
}
