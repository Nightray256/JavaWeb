package xyz.website.Main.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import xyz.website.Main.human.Person;
import xyz.website.Main.human.PersonReponsitory;

@Route("")
public class MainView extends VerticalLayout {

    private final PersonReponsitory personReponsitory;
    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private EmailField email = new EmailField("Email");
    private TextField clearID = new TextField("Select ID");
    private Grid<Person> grid = new Grid<>(Person.class);
    private Binder<Person> binder = new Binder<>(Person.class);

    public MainView(PersonReponsitory personReponsitory) {
        this.personReponsitory = personReponsitory;

        grid.setColumns("id", "firstName", "lastName", "email");
        add(getForm(), grid);
        refreshGrid();
    }

    private Component getForm() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setAlignItems(Alignment.BASELINE);

        Button addButton = new Button("add");
        addButton.addClickShortcut(Key.ENTER);
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button clearButton = new Button("clear");
        clearButton.addClickShortcut(Key.ESCAPE);
        clearButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        layout.add(firstName, lastName, email, addButton, clearID, clearButton);

        binder.bindInstanceFields(this);

        addButton.addClickListener(click -> {
            try {
                Person person = new Person();
                binder.writeBean(person);
                personReponsitory.save(person);
                binder.readBean(new Person());
                refreshGrid();
            } catch (ValidationException e) {
                //
            }
        });

        clearButton.addClickListener(click -> {
            binder.readBean(new Person());
            personReponsitory.deleteAll();
            refreshGrid();
        });

        return layout;
    }

    private void refreshGrid() {
        grid.setItems(personReponsitory.findAll());
    }
}