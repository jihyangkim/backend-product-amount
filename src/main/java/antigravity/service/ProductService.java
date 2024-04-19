package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;
import antigravity.exception.ValidException;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionProductsRepository;
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
    private final PromotionProductsRepository promotionProductsRepository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
        System.out.println("상품 가격 추출 로직을 완성 시켜주세요.");
        Integer requestProductId = request.getProductId();
        Product product = productRepository.findProductById(requestProductId);
        List<Integer> requestCouponIds = Arrays.stream(request.getCouponIds()).boxed().toList();
        List<Promotion> promotionList = promotionRepository.findPromotionsByIdIn(requestCouponIds);
        List<PromotionProducts> promotionProductsList = promotionProductsRepository.findPromotionProductsByProductId(requestProductId);

        Integer product_price = product.getPrice();
        Integer discount_price = 0;
        for (Promotion promotion : promotionList) {
            if (!checkExistProductPromotion(promotionProductsList, promotion.getId())) {
                System.out.println("해당하는 쿠폰이 존재하지 않습니다.");
                continue;
            }
            if (promotion.isApplicableDate()) {
                discount_price += getPromotionDiscountPrice(product_price, promotion);
            } else {
                System.out.println("쿠폰 적용 기한이 지났습니다.");
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
        return promotion.getDiscountType().getDiscountPrice(product_price, promotion.getDiscountValue());
    }

    public Integer toDiscardLessThan1000(Integer final_price) {
        try {
            checkValidPrice(final_price);
            return (int) (Math.floor(final_price.doubleValue() / 1000) * 1000);
        } catch (ValidException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean checkExistProductPromotion(List<PromotionProducts> promotionProductsList, Integer promotionId) {
        List<Integer> promotionIdList = promotionProductsList.stream()
                .map(PromotionProducts::getPromotionId)
                .toList();
        return promotionIdList.contains(promotionId);
    }

    static void checkValidPrice(Integer price) throws ValidException {
        if (price < 10000) {
            throw new ValidException("최소 상품가격은 ₩10,000 입니다.");
        } else if (price > 10000000) {
            throw new ValidException("최대 상품가격은 ₩10,000,000 입니다.");
        }
    }
}
