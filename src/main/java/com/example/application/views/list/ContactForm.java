package com.example.application.views.list;

import com.example.application.data.entity.Company;
import com.example.application.data.entity.Contact;
import com.example.application.data.entity.Status;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class ContactForm extends FormLayout {
    //binder - needed for the data biding and form input validation.
    Binder<Contact> binder = new BeanValidationBinder<>(Contact.class);

    //create text fields for first name, last name, email.
    TextField firstName = new TextField("First Name");
    TextField lastName = new TextField("Last name");
    EmailField email = new EmailField("Email");
    //dropdown menu options using ComboBox
    ComboBox<Status> status = new ComboBox<>("Status");
    ComboBox<Company> company = new ComboBox<>("Company");

    //Creating the buttons for the contact form.
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");
    private Contact contact;

    //generating a constructor for the contact form

    public ContactForm(List<Company> companies, List<Status> statuses) {
        addClassName("Contact-form");
        //calling the binder.
        binder.bindInstanceFields(this);

        company.setItems(companies);
        company.setItemLabelGenerator(Company::getName);

        status.setItems(statuses);
        status.setItemLabelGenerator(Status::getName);

        //adding the components above to the layout.
        add(
                firstName,
                lastName,
                email,
                company,
                status,
                //below is the button layout for the save, delete, and cancel.
                createButtonLayout()
        );
    }

    //START OF THE API - needed for the data biding and form input validation.
    public void setContact(Contact contact){
        this.contact = contact;
        binder.readBean(contact);
    }

    // button layout method for the contact form.
    private Component createButtonLayout() {
        // PRIMARY will make it stand out, to indicate that that is the primary button to use.
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        //ERROR effect
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        //TERTIARY effect.
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        //THREE CLICK LISTENERS BELOW ARE PART OF THE API CREATED FOR THE CONTACT FORM FUNCTIONALITY.
        // CLICK LISTENER EVENTS FOR THE THREE BUTTONS WITHIN THE CONTACT FORM - needed for the data biding and form input validation.
        //adding a click listener for the Save button.
        save.addClickListener(event -> validateAndSave());
        //adding a click listener for the Delete button.
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, contact)));
        //adding a click listener for the Close button
        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));

        //adding click shortcuts
        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        // displaying the buttons on a horizontal layout.
        return new HorizontalLayout(save, delete, cancel);
    }

    // CORRESPONDING METHOD validateAndSave() FOR THE THREE CLICK LISTENERS - PART OF THE API CREATED
    // FOR THE THREE BUTTONS WITHIN THE CONTACT FORM.
    private void validateAndSave() {
        try{
            binder.writeBean(contact);
            fireEvent(new SaveEvent(this, contact));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // code black below are several Form events - copied from (https://vaadin.com/docs/latest/tutorial/forms-and-validation)
    // 'Setup component events' section of the tutorial.
    public static abstract class ContactFormEvent extends ComponentEvent<ContactForm> {
        private Contact contact;

        protected ContactFormEvent(ContactForm source, Contact contact) {
            super(source, false);
            this.contact = contact;
        }

        public Contact getContact() {
            return contact;
        }
    }

    public static class SaveEvent extends ContactFormEvent {
        SaveEvent(ContactForm source, Contact contact) {
            super(source, contact);
        }
    }

    public static class DeleteEvent extends ContactFormEvent {
        DeleteEvent(ContactForm source, Contact contact) {
            super(source, contact);
        }

    }

    public static class CloseEvent extends ContactFormEvent {
        CloseEvent(ContactForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}
