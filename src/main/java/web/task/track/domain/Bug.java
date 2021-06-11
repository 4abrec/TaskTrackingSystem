package web.task.track.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "bug")
@EqualsAndHashCode(exclude = {"task"})
@NoArgsConstructor
public class Bug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @OneToOne(mappedBy = "bug")
    @JsonIgnore
    private Task task;

    public Bug(String title, String description, Task task) {
        this.title = title;
        this.description = description;
        this.task = task;
    }
}
