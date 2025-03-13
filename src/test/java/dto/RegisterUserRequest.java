package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterUserRequest {
    @JsonProperty("firebase_id")
    private String firebaseId;

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

    @JsonProperty("position")
    private String position;

    @JsonProperty("description")
    private String description;

    @JsonProperty("skills")
    private List<String> skills;

    @JsonProperty("files")
    private Map<String, FileMetadata> files;  // ВЕРНУЛ FileMetadata!

    @JsonProperty("rate")
    private int rate;

    @JsonProperty("profile_id")
    private String profileId;

    @JsonProperty("project_ids")
    private List<String> projectIds;

    @JsonProperty("password")
    private String password;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreatedAt {
        @JsonProperty("seconds")
        private long seconds;

        @JsonProperty("nanos")
        private int nanos;
    }
}
