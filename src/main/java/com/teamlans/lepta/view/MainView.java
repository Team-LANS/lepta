package com.teamlans.lepta.view;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.UI;

/**
 * Protected home view with navigation bar and root view that will be filled by the navigator.
 */
public final class MainView extends ProtectedVerticalView {

  private final Navigator navigator;


  public MainView() {
    addComponent(new NavigationBar());

    ProtectedVerticalView root = new ProtectedVerticalView();
    addComponent(root);
    root.setSizeFull();

    navigator = new Navigator(UI.getCurrent(), root);
    navigator.addProvider(getLeptaUi().getViewProvider());
    navigator.addViewChangeListener(new ViewChangeListener() {
      @Override
      public boolean beforeViewChange(ViewChangeEvent viewChangeEvent) {
        if (isProtected(viewChangeEvent.getNewView()) &&
            (getLeptaUi().getLoggedInUser() == null)) {
          getLeptaUi().goToCorrectWelcomeView();
          return false;
        } else {
          return true;
        }
      }

      @Override
      public void afterViewChange(ViewChangeEvent viewChangeEvent) {
        // needed when overriding beforeViewChange
      }
    });
  }

  private boolean isProtected(View view) {
    return view instanceof ProtectedVerticalView || view instanceof ProtectedHorizontalView;
  }

}
