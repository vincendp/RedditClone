package com.vincendp.RedditClone.Model;

import javax.persistence.*;

@Entity
@Table(name = "PostType")
public class PostType {

    public enum Type{
        TEXT(1),
        IMAGE(2),
        LINK(3);

        private int value;

        Type(int value){
            this.value = value;
        }

        public int getValue(){
            return value;
        }

    }

    @Id
    private Integer id;

    @Column(length = 50)
    private String name;

    public PostType() {
    }

    public PostType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

