package com.hackerrank.orm.controller;

import com.hackerrank.orm.enums.ItemStatus;
import com.hackerrank.orm.exceptions.BadRequestException;
import com.hackerrank.orm.exceptions.NotFoundException;
import com.hackerrank.orm.model.Item;
import com.hackerrank.orm.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ItemController {

    @Autowired
    ItemService itemService;

    //1. insert POST
    @PostMapping(path = "/app/item", produces = {"application/json"})
    public ResponseEntity<Item> storeItem(
            @RequestBody final Item itemData
    ) {
        try {
            return new ResponseEntity<>(itemService.createItem(itemData), HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //2. update PUT
    @PutMapping(path = "/app/item/{itemId}", produces = {"application/json"})
    public ResponseEntity<Item> updateItem(
            @PathVariable final Integer itemId,
            @RequestBody final Item itemData
    ) {
        try {
            return new ResponseEntity<>(itemService.updateItem(itemData, itemId), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //3. delete by itemId DELETE
    @DeleteMapping(path = "/app/item/{itemId}", produces = {"application/json"})
    public ResponseEntity<Void> deleteItemId(
            @PathVariable final Integer itemId
            ) {
        try {
            itemService.deleteItemById(itemId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //4. delete all DELETE
    @DeleteMapping(path = "/app/item", produces = {"application/json"})
    public ResponseEntity<Void> deleteAll() {
        itemService.deleteAllItems();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //5. get by itemId GET
    @GetMapping(path = "/app/item/{itemId}", produces = {"application/json"})
    public ResponseEntity<Item> getItemById(
        @PathVariable final int itemId
    ) {
        try {
            return new ResponseEntity<>(itemService.getItemById(itemId), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //6. get all GET
    @GetMapping(path = "/app/item", produces = {"application/json"})
    public ResponseEntity<List<Item>> getAllItems() {
        return new ResponseEntity<>(itemService.getAllItems(), HttpStatus.OK);
    }

    //7. filters by fields ?itemStatus={status}&itemEnteredByUser={modifiedBy} GET
    @GetMapping(params = {"itemStatus", "itemEnteredByUser"}, path = "/app/item", produces = {"application/json"})
    public ResponseEntity<List<Item>> getItemsByStatusAndEnteredBy(
            @RequestParam(value = "itemStatus") final ItemStatus itemStatus,
            @RequestParam(value = "itemEnteredByUser") final String enteredBy
    ) {
        return new ResponseEntity<>(itemService.getItemsByStatusAndEnteredBy(itemStatus, enteredBy), HttpStatus.OK);
    }


    //8. select all with sorting and pagination ?pageSize={pageSize}&page={page}&sortBy={sortBy} GET
    @GetMapping(value = "/app/item", produces = {"application/json"}, params = {"pageSize", "page", "sortBy"})
    public ResponseEntity<List<Item>> getSortedPagedItemList(
            @RequestParam(value = "pageSize") final Integer pageSize,
            @RequestParam(value = "page") final Integer page,
            @RequestParam(value = "sortBy") final String sortByField
    ) {
        return new ResponseEntity<>(itemService.getSortedPagedItemList(pageSize, page, sortByField), HttpStatus.OK);
    }
}
