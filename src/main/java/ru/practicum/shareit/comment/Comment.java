package ru.practicum.shareit.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
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

    @JoinColumn(name = "item_id")
    @ManyToOne
    private Item item;

    @Column(name = "created")
    private LocalDateTime created;
}

// create TABLE IF NOT EXISTS COMMENTS (
//   id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
//   text varchar (4000) not null,
//   author BIGINT not null  ,
//   item_id BIGINT not null ,
//   created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
//   FOREIGN KEY (author) REFERENCES users (id),
//   FOREIGN KEY (item_id) REFERENCES items (id)
//   );
