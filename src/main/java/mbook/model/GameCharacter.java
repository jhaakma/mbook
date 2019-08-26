package mbook.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;

import lombok.Getter;
import lombok.Setter;
import mbook.morrowind.model.GameClass;

@Getter @Setter
public class GameCharacter {
    @Indexed
    private String name;
    @NotNull @Valid
    private GameClass gameClass;
    @NotNull
    private Integer gold;
    @NotNull
    private Integer level; 

}
