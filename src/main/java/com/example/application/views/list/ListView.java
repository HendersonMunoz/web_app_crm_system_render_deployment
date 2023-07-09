package com.example.application.views.list;

import com.example.application.data.entity.Contact;
import com.example.application.data.service.CrmService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Scope;

import java.util.Collections;

// components for the ListViewTest
@org.springframework.stereotype.Component
@Scope("prototype")
//UI ELEMENTS RESIDE WITHIN THIS LISTVIEW FILE.
@PageTitle("Contacts | Vaadin CRM")
// MainLayout.cass is the java class we use to customize for nav menu/toggle.
@Route(value = "", layout = MainLayout.class)
// all logged-in users, should be able to access this view.
@PermitAll
public class ListView extends VerticalLayout {
    // creating the CRM grid.
    // Calling on a built-in Vaadin class, called 'Contact class'. details can be view by right-clicking on it.
    Grid<Contact> grid = new Grid<>(Contact.class);
    // creating text-fields for the new grid.
    TextField filterText = new TextField();
    //fetching the ContactForm class.
    ContactForm form;
    private CrmService service;

    // ADDING UI COMPONENTS.
    // Also, auto-wiring the CrmService Class, for DB connectivity.
    public ListView(CrmService service) {
        this.service = service;
        // Adding a ClassName, so that it makes things easier when I write CSS class later in the project.
        addClassName("list-view");
        //making the list view the same size as the entire window.
        setSizeFull();
        //configuring the layout.
        configureGrid();
        //Configuration method for the Contact Form.
        configureForm();

        //adding toolbar components to the grid.
        add(
                //calling the getToolBar method.
                getToolBar(),
                //calling the getContent method.
                getContent()
        );

        //calling the updateList method, Updates the contact list in the DB.
        updateList();

        //Calling on the closeEditor() method. When we start the application, we won't see the form
        //Because we don't have anyone selected.
        closeEditor();
    }

    //METHODS CREATED BELOW.

    //closes the contact form, unless the user interacts with it.
    private void closeEditor() {
        form.setContact(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    //method that updates the contact list.
    private void updateList() {
        grid.setItems(service.findAllContacts(filterText.getValue()));
    }

    // method that creates a horizontal wrapper layout that holds the grid and the contact form next to each-other.
    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        // line below says that the grid should get 2/3s of the page space/layout.
        content.setFlexGrow(2, grid);
        // line below says that the form should get 1/3 of the page space/layout.
        content.setFlexGrow(1,form);
        content.addClassName("Content");
        content.setSizeFull();

        return content;
    }

    //method for the configureForm method.
    private void configureForm() {
        form = new ContactForm(service.findAllCompanies(), service.findAllStatuses());
        form.setWidth("25em");

        //adding listeners to the form, for the customer events (buttons functionality.)
        form.addSaveListener(this::saveContact);
        form.addDeleteListener(this::deleteContact);
        form.addCloseListener(e -> closeEditor());
    }

    //method for the saveContact button function.
    private void saveContact(ContactForm.SaveEvent event) {
        service.saveContact(event.getContact());
        updateList();
        closeEditor();
    }
    //method for the deleteContact button function.
    private void deleteContact(ContactForm.DeleteEvent event) {
        service.deleteContact(event.getContact());
        updateList();
        closeEditor();
    }

    //creating the getToolBar component.
    private Component getToolBar() {
        //search field with a place-holder title.
        filterText.setPlaceholder("Filter by name...");
        //Clear button
        filterText.setClearButtonVisible(true);
        //ValueChangeMode(LAZY) -> this means that the app will wait until the user stops typing, to fetch the data,
        // from the database. If we don't use this 'LAZY' mode, the app will try fetching the data after every keystroke.
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        //filterText field, will filter through records as we're typing.
        filterText.addValueChangeListener(e -> updateList());

        //Add button to create a new contact.
        Button addContactButton = new Button("Add contact");
        //Giving the Add button functionality.
        addContactButton.addClickListener(q -> addContact());

        // arranging these components into a horizontal layout.
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    //method for the Add contact button functionality.
    private void addContact() {
        //using .clear() - this means that when we create a new contact, we don't want a different contact to be selected.
        grid.asSingleSelect().clear();
        editContact(new Contact());
    }

    // creating the configureGrid method.
    private void configureGrid(){
        //adding a className to this grid, so I can style it later in the project.
        grid.addClassName("contact-grid");
        grid.setSizeFull();;
        // Choose the addColumn String
        grid.setColumns("firstName", "lastName", "email");
        //chose the addColumn Render <Column> renderer.
        grid.addColumn(contact -> contact.getStatus().getName()).setHeader("Status");
        grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Company");
        // line of code below will automatically resize all the columns.
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        //if a user gets selected from the list, the code below will populate the user info in the contact form.
        //so that the contact info can be manipulated.
        grid.asSingleSelect().addValueChangeListener(e -> editContact(e.getValue()));
    }
    //edit contact method. Used under the configureGrid() method above.
    private void editContact(Contact contact) {
        if(contact == null){
            closeEditor();
        }else{
            form.setContact(contact);
            form.setVisible(true);
            addClassName("editing");
        }
    }
}
