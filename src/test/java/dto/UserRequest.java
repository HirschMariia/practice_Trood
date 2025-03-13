package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequest {
    @JsonProperty("id")
    private String id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("job_title")
    private String jobTitle;

    @JsonProperty("address")
    private String address;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("photo")
    private String photo;

    @JsonProperty("pitch_product")
    private String pitchProduct;

    @JsonProperty("profile_id")
    private String profileId;

    @JsonProperty("position")
    private String position;

    @JsonProperty("description")
    private String description;

    @JsonProperty("skills")
    private List<String> skills;

    @JsonProperty("rate")
    private int rate;

    @JsonProperty("project_ids")
    private List<String> projectIds;

    @JsonProperty("created_at")
    private RegisterUserRequest.CreatedAt createdAt;

    @JsonProperty("updated_at")
    private RegisterUserRequest.CreatedAt updatedAt;
}

