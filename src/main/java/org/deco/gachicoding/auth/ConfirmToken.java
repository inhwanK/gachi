package org.deco.gachicoding.auth;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class ConfirmToken {

    private static final long EMAIL_TOKEN_EXPIRATION_TIME_VALUE = 5L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID tokenId;

    @NotNull
    private String targetEmail;

    @NotNull @CreationTimestamp
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime expiredAt;
    private boolean expired;

//    public ConfirmToken(UUID tokenId, String targetEmail, LocalDateTime expiredAt) {
//        this.tokenId = tokenId;
//        this.targetEmail = targetEmail;
//        this.expiredAt = expiredAt;
//    }
//
//    public static ConfirmToken createEmailConfirmToken(String targetEmail) {
//        String email = targetEmail;
//        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(EMAIL_TOKEN_EXPIRATION_TIME_VALUE);
//
//        ConfirmToken authenticationToken = new ConfirmToken(tokenId, );
//
//        authenticationToken.expired = false;
//        return authenticationToken;
//    }
//
//    public UUID renewToken(){
//        this.tokenId = UUID.randomUUID();
//        this.createdAt = LocalDateTime.now();
//        this.expiredAt = createdAt.plusMinutes(EMAIL_TOKEN_EXPIRATION_TIME_VALUE);
//        return this.getTokenId();
//    }
//
//    public void confirmToken() {
//        this.expired = true;
//    }
}
