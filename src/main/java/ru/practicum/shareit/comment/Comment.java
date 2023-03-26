package ru.practicum.shareit.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "COMMENTS")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text")
    private String text;

    @JoinColumn(name = "author")
    @ManyToOne
    private User author;

    @JoinColumn(name = "itemId")
    @ManyToOne
    private Item item;

    @Column(name = "created")
    private LocalDateTime created;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
