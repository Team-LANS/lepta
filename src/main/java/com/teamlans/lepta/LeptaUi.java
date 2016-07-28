package com.teamlans.lepta;

import com.teamlans.lepta.entities.User;
import com.teamlans.lepta.service.user.UserService;
import com.teamlans.lepta.view.MainView;
import com.teamlans.lepta.view.account.LoginView;
import com.teamlans.lepta.view.account.SignUpView;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.servlet.annotation.WebServlet;

/**
 * This UI is the application entry point. A UI may either represent a browser window (or tab) or
 * some part of a html page where a Vaadin application is embedded. <p> The UI is initialized using
 * {@link #init(VaadinRequest)}. This method is intended to be overridden to add component to the
 * user interface and initialize non-component functionality.
 */
@SpringUI
@Theme("lepta")
@Widgetset("com.teamlans.lepta.MyAppWidgetset")
@PreserveOnRefresh
public class LeptaUi extends UI {

  @Autowired
  private SpringViewProvider viewProvider;

  @Autowired
  private ApplicationContext context;

  @Autowired
  private UserService service;
  private User loggedInUser;

  @Override
  protected void init(VaadinRequest request) {
    VaadinSession.getCurrent().getSession().setMaxInactiveInterval(3600); // 1 hour

    if (getLoggedInUser() == null) {
      goToCorrectWelcomeView();
    } else {
      setContent(new MainView());
    }
  }

  public void goToCorrectWelcomeView() {
    if (service.noUsersExist()) {
      setContent(context.getBean(SignUpView.class));
    } else {
      setContent(context.getBean(LoginView.class));
    }
  }

  public User getLoggedInUser() {
    return loggedInUser;
  }

  public void setLoggedInUser(User loggedInUser) {
    this.loggedInUser = loggedInUser;
  }

  public SpringViewProvider getViewProvider() {
    return viewProvider;
  }

  @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)

  @VaadinServletConfiguration(ui = LeptaUi.class, productionMode = false)
  public static class MyUIServlet extends VaadinServlet {
  }

}
