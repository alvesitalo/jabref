package org.jabref.gui.sharelatex;

import java.io.IOException;
import java.util.Optional;

import javax.inject.Inject;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;

import org.jabref.gui.StateManager;
import org.jabref.gui.util.BaseDialog;
import org.jabref.gui.util.DefaultFileUpdateMonitor;
import org.jabref.logic.sharelatex.ShareLatexManager;
import org.jabref.model.database.BibDatabaseContext;
import org.jabref.preferences.PreferencesService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ShareLatexProjectDialogView extends BaseDialog<Void> {

    private static final Log LOGGER = LogFactory.getLog(ShareLatexProjectDialogViewModel.class);

    @FXML private TableColumn<ShareLatexProjectViewModel, Boolean> colActive;
    @FXML private TableColumn<ShareLatexProjectViewModel, String> colTitle;
    @FXML private TableColumn<ShareLatexProjectViewModel, String> colFirstName;
    @FXML private TableColumn<ShareLatexProjectViewModel, String> colLastName;
    @FXML private TableColumn<ShareLatexProjectViewModel, String> colLastModified;
    @FXML private TableView<ShareLatexProjectViewModel> tblProjects;
    @Inject private ShareLatexManager manager;
    @Inject private StateManager stateManager;
    @Inject private PreferencesService preferences;

    private ShareLatexProjectDialogViewModel viewModel;

    @Inject private DefaultFileUpdateMonitor fileMonitor;

    @FXML
    private void initialize() {
        viewModel = new ShareLatexProjectDialogViewModel(stateManager, manager, preferences.getImportFormatPreferences(), fileMonitor);
        try {
            viewModel.addProjects(manager.getProjects());
        } catch (IOException e) {
            LOGGER.error("Could not add projects", e);
        }

        tblProjects.setEditable(true);
        colActive.setEditable(true);

        colActive.setCellFactory(CheckBoxTableCell.forTableColumn(colActive));

        colActive.setCellValueFactory(cellData -> cellData.getValue().isActiveProperty());
        colTitle.setCellValueFactory(cellData -> cellData.getValue().getProjectTitle());
        colFirstName.setCellValueFactory(cellData -> cellData.getValue().getFirstName());
        colLastName.setCellValueFactory(cellData -> cellData.getValue().getLastName());
        colLastModified.setCellValueFactory(cellData -> cellData.getValue().getLastUpdated());
        setBindings();

    }

    private void setBindings() {
        tblProjects.itemsProperty().bindBidirectional(viewModel.projectsProperty());
    }

    @FXML
    private void synchronizeLibrary() {
        Optional<ShareLatexProjectViewModel> projects = viewModel.projectsProperty().filtered(x -> x.isActive())
                                                                 .stream().findFirst();

        if (projects.isPresent() && stateManager.getActiveDatabase().isPresent()) {
            String projectID = projects.get().getProjectId();
            BibDatabaseContext database = stateManager.getActiveDatabase().get();
            manager.startWebSocketHandler(projectID, database, preferences.getImportFormatPreferences(), fileMonitor);
        }

    }

    @FXML
    private void cancelAndClose() {
    }

}
