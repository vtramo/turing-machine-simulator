package com.github.vtramo.turingmachine;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Path;

public class HomeController {
    private static final String TM_TAB_TEMPLATE_FXML = "turing-machine-tab-template.fxml";
    private static final Path TM_HELLO_WORLD_PATH = Path.of("/turing-machine-hello-world.yaml");

    private static final String TM_HELLO_WORLD_NAME = "Hello World";

    @FXML
    private TabPane tabPane;
    @FXML
    private Tab addTabButton;

    private final Stage primaryStage;

    public HomeController(final Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @SneakyThrows
    public void initialize() {
        addListenerToNewTuringMachineTabButton();
        createHelloWorldTuringMachineTab();
    }

    private void addListenerToNewTuringMachineTabButton() {
        final SingleSelectionModel<Tab> singleSelectionModelTab = tabPane.getSelectionModel();
        final ReadOnlyObjectProperty<Tab> readOnlyObjectPropertyTab = singleSelectionModelTab.selectedItemProperty();
        readOnlyObjectPropertyTab.addListener((__, ___, newSelectedTab) -> {
            if (newSelectedTab != addTabButton) return;
            final String turingMachineTabTitle = buildNextTuringMachineTabTitle();
            createHelloWorldTuringMachineTab();
        });
    }

    private String buildNextTuringMachineTabTitle() {
        final ObservableList<Tab> tabs = tabPane.getTabs();
        return "Turing Machine " + tabs.size();
    }

    @SneakyThrows
    public Tab createTuringMachineTab(final String title, final Path turingMachineYamlProgramPath) {
        return null;
    }

    private Tab createHelloWorldTuringMachineTab() {
        final String turingMachineYamlProgram = TuringMachineApplication.readResourceAsString(TM_HELLO_WORLD_PATH.toString());
        return createTuringMachineTab(TM_HELLO_WORLD_NAME, turingMachineYamlProgram);
    }

    private Tab createTuringMachineTab(final String title, final String turingMachineYamlProgram) {
        return createTuringMachineTab(title, turingMachineYamlProgram, null);
    }

    public Tab createTuringMachineTab(final String title, final String turingMachineYamlProgram, final Path turingMachineYamlProgramPath) {
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(TuringMachineApplication.class.getResource(TM_TAB_TEMPLATE_FXML));
            final TuringMachineTabController turingMachineTabController = new TuringMachineTabController(
                this, primaryStage,
                turingMachineYamlProgram, turingMachineYamlProgramPath
            );
            fxmlLoader.setController(turingMachineTabController);

            final Tab tab = fxmlLoader.load();
            tab.setText(title);
            setTabClosablePolicy(tab);
            addTab(tab);
            selectTab(tab);

            return tab;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setTabClosablePolicy(final Tab tab) {
        final ObservableList<Tab> tabs = tabPane.getTabs();
        final BooleanProperty tabBooleanProperty = tab.closableProperty();
        tabBooleanProperty.bind(Bindings.size(tabs).greaterThan(2));
    }

    private void addTab(Tab tab) {
        final ObservableList<Tab> tabs = tabPane.getTabs();
        tabs.add(tabs.size() - 1, tab);
    }

    private void selectTab(Tab tab) {
        final SingleSelectionModel<Tab> singleSelectionModelTab = tabPane.getSelectionModel();
        singleSelectionModelTab.select(tab);
    }
}