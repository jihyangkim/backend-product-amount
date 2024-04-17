package antigravity.domain.enumable;

import java.util.Objects;

public enum DiscountType {
    WON,
    PERCENT;

    public Integer getDiscountPrice(Integer product_price, Integer discount_value) {
        int result = 0;
        if (Objects.equals(this.name(), "WON")) {
            result = discount_value;
        } else if (Objects.equals(this.name(), "PERCENT")) {
            result = product_price / discount_value;
        }
        return result;
    }
}
