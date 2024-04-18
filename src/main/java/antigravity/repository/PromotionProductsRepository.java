package antigravity.repository;

import antigravity.domain.entity.PromotionProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionProductsRepository extends JpaRepository<PromotionProducts, Integer> {
    List<PromotionProducts> findPromotionProductsByProductId(Integer productId);
}
