package team_f.client.pages.musicalwork;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Callback;
import jfxtras.labs.scene.control.BigDecimalField;
import team_f.client.controls.numberfield.NumberField;
import team_f.client.converter.MusicalWorkConverter;
import team_f.client.exceptions.NumberRangeException;
import team_f.client.helper.ErrorMessageHelper;
import team_f.client.pages.BaseTablePage;
import team_f.jsonconnector.entities.*;
import team_f.jsonconnector.entities.Error;
import team_f.jsonconnector.entities.special.errorlist.MusicalWorkErrorList;
import team_f.jsonconnector.interfaces.JSONObjectEntity;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MusicalWorkManagement extends BaseTablePage<MusicalWorkErrorList, MusicalWork, MusicalWork, MusicalWorkParameter> {
    private TextField _nameField;
    private TextField _composerField;
    private TableView<MusicalWork> _table;

    //String
    private NumberField _firstViolinField;
    private NumberField _secondViolinField;
    private NumberField _violaField;
    private NumberField _violoncelloField;
    private NumberField _doublebassField;

    //Wood
    private NumberField _fluteField;
    private NumberField _oboeField;
    private NumberField _clarinetField;
    private NumberField _bassoonField;

    //Brass
    private NumberField _hornField;
    private NumberField _trumpetField;
    private NumberField _tromboneField;
    private NumberField _tubeField;

    //Percussion
    private NumberField _kettledrumField;
    private NumberField _percussionField;
    private NumberField _harpField;
    private HBox _textfields;
    private List<BigDecimalField> _fields;

    //SpecialInstrumentation
    private TextField _specialInstrumentation;
    private Button _addButton;
    private Button _updateButton;
    private Button _editButton;
    private Button _deleteButton;
    private Button _cancelButton;

    public MusicalWorkManagement() {
    }

    @Override
    public void initialize() {
        if (_initialize != null) {
            _initialize.doAction(null);
        }
        final URL Style = ClassLoader.getSystemResource("style/stylesheetMusicalWork.css");
        getStylesheets().add(Style.toString());
        _fields = new ArrayList<>();
        _nameField = new TextField();
        _nameField.setMinWidth(200);
        _composerField = new TextField();
        _composerField.setMinWidth(200);

        try {
            _firstViolinField = new NumberField(0, 0, Integer.MAX_VALUE);
            _firstViolinField.setMaxWidth(60);
            _fields.add(_firstViolinField);
            _secondViolinField = new NumberField(0, 0, Integer.MAX_VALUE);
            _secondViolinField.setMaxWidth(60);
            _fields.add(_secondViolinField);
            _violaField = new NumberField(0, 0, Integer.MAX_VALUE);
            _violaField.setMaxWidth(60);
            _fields.add(_violaField);
            _violoncelloField = new NumberField(0, 0, Integer.MAX_VALUE);
            _violoncelloField.setMaxWidth(60);
            _fields.add(_violoncelloField);
            _doublebassField = new NumberField(0, 0, Integer.MAX_VALUE);
            _doublebassField.setMaxWidth(60);
            _fields.add(_doublebassField);

            _fluteField = new NumberField(0, 0, Integer.MAX_VALUE);
            _fluteField.setMaxWidth(60);
            _fields.add(_fluteField);
            _oboeField = new NumberField(0, 0, Integer.MAX_VALUE);
            _oboeField.setMaxWidth(60);
            _fields.add(_oboeField);
            _clarinetField = new NumberField(0, 0, Integer.MAX_VALUE);
            _clarinetField.setMaxWidth(60);
            _fields.add(_clarinetField);
            _bassoonField = new NumberField(0, 0, Integer.MAX_VALUE);
            _bassoonField.setMaxWidth(60);
            _fields.add(_bassoonField);

            _hornField = new NumberField(0, 0, Integer.MAX_VALUE);
            _hornField.setMaxWidth(60);
            _fields.add(_hornField);
            _trumpetField = new NumberField(0, 0, Integer.MAX_VALUE);
            _trumpetField.setMaxWidth(60);
            _fields.add(_trumpetField);
            _tromboneField = new NumberField(0, 0, Integer.MAX_VALUE);
            _tromboneField.setMaxWidth(60);
            _fields.add(_tromboneField);
            _tubeField = new NumberField(0, 0, Integer.MAX_VALUE);
            _tubeField.setMaxWidth(60);
            _fields.add(_tubeField);

            _kettledrumField = new NumberField(0, 0, Integer.MAX_VALUE);
            _kettledrumField.setMaxWidth(60);
            _fields.add(_kettledrumField);
            _percussionField = new NumberField(0, 0, Integer.MAX_VALUE);
            _percussionField.setMaxWidth(60);
            _fields.add(_percussionField);
            _harpField = new NumberField(0, 0, Integer.MAX_VALUE);
            _harpField.setMaxWidth(60);
            _fields.add(_harpField);
        } catch (NumberRangeException e) {
        }

        _specialInstrumentation = new TextField();

        _table = new TableView<>();
        _table.setEditable(false);

        TableView.TableViewSelectionModel<MusicalWork> tsm = _table.getSelectionModel();
        tsm.setSelectionMode(SelectionMode.SINGLE);

        _table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        _table.getColumns().addListener((ListChangeListener) change -> {
            change.next();
            if (change.wasReplaced()) {
                update();
            }
        });
        _table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                _editButton.setDisable(false);
                _deleteButton.setDisable(false);
            } else {
                _editButton.setDisable(true);
                _deleteButton.setDisable(true);
            }
        });

        GridPane newDataPane = getNewMusicalWorkDataPane();

        // Create the Delete Button and add Event-Handler
        _deleteButton = new Button("Delete Selected Work");
        _deleteButton.setDisable(true);
        _deleteButton.setOnAction(e -> deleteMusicalWork());


        HBox buttonsBox = new HBox(_editButton, _deleteButton);
        buttonsBox.setSpacing(10);

        VBox root = new VBox();
        root.getChildren().addAll(newDataPane, _table, buttonsBox);
        root.setSpacing(5);
        BorderPane borderPane = new BorderPane();
        borderPane.setId("borderPane");

        borderPane.setCenter(root);
        setCenter(borderPane);
    }

    @Override
    public void load() {
        if (_load != null) {
        }

        loadList();
    }

    @Override
    public void update() {
        _table.getColumns().clear();
        _table.getColumns().addAll(MusicalWorkHelper.getIdColumn(), MusicalWorkHelper.getMusicalWorkNameColumn(),
                MusicalWorkHelper.getComposerColumn(), MusicalWorkHelper.getInstrumentationColumn());
    }

    @Override
    public void exit() {
        if (_exit != null) {
            _exit.doAction(null);
        }
    }

    @Override
    public void dispose() {
    }

    public void addMusicalWork() {
        if (_create != null) {
            MusicalWork musicalWork = new MusicalWork();
            musicalWork.setName(_nameField.getText());
            musicalWork.setComposer(_composerField.getText());
            Instrumentation instrumentation = new Instrumentation();

            instrumentation.setViolin1(Integer.parseInt(_firstViolinField.getText()));
            instrumentation.setViolin2(Integer.parseInt(_secondViolinField.getText()));
            instrumentation.setViola(Integer.parseInt(_violaField.getText()));
            instrumentation.setViolincello(Integer.parseInt(_violoncelloField.getText()));
            instrumentation.setDoublebass(Integer.parseInt(_doublebassField.getText()));

            instrumentation.setFlute(Integer.parseInt(_fluteField.getText()));
            instrumentation.setOboe(Integer.parseInt(_oboeField.getText()));
            instrumentation.setClarinet(Integer.parseInt(_clarinetField.getText()));
            instrumentation.setBassoon(Integer.parseInt(_bassoonField.getText()));

            instrumentation.setHorn(Integer.parseInt(_hornField.getText()));
            instrumentation.setTrumpet(Integer.parseInt(_trumpetField.getText()));
            instrumentation.setTrombone(Integer.parseInt(_tromboneField.getText()));
            instrumentation.setTube(Integer.parseInt(_tubeField.getText()));

            instrumentation.setKettledrum(Integer.parseInt(_kettledrumField.getText()));
            instrumentation.setPercussion(Integer.parseInt(_percussionField.getText()));
            instrumentation.setHarp(Integer.parseInt(_harpField.getText()));


            //instrumentation.setSpecialInstrumentation();

            musicalWork.setInstrumentation(instrumentation);

            MusicalWorkErrorList resultMusicalWorkErrorList = _create.doAction(musicalWork);

            if (resultMusicalWorkErrorList != null && resultMusicalWorkErrorList.getKeyValueList() != null) {
                List<Pair<JSONObjectEntity, List<Error>>> errorList = MusicalWorkConverter.getAbstractList(resultMusicalWorkErrorList.getKeyValueList());
                String tmpErrorText = ErrorMessageHelper.getErrorMessage(errorList);

                if (tmpErrorText.isEmpty() && resultMusicalWorkErrorList.getKeyValueList().size() == 1 && resultMusicalWorkErrorList.getKeyValueList().get(0).getKey() != null && resultMusicalWorkErrorList.getKeyValueList().get(0).getKey().getMusicalWorkID() > 0) {
                    showSuccessMessage("Successful", tmpErrorText);

                    _table.getItems().add(resultMusicalWorkErrorList.getKeyValueList().get(0).getKey());
                    update();
                } else {
                    showErrorMessage("Error", tmpErrorText);
                }
            } else {
                showTryAgainLaterErrorMessage();
            }
        }

        reset();
    }

    public void deleteMusicalWork() {
        if (_delete != null) {
            MusicalWork musicalWork = new MusicalWork();

            MusicalWorkErrorList resultMusicalWorkErrorList = _create.doAction(musicalWork);

            if (resultMusicalWorkErrorList != null && resultMusicalWorkErrorList.getKeyValueList() != null) {
                List<Pair<JSONObjectEntity, List<Error>>> errorList = MusicalWorkConverter.getAbstractList(resultMusicalWorkErrorList.getKeyValueList());
                String tmpErrorText = ErrorMessageHelper.getErrorMessage(errorList);

                if (tmpErrorText.isEmpty() && resultMusicalWorkErrorList.getKeyValueList().size() == 1 && resultMusicalWorkErrorList.getKeyValueList().get(0).getKey() != null && resultMusicalWorkErrorList.getKeyValueList().get(0).getKey().getMusicalWorkID() > 0) {
                    showSuccessMessage("Successful", tmpErrorText);

                    _table.getItems().remove(resultMusicalWorkErrorList.getKeyValueList().get(0).getKey());
                    update();
                } else {
                    showErrorMessage("Error", tmpErrorText);
                }
            } else {
                showTryAgainLaterErrorMessage();
            }
        }
    }

    private GridPane getNewMusicalWorkDataPane() {
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);

        pane.getColumnConstraints().addAll(new ColumnConstraints(90), new ColumnConstraints(90), new ColumnConstraints(90),
                new ColumnConstraints(90), new ColumnConstraints(90), new ColumnConstraints(90), new ColumnConstraints(90), new ColumnConstraints(90), new ColumnConstraints(120));

        Label titleMusicalWork = new Label("MusicalWork");
        titleMusicalWork.setId("titleMusicalWork");
        titleMusicalWork.setMinWidth(200);

        _textfields = new HBox();
        _textfields.setMinWidth(600);
        _textfields.setSpacing(10);
        Label nameLabel = new Label("Name:");
        nameLabel.setMinWidth(60);
        Label composerLabel = new Label("Composer:");
        composerLabel.setMinWidth(60);
        _textfields.getChildren().addAll(nameLabel, _nameField, composerLabel, _composerField);
        pane.addRow(0, titleMusicalWork);
        pane.addRow(1, _textfields);

        //Strings
        pane.addRow(4, new Label("1. Violin:"), _firstViolinField);
        pane.addRow(5, new Label("2. Violin:"), _secondViolinField);
        pane.addRow(6, new Label("Viola:"), _violaField);
        pane.addRow(7, new Label("Violoncello:"), _violoncelloField);
        pane.addRow(8, new Label("Doublebass:"), _doublebassField);

        //Wood
        pane.addRow(4, new Label("Flute:"), _fluteField);
        pane.addRow(5, new Label("Oboe:"), _oboeField);
        pane.addRow(6, new Label("Clarinet:"), _clarinetField);
        pane.addRow(7, new Label("Bassoon:"), _bassoonField);

        //Brass
        pane.addRow(4, new Label("Horn:"), _hornField);
        pane.addRow(5, new Label("Trumpet:"), _trumpetField);
        pane.addRow(6, new Label("Trombone:"), _tromboneField);
        pane.addRow(7, new Label("Tube:"), _tubeField);

        //Percussion
        pane.addRow(4, new Label("Kettledrum:"), _kettledrumField);
        pane.addRow(5, new Label("Percussion:"), _percussionField);
        pane.addRow(6, new Label("Harp:"), _harpField);

        pane.addRow(4, _specialInstrumentation);


        List<TextField> textFields = new LinkedList();
        textFields.add(_nameField);
        textFields.add(_composerField);

        List<BigDecimalField> decimalFields = new LinkedList<>();
        decimalFields.add(_firstViolinField);
        decimalFields.add(_secondViolinField);
        decimalFields.add(_violaField);
        decimalFields.add(_violoncelloField);
        decimalFields.add(_doublebassField);

        decimalFields.add(_fluteField);
        decimalFields.add(_oboeField);
        decimalFields.add(_clarinetField);
        decimalFields.add(_bassoonField);

        decimalFields.add(_hornField);
        decimalFields.add(_trumpetField);
        decimalFields.add(_tromboneField);
        decimalFields.add(_tubeField);

        decimalFields.add(_kettledrumField);
        decimalFields.add(_percussionField);
        decimalFields.add(_harpField);


        _addButton = new Button("Add MusicalWork");
        _addButton.setMinWidth(130);
        _addButton.setOnAction(e -> {
            if (_nameField.getText().isEmpty() || _composerField.getText().isEmpty() ||
                    _firstViolinField.getText().isEmpty() || _secondViolinField.getText().isEmpty() || _violaField.getText().isEmpty() || _violoncelloField.getText().isEmpty() ||
                    _doublebassField.getText().isEmpty() || _fluteField.getText().isEmpty() || _oboeField.getText().isEmpty() || _clarinetField.getText().isEmpty() ||
                    _bassoonField.getText().isEmpty() || _hornField.getText().isEmpty() || _trumpetField.getText().isEmpty() || _tromboneField.getText().isEmpty() || _tubeField.getText().isEmpty()
                    || _kettledrumField.getText().isEmpty() || _percussionField.getText().isEmpty() || _harpField.getText().isEmpty()) {
                validate(textFields);
                validate(decimalFields);
                showValuesMissingMessage();
            } else {
                addMusicalWork();
            }
        });

        _editButton = new Button("Edit Selected Work");
        _editButton.setDisable(true);
        _editButton.setMinWidth(125);
        _editButton.setOnAction(e -> {
            _table.setDisable(true);
            _addButton.setDisable(true);
            _updateButton.setDisable(false);
            _editButton.setDisable(true);
            _deleteButton.setDisable(true);
            _cancelButton.setText("Cancel Update");
            fillFields((MusicalWork) _table.getSelectionModel().getSelectedItem());
        });

        _updateButton = new Button("Update Work");
        _updateButton.setMinWidth(130);
        _updateButton.setDisable(true);
        _updateButton.setOnAction(e -> {
            _table.setDisable(false);
            editWork();
            reset();
        });

        _cancelButton = new Button("Reset");
        _cancelButton.setMinWidth(125);
        _cancelButton.setOnAction(e -> {
            _table.setDisable(false);
            reset();
        });

        pane.add(new Label("Instrumentation:"), 0, 2);
        pane.add(new Label("String:"), 0, 3);
        pane.add(new Label("Wood:"), 2, 3);
        pane.add(new Label("Brass:"), 4, 3);
        pane.add(new Label("Percussion:"), 6, 3);
        pane.add(new Label("Special Instruments:"), 8, 3);
        pane.add(_addButton, 8, 8);
        pane.add(_updateButton, 8, 9);
        pane.add(_cancelButton, 0, 9);
        return pane;
    }

    private void loadList() {
        if (_loadList != null) {
            MusicalWorkParameter musicalWorkParameter = new MusicalWorkParameter();
            List<MusicalWork> musicalWorkList = _loadList.doAction(musicalWorkParameter);

            if (musicalWorkList != null) {
                _table.setItems(FXCollections.observableList(musicalWorkList));
                update();
            } else {
                showTryAgainLaterErrorMessage();
            }
        }
    }

    public void editWork() {
        if (_edit != null) {
            MusicalWork musicalWork = _table.getSelectionModel().getSelectedItem();
        }

        //TODO: look in MusicianManagement editPerson() methode, should be nearly the same
    }

    private void reset() {
        _nameField.clear();
        _nameField.setStyle("-fx-border-color: transparent");
        _composerField.clear();
        _composerField.setStyle("-fx-border-color: transparent");
        _table.getSelectionModel().clearSelection();
        _addButton.setDisable(false);
        _editButton.setDisable(true);
        _deleteButton.setDisable(true);
        _updateButton.setDisable(true);
        _cancelButton.setText("Reset");
        for (BigDecimalField field : _fields) {
            field.setNumber(new BigDecimal(0));
            field.setStyle("-fx-border-color: transparent");
        }
    }

    public void fillFields(MusicalWork musicalWork) {
        if (musicalWork.getComposer() != null) {
            _composerField.setText(musicalWork.getComposer());
        }
        if (musicalWork.getName() != null) {
            _nameField.setText(musicalWork.getName());
        }
        if (musicalWork.getInstrumentation() != null) {
            Instrumentation instrumentation = musicalWork.getInstrumentation();
            _firstViolinField.setNumber(new BigDecimal(instrumentation.getViolin1()));
            _secondViolinField.setNumber(new BigDecimal(instrumentation.getViolin2()));
            _violaField.setNumber(new BigDecimal(instrumentation.getViola()));
            _violoncelloField.setNumber(new BigDecimal(instrumentation.getViolincello()));
            _doublebassField.setNumber(new BigDecimal(instrumentation.getDoublebass()));

            _fluteField.setNumber(new BigDecimal(instrumentation.getFlute()));
            _oboeField.setNumber(new BigDecimal(instrumentation.getOboe()));
            _clarinetField.setNumber(new BigDecimal(instrumentation.getClarinet()));
            _bassoonField.setNumber(new BigDecimal(instrumentation.getBassoon()));

            _hornField.setNumber(new BigDecimal(instrumentation.getHorn()));
            _trumpetField.setNumber(new BigDecimal(instrumentation.getTrumpet()));
            _tromboneField.setNumber(new BigDecimal(instrumentation.getTrombone()));
            _tubeField.setNumber(new BigDecimal(instrumentation.getTube()));

            _kettledrumField.setNumber(new BigDecimal(instrumentation.getKettledrum()));
            _percussionField.setNumber(new BigDecimal(instrumentation.getPercussion()));
            _harpField.setNumber(new BigDecimal(instrumentation.getHarp()));

            if (instrumentation.getSpecialInstrumentation() != null) {
                _specialInstrumentation.setText(instrumentation.getSpecialInstrumentation().toString());
            }
        }
    }


}
