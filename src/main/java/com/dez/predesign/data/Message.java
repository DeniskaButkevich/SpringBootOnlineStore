package com.dez.predesign.data;

import com.dez.predesign.data.catalog.Product;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Please fill the text")
    @Length(max = 1024, message = "Text too long (more than 1kB)")
    private String text;

    @NotBlank(message = "Please fill the subject")
    @Length(max = 256, message = "Subject too long (more than 256)")
    private String subject;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Date placedAt;

    private Integer rating;

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    @PrePersist
    void placedAt() {
        this.placedAt = new Date();
    }
}
