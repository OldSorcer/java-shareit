package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validator.groups.Create;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * // TODO .
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "items")
@ToString
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(groups = {Create.class})
    @NotBlank(groups = {Create.class})
    private String name;
    @NotNull(groups = {Create.class})
    @NotBlank(groups = {Create.class})
    private String description;
    @NotNull(groups = {Create.class})
    private Boolean available;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @OneToOne
    @JoinColumn(name = "request_id")
    private ItemRequest request;
}
