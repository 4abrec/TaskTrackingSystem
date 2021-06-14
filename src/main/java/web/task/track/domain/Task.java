package web.task.track.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.springframework.data.history.RevisionMetadata;
import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "task")
@EqualsAndHashCode(exclude= {"user", "feature", "status", "bug"})
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "current_user_id")
    @Audited
    private User user;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EStatus status;

    @ManyToOne
    @JoinColumn(name = "feature_id")
    private Feature feature;

    @OneToOne
    @JoinColumn(name = "bug_id", referencedColumnName = "id")
    private Bug bug;

    @Transient
    @JsonIgnore
    private RevisionMetadata<Integer> editVersion;


    public void setEditVersion(RevisionMetadata<Integer> editVersion) {
        this.editVersion = editVersion;
    }

    public RevisionMetadata<Integer> getEditVersion() {
        return editVersion;
    }

    public Task(String title, String description, User user, Feature feature, EStatus status) {
        this.title = title;
        this.description = description;
        this.user = user;
        this.feature = feature;
        this.status = status;
    }

}
