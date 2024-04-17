package antigravity.repository;

import antigravity.domain.entity.Promotion;
import antigravity.domain.enumable.DiscountType;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class PromotionRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Promotion> getPromotions(List<Integer> ids) {
        String query = "SELECT * FROM `promotion` WHERE id in (:ids) ";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", ids);

        return namedParameterJdbcTemplate.query(
                query,
                params,
                (rs, rowNum) -> Promotion.builder()
                        .id(rs.getInt("id"))
                        .promotion_type(rs.getString("promotion_type"))
                        .name(rs.getString("name"))
                        .discount_type(DiscountType.valueOf(rs.getString("discount_type")))
                        .discount_value(rs.getInt("discount_value"))
                        .use_started_at(rs.getDate("use_started_at"))
                        .use_ended_at(rs.getDate("use_ended_at"))
                        .build()
        );
    }
}
