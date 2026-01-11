package dev.khondamir.onlinelib;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Book(
        Long id,

        @NotBlank
        @Size(max = 30)
        String name,

        @NotBlank
        @Size(max = 30)
        String authonName,

        @Min(0)
        @NotNull
        @JsonProperty("pubYear")
        Integer publicationYear,

        @Min(0)
        @NotNull
        @JsonProperty("pageNum")
        Integer pageNumber,

        @Min(0)
        @Max(10000)
        @NotNull
        Integer cost
) {
}
