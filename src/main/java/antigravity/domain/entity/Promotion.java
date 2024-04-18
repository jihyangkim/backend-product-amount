package antigravity.domain.entity;

import antigravity.domain.enumable.DiscountType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
public class Promotion {
    @Id
    private int id;
    private String promotionType; //쿠폰 타입 (쿠폰, 코드)
    private String name;
    @Enumerated(EnumType.STRING) private DiscountType discountType; // WON : 금액 할인, PERCENT : %할인
    private int discountValue; // 할인 금액 or 할인 %
    private Date useStartedAt; // 쿠폰 사용가능 시작 기간
    private Date useEndedAt; // 쿠폰 사용가능 종료 기간

    public Boolean isApplicableDate() {
        Date now = new Date();
        return useStartedAt.before(now) && useEndedAt.after(now);
    }
}
