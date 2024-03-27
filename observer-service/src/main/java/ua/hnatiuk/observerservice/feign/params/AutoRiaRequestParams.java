package ua.hnatiuk.observerservice.feign.params;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Hnatiuk Volodymyr on 27.03.2024.
 */
@Getter
@Setter
@Builder
public class AutoRiaRequestParams {
    private Integer category_id;
    private Integer marka_id;
    private Integer model_id;
    private Integer price_ot;
    private Integer price_do;
    private Integer s_yers;
    private Integer po_yers;
    private Integer raceFrom;
    private Integer raceTo;
    private Integer gearbox;
    private Integer type;
}
