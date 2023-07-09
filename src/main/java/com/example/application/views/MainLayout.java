package com.example.application.views;

import com.example.application.security.SecurityService;
import com.example.application.views.list.ListView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {
    private SecurityService securityService;

    //constructor
    public MainLayout(SecurityService securityService) {
        //field for the security service call
        this.securityService = securityService;
        //helper method to create header
        createHeader();
        //helper method to create drawer
        createDrawer();
    }
    //defining methods below.
    private void createHeader() {
        H1 logo = new H1("Vaadin CRM");
        //design class names can be found on the vaadin design docs (https://vaadin.com/docs/latest/styling/lumo/utility-classes)
        logo.addClassNames("text-l", "m-m");

        // Log out button
        Button logOut = new  Button("Log out", e -> securityService.logout());
        //creating a new horizontal layout for what is going to go in the H1 header above.
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logOut);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        //padding in the 'Y' axis = 0   and the padding in the 'X' axis = medium.
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink listView = new RouterLink("List", ListView.class);
        listView.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                listView
                //The Pie chart is a paid Vaadin feature, so it will not work for this project. Hence commenting it out.
                //new RouterLink("Dashboard", DashboardView.class)
        ));
    }
}
