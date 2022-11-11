package org.deco.gachicoding.user.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(of = "userEmail")
@NoArgsConstructor
@Embeddable
public class UserEmail {

    @NotBlank @Email // @Email은 null, "", " "값을 유효하다고 판단함
    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;

    public UserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public String toString() {
        return "UserEmail{" +
                "userEmail='" + userEmail + '\'' +
                '}';
    }
}
