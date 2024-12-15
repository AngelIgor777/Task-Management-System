package org.task.task_management_system.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@Entity
@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
@Table(name = "tasks", schema = "task_management")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String priority;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(author, task.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author);
    }
}