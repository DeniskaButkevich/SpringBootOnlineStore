package com.dez.predesign.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class Message  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    public Message(String name, String text, String tag) {
        this.name = name;
        this.text = text;
        this.tag = tag;
    }

    private String name;
    private String text;
    private String tag;

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
