package com.test;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.jdbi.v3.core.Jdbi;

import com.test.core.Task;
import com.test.db.TaskDAO;
import com.test.health.TemplateHealthCheck;
import com.test.resources.HelloWorldResource;
import com.test.resources.TaskResource;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class todolistApplication extends Application<todolistConfiguration> {

    public static void main(final String[] args) throws Exception {
        new todolistApplication().run(args);
    }
    
    private final HibernateBundle<todolistConfiguration> hibernateBundle =
            new HibernateBundle<todolistConfiguration>(Task.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(todolistConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    @Override
    public String getName() {
        return "todolist";
    }

    @Override
    public void initialize(final Bootstrap<todolistConfiguration> bootstrap) {
       bootstrap.addBundle(new MigrationsBundle<todolistConfiguration>() {
            public DataSourceFactory getDataSourceFactory(todolistConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });  
       
       bootstrap.addBundle(hibernateBundle);


    }

    @Override
    public void run(final todolistConfiguration configuration,
                    final Environment environment) {
    	
    	final FilterRegistration.Dynamic cors =
    	        environment.servlets().addFilter("CORS", CrossOriginFilter.class);

    	    // Configure CORS parameters
    	    cors.setInitParameter("allowedOrigins", "*");
    	    cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
    	    cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

    	    // Add URL mapping
    	    cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    	final HelloWorldResource resource = new HelloWorldResource(
    	        configuration.getTemplate(),
    	        configuration.getDefaultName()
    	    );
    	final TaskDAO dao = new TaskDAO(hibernateBundle.getSessionFactory());
    	final TaskResource taskResource = new TaskResource(dao);
    	
    	final TemplateHealthCheck healthCheck =
    	        new TemplateHealthCheck(configuration.getTemplate());
    	    environment.healthChecks().register("template", healthCheck);
    	    
    	    environment.jersey().register(resource);
    	    environment.jersey().register(taskResource);
    	    	    
        
    }

}
