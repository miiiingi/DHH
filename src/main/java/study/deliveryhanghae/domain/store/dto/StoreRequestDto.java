package study.deliveryhanghae.domain.store.dto;

import study.deliveryhanghae.domain.owner.entity.Owner;
import study.deliveryhanghae.domain.store.entity.Store;

public class StoreRequestDto {

    public record CreateStoreDto(String name, String businessNumber, String address, String description) {

        public Store toEntity(Owner owner, String imageUrl, String originFileName) {
            return Store.builder()
                    .owner(owner)
                    .name(name)
                    .address(address)
                    .businessNumber(businessNumber)
                    .description(description)
                    .originFileName(originFileName)
                    .imageUrl(imageUrl)
                    .build();
        };
    }

    public record UpdateStoreDto(String name, String address, String description) {

    }
}
