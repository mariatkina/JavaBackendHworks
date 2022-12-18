package Lesson5HW;

import com.fasterxml.jackson.annotation.*;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonInclude;
import lombok.*;
import java.util.HashMap;
import java.util.Map;
@SuppressWarnings("ALL")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@With
@NoArgsConstructor
@AllArgsConstructor

public class DtoProduct {

    @JsonProperty("id")
        private Integer id;
        @JsonProperty("title")
        private String title;
        @JsonProperty("price")
        private Integer price;
        @JsonProperty("categoryTitle")
        private String categoryTitle;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();


}

