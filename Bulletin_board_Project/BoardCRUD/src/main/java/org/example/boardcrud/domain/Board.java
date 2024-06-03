package org.example.boardcrud.domain;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Table("board")
@EqualsAndHashCode
public class Board {
    @Id
    private Integer id;

    @NotEmpty(message = "name은 공백을 허용하지 않습니다.")
    private String name;

    private String title;

    @NotEmpty(message = "password는 공백을 허용하지 않습니다.")
    @Size(min = 3, message = "password는 최소 3자 이상이어야 합니다.")
    private String password;

    private String content;

    @PastOrPresent(message = "created_at은 현재 또는 과거 날짜여야 합니다.")
    private LocalDateTime created_at;

    @PastOrPresent(message = "updated_at은 현재 또는 과거 날짜여야 합니다.")
    private LocalDateTime updated_at;

    private int likes;
}
