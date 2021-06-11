package web.task.track.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "status")
@EqualsAndHashCode(exclude = {"tasks"})
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private EStatusTask name;

    @OneToMany(mappedBy = "status")
    @JsonIgnore
    private Set<Task> tasks;

}

