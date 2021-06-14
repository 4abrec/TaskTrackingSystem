package web.task.track.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "feature")
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"users", "tasks"})
public class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "features")
    private Set<User> users;

    @OneToMany(mappedBy = "feature")
    @JsonIgnore
    private Set<Task> tasks;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EStatus statusFeature;

    public Feature(String title, String description, Set<User> users) {
        this.title = title;
        this.description = description;
        this.users = users;
        this.statusFeature = EStatus.OPEN;
    }
}
