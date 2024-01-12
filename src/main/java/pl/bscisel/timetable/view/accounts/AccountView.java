package pl.bscisel.timetable.view.accounts;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import pl.bscisel.timetable.data.entity.Account;
import pl.bscisel.timetable.form.AccountForm;
import pl.bscisel.timetable.service.AccountService;
import pl.bscisel.timetable.view.layout.MainLayout;


@Route(value = "accounts", layout = MainLayout.class)
@RolesAllowed("ADMIN")
@PageTitle("Timetable - Accounts")
public class AccountView extends VerticalLayout {
    private final TextField textFilter = new TextField();
    private final Button addAccountBtn = new Button("Add account", event -> addAccount());
    private final Grid<Account> grid = new Grid<>(Account.class);

    private final AccountService accountService;
    private final AccountForm form;

    public AccountView(AccountService accountService,
                       AccountForm form) {
        this.accountService = accountService;
        this.form = form;
        setSizeFull();
        addClassName("managing-view");

        configureToolbar();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateItems();
    }

    private void closeForm() {
        closeEditor();
    }

    private void saveAccount(Account account) {
        accountService.save(account);
        updateItems();
        closeEditor();
    }

    private void deleteAccount(Account account) {
        accountService.delete(account);
        updateItems();
        closeEditor();
    }

    private void configureForm() {
        form.setVisible(false);

        form.addSaveAction(this::saveAccount);
        form.addDeleteAction(this::deleteAccount);
        form.addCancelAction(ignore -> closeForm());
    }

    private void configureGrid() {
        grid.addClassName("grid");
        grid.setSizeFull();
        grid.setColumns("emailAddress");
        grid.addColumn(Account::formatRoles).setHeader("Roles");
        grid.getColumns().forEach(column -> column.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event -> editAccount(event.getValue(), false));
    }

    private void configureToolbar() {
        textFilter.addValueChangeListener(event -> updateItems());
        textFilter.setValueChangeMode(ValueChangeMode.LAZY);
        textFilter.setClearButtonVisible(true);
        textFilter.setPlaceholder("Filter by email address...");
    }

    private Component getToolbar() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.addClassName("toolbar");
        layout.add(textFilter, addAccountBtn);
        return layout;
    }

    private Component getContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeFull();
        layout.setFlexGrow(2, grid);
        layout.setFlexGrow(1, form);
        layout.add(grid, form);
        return layout;
    }

    private void closeEditor() {
        form.setFormBean(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void editAccount(Account account, boolean isNew) {
        if (account == null) {
            closeEditor();
        } else {
            if (isNew)
                form.setMode(AccountForm.Mode.ADD);
            else
                form.setMode(AccountForm.Mode.EDIT);
            form.setFormBean(account);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void addAccount() {
        grid.asSingleSelect().clear();
        editAccount(new Account(), true);
    }

    private void updateItems() {
        grid.setItems(accountService.search(textFilter.getValue()));
    }
}