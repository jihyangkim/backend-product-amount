package antigravity.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class PromotionProducts {
    @Id
    private int id;
    private int promotionId;
    private int productId;
}
