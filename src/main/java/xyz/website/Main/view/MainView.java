package xyz.website.Main.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
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

    private PersonReponsitory personReponsitory;
    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private EmailField email = new EmailField("Email");
    private TextField clearID = new TextField("Select ID");
    private Grid<Person> grid = new Grid<>(Person.class);
    private Binder<Person> binder = new Binder<>(Person.class);

    public MainView(PersonReponsitory personReponsitory) {
        this.personReponsitory = personReponsitory;

        grid.setColumns("id", "firstName", "lastName", "email");
        add(createTitle("Minecraft 插件開發", "LightBlue", "20px"));
        add(getForm(), grid);
        refreshGrid();
    }

    private HorizontalLayout createTitle(String text, String titleColor, String marginBottom) {
        H1 title = new H1(text);
        title.getStyle()
                .set("color", titleColor)
                .set("margin-bottom", marginBottom);

        return new HorizontalLayout(title);
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

        Div line1 = new Div();
        line1.add(firstName, lastName, email, addButton);
        line1.getStyle().set("padding", "5px");
        lastName.getStyle()
                .set("margin-left", "20px")
                .set("margin-right", "20px");
        email.getStyle().set("margin-right", "20px");

        Div line2 = new Div();
        line2.add(clearID, clearButton);
        line2.getStyle().set("padding", "5px");
        clearID.getStyle()
                .set("margin-left", "15px")
                .set("margin-right", "20px");


        HorizontalLayout remove = new HorizontalLayout(line2);
        layout.setAlignItems(Alignment.BASELINE);

        layout.add(line1);
        layout.add(remove);

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