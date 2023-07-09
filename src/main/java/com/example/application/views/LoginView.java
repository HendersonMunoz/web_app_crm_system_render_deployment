package com.example.application.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

// Spring Authentication. Creating a login page.
@Route("login")
@PageTitle("Login | Vaadin CRM")
public class LoginView extends VerticalLayout implements BeforeEnterListener {
    //constructing the view
    private LoginForm login = new LoginForm();
    public LoginView() {
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        //configuring the login action
        login.setAction("login");

        //components of the layout
        add(
                new H1("Vaadin CRM"),
                login
        );
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        //check if the URL has an error query parameter. If so, show the error.
        if(beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error")){
            login.setError(true);
        }
    }
}
