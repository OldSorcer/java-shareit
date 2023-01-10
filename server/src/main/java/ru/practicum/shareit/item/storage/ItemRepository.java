package ru.practicum.shareit.item.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerIdOrderById(Long ownerId, Pageable page);

    @Query("select item from Item item " +
            "where lower(item.name) like lower(concat('%', ?1, '%'))  " +
            "or lower(item.description) like lower(concat('%', ?1, '%')) " +
            "and item.available = true")
    List<Item> searchBy(String text, Pageable page);

    List<Item> findByRequestId(Long requestId);
}