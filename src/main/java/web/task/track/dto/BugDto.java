package web.task.track.dto;

import lombok.Data;

@Data
public class BugDto {
    private String title;
    private String description;
    private Integer taskId;
}
