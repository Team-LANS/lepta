package com.teamlans.lepta.view.bill.edit.component.items;

import com.teamlans.lepta.entities.Bill;
import com.teamlans.lepta.entities.Item;
import com.teamlans.lepta.view.LeptaNotification;
import com.vaadin.data.Validator;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;

public class AddItemControl extends HorizontalLayout {

  private Bill bill;
  private ItemNameField itemNameField;
  private ItemPriceField itemItemPriceField;
  private ItemCollection itemCollection;

  private static final Logger logger = LoggerFactory.getLogger(AddItemControl.class);

  public AddItemControl() {
    itemNameField = new ItemNameField();
    addComponent(itemNameField);
    itemItemPriceField = new ItemPriceField();
    addComponent(itemItemPriceField);
    Button addItemButton = new Button("Add");
    addItemButton.addClickListener(event -> createItem());
    addComponent(addItemButton);
  }

  public void setBill(Bill bill) {
    this.bill = bill;
  }


  public void setItemCollection(ItemCollection itemCollection) {
    this.itemCollection = itemCollection;
  }

  private void createItem() {
    try {
      itemNameField.validate();
      itemItemPriceField.validate();
    } catch (Validator.InvalidValueException e) {
      logger.error("Validation error", e);
      LeptaNotification.showError(e.getMessage());
      return;
    }
    String name = itemNameField.getValue();
    Double price = Double.parseDouble(itemItemPriceField.getValue());
    itemNameField.clear();
    itemItemPriceField.clear();
    Item item = new Item(name, price, bill);
    itemCollection.addItem(item);
  }


}
