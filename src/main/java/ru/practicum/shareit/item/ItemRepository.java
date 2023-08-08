package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(nativeQuery = true, value = "select * from items i where i.user_id = ?1 order by i.id asc")
    List<Item> findAllByOwnerId(long ownerId);

    List<Item> findByDescriptionContainingIgnoreCaseAndAvailable(String search, Boolean available);
}
