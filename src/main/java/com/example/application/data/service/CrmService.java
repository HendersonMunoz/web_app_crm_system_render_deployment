package com.example.application.data.service;
import com.example.application.data.entity.Company;
import com.example.application.data.entity.Contact;
import com.example.application.data.entity.Status;
import com.example.application.data.repository.CompanyRepository;
import com.example.application.data.repository.ContactRepository;
import com.example.application.data.repository.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Spring Service class, so we'll annotate below with a @Service annotation.
//CrmService is the java class I'll be creating for the database connectivity.
@Service
public class CrmService {

    private final ContactRepository contactRepository;
    private final CompanyRepository companyRepository;
    private final StatusRepository statusRepository;

    //Constructor below. Adding the repositories that we need.
    public CrmService(ContactRepository contactRepository,
                      CompanyRepository companyRepository,
                      StatusRepository statusRepository) {
        this.contactRepository = contactRepository;
        this.companyRepository = companyRepository;
        this.statusRepository = statusRepository;
    }

    //method that finds contacts based on a filtered text.
    public List<Contact> findAllContacts(String filterText) {
        if(filterText == null || filterText.isEmpty()){
            return contactRepository.findAll();
        } else {
            return contactRepository.search(filterText);
        }
    }

    // public method. returns contact repository count.
    public long countContacts(){
        return contactRepository.count();
    }

    //public method. delete contact
    public void deleteContact(Contact contact){
        contactRepository.delete(contact);
    }

    //public method. save contact
    public void saveContact(Contact contact){
        if(contact == null) {
            System.err.println("Contact is null.");
            return;
        }
        contactRepository.save(contact);
    }

    //public method. find companies.
    public List<Company> findAllCompanies(){
        return companyRepository.findAll();
    }

    //public method. Finds status.
    public List<Status> findAllStatuses(){
        return statusRepository.findAll();
    }
}
