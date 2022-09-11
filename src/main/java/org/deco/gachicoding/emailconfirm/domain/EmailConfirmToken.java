package org.deco.gachicoding.emailconfirm.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;


@DynamicInsert
@Getter
@NoArgsConstructor
@Entity
public class EmailConfirmToken {

    private static final long EMAIL_TOKEN_EXPIRATION_TIME_VALUE = 5L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID tokenId;

    @Column(nullable = false)
    private String targetEmail;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean confirmed;

    private EmailConfirmToken(
            String targetEmail,
            LocalDateTime expiredAt
    ) {

        this.targetEmail = targetEmail;
        this.expiredAt = expiredAt;
    }

    public static EmailConfirmToken createEmailConfirmToken(
            String targetEmail
    ) {

        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(EMAIL_TOKEN_EXPIRATION_TIME_VALUE);
        EmailConfirmToken authenticationToken = new EmailConfirmToken(targetEmail, expiredAt);

        return authenticationToken;
    }

    public UUID renewToken() {
        this.tokenId = UUID.randomUUID();
        this.expiredAt = LocalDateTime.now()
                .plusMinutes(EMAIL_TOKEN_EXPIRATION_TIME_VALUE);

        return tokenId;
    }

    public boolean validCheck(
            UUID tokenId
    ) {

        if (tokenId.equals(this.tokenId)) {
            return true;
        }
        return false;
    }

    public void confirmToken() {
        this.confirmed = true;
    }
}
