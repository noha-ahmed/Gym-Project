package com.example.gymserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table
@Setter
@Getter
@EqualsAndHashCode
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trainee implements Serializable {
    @Id
    @Column(name = "traineeId")
    private Long id;

    @OneToOne(cascade=CascadeType.ALL)
    @MapsId
    @JsonIgnore
    @JoinColumn(name = "traineeId")
    private User user;

    @OneToMany(
            mappedBy = "trainee",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<PClassFollowUp> pClassFollowUps = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sessionReservation",
            joinColumns = @JoinColumn(name = "traineeId"),
            inverseJoinColumns = @JoinColumn(name = "sessionId"))
    List<Session> sessions;

}
