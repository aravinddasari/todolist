package com.test.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tasks")
@NamedQueries(
        {
                @NamedQuery(
                        name = "getAll",
                        query = "from Task t where t.parent_id = 0 and t.id != 0"
                ),
                @NamedQuery(
                        name = "updateTask",
                        query = "update Task set name = :name, description = :description where id = :taskId"
                ),
                @NamedQuery(
                        name = "getSubTasks",
                        query = "from Task t where t.parent_id = :parentId"
                )
        })
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Cascade(CascadeType.DELETE)
    private long id;
       
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;
    
    @Cascade(CascadeType.REMOVE)
    @Column(name = "parent_id", insertable=true, updatable=true)
    private long parent_id;
    
    @OneToMany(mappedBy="parent_id",  orphanRemoval = true, fetch=FetchType.LAZY)
    @Cascade({CascadeType.REMOVE, CascadeType.SAVE_UPDATE})
    private List<Task> subTasks;
    
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;
    
    @PreUpdate
    protected void onUpdate() {
      this.updatedAt = new Date();
    }

    @PrePersist
    protected void onCreate() {
      this.updatedAt = this.createdAt = new Date();
    }

    public Task() {
    }

    public Task(String name, String description, int parent_id, List<Task> subTasks) {
        this.name = name;
        this.description = description;
        this.parent_id = parent_id;
        this.subTasks = subTasks;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setParentId(long setParentId) {
        this.parent_id = setParentId;
    }
    
    public long getParentId() {
        return parent_id;
    }
    
    public void setSubTasks(List<Task> subTasks) {
        this.subTasks = subTasks;
    }
    
    public List<Task> getSubTasks() {
        return subTasks;
    }



    
}

