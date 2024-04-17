package antigravity.controller;

import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig
@SpringBootTest
class ProductControllerTest {
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

        assertThat(productService.getProductAmount(request)).isEqualTo(response);
    }
}