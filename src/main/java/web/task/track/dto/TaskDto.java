package web.task.track.dto;

import lombok.Data;

@Data
public class TaskDto {
    private String title;
    private String description;
    private String username;
    private Integer featureId;
}
