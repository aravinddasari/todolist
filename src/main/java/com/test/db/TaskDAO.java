package com.test.db;

import com.test.core.Task;
import io.dropwizard.hibernate.AbstractDAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.NotFoundException;

public class TaskDAO extends AbstractDAO<Task> {
  public TaskDAO(SessionFactory factory) {
      super(factory);
  }

  public Optional<Task> findById(Long id) {
      return Optional.ofNullable(get(id));
  }

  public Task create(Task task) {
      return persist(task);
  }
  
  public void update(Task task) {
	  Query query = namedQuery("updateTask");
	  query.setParameter("taskId", task.getId());
	  query.setParameter("name", task.getName());
	  query.setParameter("description", task.getDescription());
      query.executeUpdate();
  }
  
  
  public List<Task> getSubTasks(Long parentId) {
	  Query query = namedQuery("getSubTasks");
	  query.setParameter("parentId", parentId);
	  return list(query);
  }


  public List<Task> getAll() {
      return list((Query<Task>) namedQuery("getAll"));
  }

  public void deleteById(Long id) {
	  Task t = get(id);
	  Session session = currentSession();
	  
	  Query query = session.createQuery("from Task where id = :id ");
	  query.setParameter("id", id);
	  System.out.print("result is" + query.list());
	  session.delete(query.list().get(0));
  }
  
}