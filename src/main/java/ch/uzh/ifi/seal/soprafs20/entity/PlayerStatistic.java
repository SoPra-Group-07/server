package ch.uzh.ifi.seal.soprafs20.entity;

import org.mapstruct.BeanMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.nio.MappedByteBuffer;
import java.util.List;

/**
 *
 */
//@MappedSuperclass
@Entity
public abstract class PlayerStatistic implements Serializable {

    private static final long serialVersionUID = 1L;

    public PlayerStatistic() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playerStatisticId;

    @Column(name = "gameRound_Id")
    private Long gameRoundId;


}