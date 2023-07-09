package com.example.application.views;

import com.example.application.data.service.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard | Vaadin CRM")
// all logged-in users, should be able to access this view.
@PermitAll
public class DashboardView extends VerticalLayout {
    //Constructor
    private CrmService service;
    public DashboardView(CrmService service) {
        this.service = service ;
        //giving it a class name
        addClassName("dashboard-view");
        //centering the dashboard
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        //calling two helper methods.
        add(getContactStats(), getCompaniesChart());
    }

    //defining the helper methods above.
    private Component getContactStats(){
        //show how many contacts we have in the system. For that we can use Span().
        Span stats = new Span(service.countContacts() + " contact");
        // setting the text to extra large, and the margin top to medium.
        stats.addClassNames("text-xl", "mt-m");
        return stats;
    }

    private Component getCompaniesChart(){
        //creating a new chart
        Chart chart = new Chart(ChartType.PIE);
        //getting the data for the Pie chart, using a data series.
        DataSeries dataSeries = new DataSeries();
        //pulling the stats for each company, so we can add it to the Pie chart.
        service.findAllCompanies().forEach(company -> {
            //code for the getEmployeeCount is located in the Company.java class
            dataSeries.add(new DataSeriesItem(company.getName(), company.getEmployeeCount));
        });
        //getting the chat, setting the configuration and set data series.
        chart.getConfiguration().setSeries(dataSeries);
        return chart;
    }

}
