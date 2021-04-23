package com.hackerrank.orm.service;

import com.hackerrank.orm.enums.ItemStatus;
import com.hackerrank.orm.exceptions.BadRequestException;
import com.hackerrank.orm.exceptions.NotFoundException;
import com.hackerrank.orm.model.Item;
import com.hackerrank.orm.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class ItemService {

    @Autowired
    public ItemRepository itemRepository;

    public Item createItem(Item itemData) {
        if (!itemRepository.existsById(itemData.getItemId())) {
            return itemRepository.save(itemData);
        } else {
            throw new BadRequestException();
        }
    }

    public Item updateItem(Item itemData, Integer itemId) {
        Optional<Item> persistedItem = itemRepository.findById(itemId);

        if (persistedItem.isPresent()) {
            if (itemData.getItemId() == 0) {
                itemData.setItemId(itemId);
            }
            return itemRepository.saveAndFlush(itemData);
        } else {
            throw new NotFoundException();
        }
    }

    public void deleteItemById(Integer itemId) {
        Optional<Item> persistedItem = itemRepository.findById(itemId);

        if (persistedItem.isPresent()) {
            itemRepository.deleteById(itemId);
        } else {
            throw new NotFoundException();
        }
    }

    public void deleteAllItems() {
        itemRepository.deleteAll();
    }

    public Item getItemById(int itemId) {
        Optional<Item> persistedItem = itemRepository.findById(itemId);

        if (persistedItem.isPresent()) {
            return persistedItem.get();
        } else {
            throw new NotFoundException();
        }
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public List<Item> getItemsByStatusAndEnteredBy(ItemStatus itemStatus, String enteredBy) {
        return itemRepository.findByItemStatusAndItemEnteredByUser(itemStatus, enteredBy);
    }

    public List<Item> getSortedPagedItemList(Integer pageSize, Integer page, String sortByField) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sortByField));
        return itemRepository.findAll(pageable).toList();
    }
}
