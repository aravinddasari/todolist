package com.test.resources;

import com.test.core.Task;
import com.test.db.TaskDAO;
import com.test.api.*;

import io.dropwizard.hibernate.UnitOfWork;

import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.List;


@Path("/todos")
@Produces(MediaType.APPLICATION_JSON)
public class TaskResource {
	private final TaskDAO taskDAO;
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskResource.class);

    public TaskResource(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }
    
    @POST
    @UnitOfWork
    public Response createTask(@Valid Task task) {
    	LOGGER.info("Received inputs", task);
        return Response.ok(taskDAO.create(task), MediaType.APPLICATION_JSON).build();
    }
    
    @PUT
    @UnitOfWork
    @Path("/{taskId}")
    public Response update(@PathParam("taskId") Long taskId, Task task) {
    	Task existingTask = findSafely(taskId);
    	if(existingTask!= null) {
    		task.setId(taskId);
    		taskDAO.update(task);
    	}
		return Response.ok(Response.Status.OK).build();
    }

    @GET
    @UnitOfWork
    public Response getAllTasks() {
    	List<Task> res =  taskDAO.getAll();
    	return Response.ok(res, MediaType.APPLICATION_JSON).build();    
    }
    
    @GET
    @UnitOfWork
    @Path("/{taskId}")
    public Response getTaskById(@PathParam("taskId") Long taskId) {
    	Task task = findSafely(taskId); 
    	return Response.ok(task, MediaType.APPLICATION_JSON).build(); 	
    }
    
    @DELETE
    @UnitOfWork
    @Path("/{id}")
    public Response delete(@PathParam("id") Long taskId) {
    	Task task = findSafely(taskId); 
    	if(task != null)
    		taskDAO.deleteById(taskId);
    	return Response.ok(Response.Status.OK).build();
    }
    

	private Task findSafely(long taskId) {
        return taskDAO.findById(taskId).orElseThrow(() -> new NotFoundException("No such task."));
    }
}
