package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
        System.out.println("상품 가격 추출 로직을 완성 시켜주세요.");
        Product product = productRepository.getProduct(request.getProductId());
        List<Integer> requestCouponIds = Arrays.stream(request.getCouponIds()).boxed().toList();
        List<Promotion> promotionList = promotionRepository.getPromotions(requestCouponIds);

        Integer product_price = product.getPrice();
        Integer discount_price = 0;
        for (Promotion promotion : promotionList) {
            if (promotion.isApplicable()) {
                if (requestCouponIds.contains(promotion.getId())) {
                    discount_price += getPromotionDiscountPrice(product_price, promotion);
                }
            } else {
                System.out.println("쿠폰 적용에 실패 하셨습니다.");
            }
        }

        Integer final_price = product_price - discount_price;
        final_price = toDiscardLessThan1000(final_price);

        return ProductAmountResponse
                .builder()
                .name(product.getName())
                .originPrice(product_price)
                .discountPrice(discount_price)
                .finalPrice(final_price)
                .build();
    }

    public Integer getPromotionDiscountPrice(Integer product_price, Promotion promotion) {
        return promotion.getDiscount_type().getDiscountPrice(product_price, promotion.getDiscount_value());
    }

    public Integer toDiscardLessThan1000(Integer final_price) {
        return (int) (Math.floor(final_price.doubleValue() / 1000) * 1000);
    }
}
