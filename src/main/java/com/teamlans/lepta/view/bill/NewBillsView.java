package com.teamlans.lepta.view.bill;

import com.teamlans.lepta.entities.Bill;
import com.teamlans.lepta.service.bill.BillService;
import com.teamlans.lepta.view.LeptaNotification;
import com.teamlans.lepta.view.ProtectedVerticalView;
import com.teamlans.lepta.view.bill.edit.EditBillView;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringView(name = NewBillsView.VIEW_NAME)
public class NewBillsView extends ProtectedVerticalView {

  public static final String VIEW_NAME = "Bills";

  @Autowired
  private BillService billService;

  private Table table;

  private BeanItemContainer<Bill> container;

  private Button editButton;

  private Button deleteButton;

  @PostConstruct
  void init() {
    setSpacing(true);
    setMargin(true);
    VerticalLayout centerContainer = new VerticalLayout();
    centerContainer.setSpacing(true);
    centerContainer.setWidth("70%");
    centerContainer.addComponent(buildBillsTable());
    HorizontalLayout buttonContainer = buildButtonContainer();
    centerContainer.addComponent(buttonContainer);
    addComponent(centerContainer);
    setComponentAlignment(centerContainer, Alignment.MIDDLE_CENTER);
  }

  private HorizontalLayout buildButtonContainer() {
    HorizontalLayout buttonContainer = new HorizontalLayout();
    buttonContainer.setSizeFull();
    buttonContainer.setSpacing(true);
    buttonContainer.addComponent(buildAddBillButton());
    editButton = buildEditBillButton();
    buttonContainer.addComponent(editButton);
    buttonContainer.setComponentAlignment(editButton, Alignment.MIDDLE_CENTER);
    editButton.setEnabled(false);
    deleteButton = buildDeleteBillButton();
    buttonContainer.addComponent(deleteButton);
    buttonContainer.setComponentAlignment(deleteButton, Alignment.MIDDLE_RIGHT);
    deleteButton.setEnabled(false);
    return buttonContainer;
  }

  private Table buildBillsTable() {
    table = new Table();
    table.setSizeFull();
    container = new BeanItemContainer<>(Bill.class);
    container.addAll(billService.listBillsFor(getLeptaUi().getLoggedInUser()));
    table.setContainerDataSource(container);
    table.setVisibleColumns("name");
    table.setSelectable(true);
    table.addItemClickListener(event -> {
      editButton.setEnabled(true);
      deleteButton.setEnabled(true);
    });
    return table;
  }


  private Button buildAddBillButton() {
    Button addButton = new Button("Add");
    addButton.addClickListener(
        event -> getUI().getNavigator().navigateTo(EditBillView.VIEW_NAME + "/" + -1));
    return addButton;
  }

  private Button buildEditBillButton() {
    Button editButton = new Button("Edit");
    editButton.addClickListener(clickEvent -> {
      Bill selectedBill = (Bill) table.getValue();
      int id = selectedBill.getId();
      getUI().getNavigator().navigateTo(EditBillView.VIEW_NAME + "/" + id);
    });
    return editButton;
  }

  private Button buildDeleteBillButton() {
    Button deleteButton = new Button("Delete");
    deleteButton.addClickListener(clickEvent -> {
      Bill selectedBill = (Bill) table.getValue();
      container.removeItem(selectedBill);
      billService.deleteBill(selectedBill);
      LeptaNotification.show("Bill has been deleted");
    });
    return deleteButton;
  }


  @Override
  public void enter(ViewChangeListener.ViewChangeEvent event) {
    // the view is constructed in the init() method()
  }
}
