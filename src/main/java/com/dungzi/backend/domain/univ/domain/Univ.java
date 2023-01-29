package com.dungzi.backend.domain.univ.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Univ {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column( nullable = false, length = 36)
    @Type(type = "uuid-char")
    private UUID univId;

//    @Id
//    @Column(nullable = false)
//    private String univId;

    private String univName;

    private String emailDomain;
}
