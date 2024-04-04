package ua.hnatiuk.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Hnatiuk Volodymyr on 27.03.2024.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhotoData {
    @JsonProperty("seoLinkF")
    private String photoLink;
}
