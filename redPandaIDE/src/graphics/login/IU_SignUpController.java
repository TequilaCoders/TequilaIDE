package graphics.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import entities.Usuario;
import java.net.URL;
import java.security.MessageDigest;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.bind.DatatypeConverter;

/**
 * FXML Controller class
 *
 * @author alanc
 */
public class IU_SignUpController implements Initializable {

  private ResourceBundle rb;

  @FXML
  private Hyperlink botonIniciarSesion;

  private IU_LogInController parentController;

  @FXML
  private JFXTextField tfName;

  @FXML
  private JFXButton buttonSignUp;

  @FXML
  private JFXTextField tfAlias;

  @FXML
  private JFXTextField tfEmail;

  @FXML
  private JFXPasswordField pfPassword;

  @FXML
  private JFXPasswordField pfConfirmPassword;

  @FXML
  private ImageView imAliasRedCross;

  @FXML
  private ImageView imEmailRedCross;

  @FXML
  private ImageView imPasswordRedCross;

  boolean emailStatus = false;
  boolean aliasStatus = false;

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    this.rb = rb;
  }

  @FXML
  private void eventLogIn(ActionEvent event) throws InterruptedException {
    parentController.closeDrawer();
  }

  public void setParentController(IU_LogInController parentController) {
    this.parentController = parentController;
  }

  @FXML
  void registerUser(ActionEvent event) {
    String name;
    String alias;
    String email;
    String password;
    String hashedPassword;

    name = tfName.getText();
    alias = tfAlias.getText().toLowerCase();
    email = tfEmail.getText().toLowerCase();
    password = pfPassword.getText();

    hashedPassword = getHashedPassword(password);
    createUser(name, alias, email, hashedPassword);

  }

  @FXML
  void verifyFieldsListener(KeyEvent event) {
    String name;
    String alias;
    String email;
    String password;
    String confirmedPassword;

    name = tfName.getText();
    alias = tfAlias.getText().toLowerCase();
    email = tfEmail.getText().toLowerCase();
    password = pfPassword.getText();
    confirmedPassword = pfConfirmPassword.getText();

    boolean emptyStatus = thereAreEmptyFields(name, alias, email, password, confirmedPassword);;
    isAliasDuplicate();
    isEmailDuplicate();

    boolean passwordStatus = isPasswordConfirmationCorrect(password, confirmedPassword);

    //A ELIMINAR!!------------------------
    System.out.println("empty: " + emptyStatus);
    System.out.println("alias: " + aliasStatus);
    System.out.println("email: " + emailStatus);
    System.out.println("password: " + passwordStatus);
    //_---------------------------------------------

    if (!emptyStatus && !aliasStatus && !emailStatus && passwordStatus) {
      buttonSignUp.setDisable(false);
    } else {
      buttonSignUp.setDisable(true);
    }
  }

  public Usuario createUser(String name, String alias, String email, String password) {

    Usuario newUser = new Usuario();
    newUser.setNombres(name);
    newUser.setAlias(alias);
    newUser.setCorreo(email);
    newUser.setClave(password);

    EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("redPandaIDEPU");
    EntityManager entitymanager = emfactory.createEntityManager();

    entitymanager.getTransaction().begin();
    entitymanager.persist(newUser);
    entitymanager.getTransaction().commit();

    entitymanager.close();

    System.out.println("se creo usuario");

    return newUser;
  }

  public boolean thereAreEmptyFields(String name, String alias, String email, String password, String confirmedPassword) {
    boolean isEmpty = true;
    if (!name.trim().isEmpty() && !alias.trim().isEmpty() && !email.trim().isEmpty()
        && !password.trim().isEmpty() && !confirmedPassword.trim().isEmpty()) {
      isEmpty = false;
    }
    return isEmpty;
  }

  public void isAliasDuplicate() {
    tfAlias.setOnKeyTyped((KeyEvent event) -> {

      String alias = tfAlias.getText() + event.getCharacter();

      List<Usuario> coincidenceList;
      EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("redPandaIDEPU");
      EntityManager entitymanager = emfactory.createEntityManager();

      coincidenceList = entitymanager.createNamedQuery("Usuario.compareAlias",
          Usuario.class)
          .setParameter("alias", alias)
          .getResultList();

      if (coincidenceList.isEmpty() || tfAlias.getText().isEmpty()) { //esta duplicado

        aliasStatus = false;
        tfAlias.setFocusColor(Paint.valueOf("#77d2ff"));
        tfAlias.setUnFocusColor(Paint.valueOf("#17a589"));

        tfAlias.setPromptText("Alias");
        tfAlias.setStyle("-fx-prompt-text-fill: #6494ed; -fx-text-fill: #FFFFFF");
        imAliasRedCross.setVisible(false);
      } else {
        aliasStatus = true;
        tfAlias.setFocusColor(Paint.valueOf("orange"));
        tfAlias.setUnFocusColor(Paint.valueOf("orange"));
        tfAlias.setPromptText("Alias (ya existe el Alias)");
        tfAlias.setStyle("-fx-prompt-text-fill: orange; -fx-text-fill: #FFFFFF");
        imAliasRedCross.setVisible(true);
      }
    });
  }

  public void isEmailDuplicate() {
    tfEmail.setOnKeyTyped((KeyEvent event) -> {

      String email = tfEmail.getText() + event.getCharacter();

      List<Usuario> coincidenceList;
      EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("redPandaIDEPU");
      EntityManager entitymanager = emfactory.createEntityManager();

      coincidenceList = entitymanager.createNamedQuery("Usuario.compareEmail",
          Usuario.class)
          .setParameter("email", email)
          .getResultList();

      if (coincidenceList.isEmpty() || tfEmail.getText().isEmpty()) { // esta duplicado
        emailStatus = false;
        tfEmail.setFocusColor(Paint.valueOf("#77d2ff"));
        tfEmail.setUnFocusColor(Paint.valueOf("#17a589"));
        tfEmail.setPromptText("Email");
        tfEmail.setStyle("-fx-prompt-text-fill: #6494ed; -fx-text-fill: #FFFFFF");

        imEmailRedCross.setVisible(false);
      } else {
        emailStatus = true;
        tfEmail.setFocusColor(Paint.valueOf("orange"));
        tfEmail.setUnFocusColor(Paint.valueOf("orange"));
        tfEmail.setPromptText("Email (ya existe el Email)");
        tfEmail.setStyle("-fx-prompt-text-fill: orange; -fx-text-fill: #FFFFFF");
        imEmailRedCross.setVisible(true);
      }

    });
  }

  public boolean isPasswordConfirmationCorrect(String password, String confirmedPassword) {
    boolean status = false;
    if (password.equals(confirmedPassword) || pfConfirmPassword.getText().isEmpty()) {
      status = true;
      pfConfirmPassword.setFocusColor(Paint.valueOf("#77d2ff"));
      pfConfirmPassword.setUnFocusColor(Paint.valueOf("#77d2ff"));
      pfConfirmPassword.setPromptText("Confirmar Contraseña");
      pfConfirmPassword.setStyle("-fx-prompt-text-fill: #6494ed; -fx-text-fill: #FFFFFF");
      imPasswordRedCross.setVisible(false);
    } else {
      pfConfirmPassword.setFocusColor(Paint.valueOf("orange"));
      pfConfirmPassword.setUnFocusColor(Paint.valueOf("orange"));
      pfConfirmPassword.setPromptText("Confirm Password (las claves no coinciden)");
      pfConfirmPassword.setStyle("-fx-prompt-text-fill: orange; -fx-text-fill: #FFFFFF");
      imPasswordRedCross.setVisible(true);
    }
    return status;
  }

  private String getHashedPassword(String password) {
    String result = null;

    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(password.getBytes("UTF-8"));
      return bytesToHex(hash); // make it printable
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return result;
  }

  private String bytesToHex(byte[] hash) {
    return DatatypeConverter.printHexBinary(hash);
  }
}
