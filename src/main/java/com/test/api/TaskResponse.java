package com.test.api;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.test.core.Task;

public class TaskResponse {
	private long id;
	private String name;
	private String description;
	private List<Task> sub_tasks;
 	
	
	public TaskResponse(Task task, List<Task> subTasks) {
		this.id = task.getId();
		this.name = task.getName();
		this.description = task.getDescription();
		this.sub_tasks = subTasks;
	}
	
    @JsonProperty
    public long getId() {
        return id;
    }
    
    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public String getDescription() {
        return description;
    }
    
    @JsonProperty
    public List<Task> getSubTasks() {
        return sub_tasks;
    }

}
