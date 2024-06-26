package antigravity.service;

import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;
import antigravity.domain.enumable.DiscountType;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig
@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @Test
    void getProductAmount() {
        ProductInfoRequest request = ProductInfoRequest
                .builder()
                .productId(1)
                .couponIds(new int[]{1, 2})
                .build();

        ProductAmountResponse response = ProductAmountResponse
                .builder()
                .name("피팅노드상품")
                .originPrice(215000)
                .discountPrice(0)
                .finalPrice(215000)
                .build();

        ProductAmountResponse result;

        result = productService.getProductAmount(request);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void getPromotionDiscountPrice() {
        Integer product_price = 21500;
        Date now = new Date();
        Promotion promotion = new Promotion();
        promotion.setId(1);
        promotion.setPromotionType("COUPON");
        promotion.setDiscountType(DiscountType.WON);
        promotion.setDiscountValue(2000);
        promotion.setUseStartedAt(now);
        promotion.setUseEndedAt(now);

        Integer result = productService.getPromotionDiscountPrice(product_price, promotion);

        assertThat(result).isEqualTo(2000);
    }

    @Test
    void setScale() {
        Integer final_price = 213456;
        Integer result = productService.toDiscardLessThan1000(final_price);
        assertThat(result).isEqualTo(213000);
    }

    @Test
    void checkExistProductPromotion() {
        PromotionProducts promotionProducts = new PromotionProducts();
        promotionProducts.setId(1);
        promotionProducts.setProductId(1);
        promotionProducts.setPromotionId(1);
        ArrayList<PromotionProducts> promotionProductsList = new ArrayList<>();
        promotionProductsList.add(promotionProducts);
        Integer promotionId = 1;

        Boolean result = productService.checkExistProductPromotion(promotionProductsList, promotionId);
        assertThat(result).isEqualTo(true);
    }
}